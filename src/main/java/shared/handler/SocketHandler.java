package shared.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.request.Packet;
import shared.request.PacketListener;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public abstract class SocketHandler implements PacketListener {
    private static final Logger logger = LogManager.getLogger(SocketHandler.class);

    private final ArrayList<Runnable> listeners = new ArrayList<>();
    protected final Socket socket;
    protected final ObjectInputStream inputStream;
    protected final ObjectOutputStream outputStream;
    private final ReentrantLock outputStreamLock = new ReentrantLock();

    public SocketHandler(Socket socket) throws IOException {
        this.socket = socket;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        Thread clientThread = new Thread(this::inputListener);
        clientThread.start();
    }

    public void addDisconnectListener(Runnable listener) {
        listeners.add(listener);
    }

    private void inputListener() {
        while (true) {
            try {
                Packet packet = (Packet) inputStream.readObject();
                System.out.println("Got packet - " + packet.target);
                listenPacket(packet);
            }
            catch (EOFException | SocketException e) {
                break;
            }
            catch (Exception e) {
                logger.error("invalid request was made -" + e.getMessage());
                e.printStackTrace();
                break;
            }
        }
        for (Runnable listener: listeners) {
            try {
                listener.run();
            }
            catch (Exception e) {
                logger.error("error in socket disconnect listener - " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void sendPacket(Packet packet) throws SocketException {
        outputStreamLock.lock();
        try {
            outputStream.writeObject(packet);
        } catch (SocketException e) {
            throw e;
        } catch (IOException e) {
            logger.error("failed to send response to user - " + e.getMessage());
            e.printStackTrace();
        }
        outputStreamLock.unlock();
    }
}
