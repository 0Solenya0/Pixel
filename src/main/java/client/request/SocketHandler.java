package client.request;

import client.request.exception.ConnectionException;
import client.store.MyProfileStore;
import client.views.ViewManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.lock.CustomLock;
import shared.request.Packet;
import shared.request.PacketListener;
import shared.request.StatusCode;
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
        if (socketHandler == null || socketHandler.socket == null) {
            try {
                socketHandler = new SocketHandler(new Socket("localhost", Integer.parseInt(config.getProperty("PORT"))));
            } catch (IOException e) {
                logger.info("failed to open new connection with server");
                serviceLock.unlock();
                throw new ConnectionException(ConnectionException.ErrorType.CONNECTION_ERROR);
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
            try {
                return new SocketHandler(null);
            } catch (IOException ignored) {
                return null;
            }
        }
    }

    public SocketHandler(Socket socket) throws IOException {
        super(socket);
    }

    public void sendPacketAndListen(Packet packet, PacketListener packetListener) {
        int rid = lastRid.addAndGet(1);
        ridListeners.put(rid, packetListener);
        packet.put("m-rid", rid);
        try {
            sendPacket(packet);
        } catch (SocketException ignored) {}
    }

    public Packet sendPacketAndGetResponse(Packet packet) {
        AtomicReference<Packet> response = new AtomicReference<>();
        response.set(new Packet(StatusCode.BAD_GATEWAY));
        CustomLock lock = new CustomLock();
        int rid = lastRid.addAndGet(1);
        packet.put("m-rid", rid);
        lock.lock();
        ridListeners.put(rid, (p) -> {
            response.set(p);
            lock.unlock();
        });
        try {
            sendPacket(packet);
            lock.lock();
        } catch (SocketException ignored) {}
        if (response.get().getStatus() == StatusCode.BAD_GATEWAY)
            ViewManager.connectionError();
        return response.get();
    }

    @Override
    public void sendPacket(Packet packet) throws SocketException {
        if (MyProfileStore.getInstance().getAuthToken() != null)
            packet.put("auth-token", MyProfileStore.getInstance().getAuthToken());
        super.sendPacket(packet);
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
