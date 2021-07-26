package client.request;

import client.request.exception.ConnectionException;
import client.store.MyProfile;
import client.views.ViewManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.lock.CustomLock;
import shared.request.Packet;
import shared.request.PacketListener;
import shared.util.Config;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class SocketHandler extends shared.handler.SocketHandler {
    private static final Config config = Config.getConfig("mainConfig");
    private static final ConcurrentHashMap<Integer, PacketListener> ridListeners = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, PacketListener> targetListener = new ConcurrentHashMap<>();
    private static final AtomicInteger lastRid = new AtomicInteger();
    private static final Logger logger = LogManager.getLogger(SocketHandler.class);
    private static final ReentrantLock serviceLock = new ReentrantLock();
    private static SocketHandler socketHandler;

    public static SocketHandler getSocketHandler() throws ConnectionException {
        serviceLock.lock();
        if (socketHandler == null) {
            try {
                socketHandler = new SocketHandler(new Socket("localhost", Integer.parseInt(config.getProperty("PORT"))));
            } catch (IOException e) {
                logger.info("failed to open new connection with server");
                throw new ConnectionException();
            }
        }
        serviceLock.unlock();
        return socketHandler;
    }

    public static void killSocket() {
        serviceLock.lock();
        try {
            socketHandler.socket.close();
        } catch (IOException e) {
            System.out.println("failed to close socket");
            logger.error("failed to close socket");
            e.printStackTrace();
        }
        socketHandler = null;
        serviceLock.unlock();
    }

    public static SocketHandler getSocketHandlerWithoutException() {
        try {
            return getSocketHandler();
        } catch (ConnectionException e) {
            ViewManager.connectionError();
            return null;
        }
    }

    public SocketHandler(Socket socket) throws IOException {
        super(socket);
    }

    public void sendPacketAndListen(Packet packet, PacketListener packetListener) {
        int rid = lastRid.addAndGet(1);
        ridListeners.put(rid, packetListener);
        packet.put("m-rid", rid);
        sendPacket(packet);
    }

    public Packet sendPacketAndGetResponse(Packet packet) {
        AtomicReference<Packet> response = new AtomicReference<>();
        CustomLock lock = new CustomLock();
        int rid = lastRid.addAndGet(1);
        packet.put("m-rid", rid);
        lock.lock();
        ridListeners.put(rid, (p) -> {
            response.set(p);
            lock.unlock();
        });
        sendPacket(packet);
        lock.lock();
        return response.get();
    }

    @Override
    public void sendPacket(Packet packet) {
        if (MyProfile.getInstance().getAuthToken() != null)
            packet.put("auth-token", MyProfile.getInstance().getAuthToken());
        try {
            super.sendPacket(packet);
        }
        catch (SocketException e) {
            ViewManager.connectionError();
        }
    }

    public void addTargetListener(String target, PacketListener listener) {
        targetListener.put(target, listener);
    }

    @Override
    public void listenPacket(Packet packet) {
        if (packet.hasKey("m-rid"))
            ridListeners.get(packet.getInt("m-rid")).listenPacket(packet);
        if (targetListener.containsKey(packet.target))
            targetListener.get(packet.target).listenPacket(packet);
    }
}
