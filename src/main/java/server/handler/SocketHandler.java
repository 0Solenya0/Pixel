package server.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.request.Packet;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketHandler extends shared.handler.SocketHandler {
    private static final Logger logger = LogManager.getLogger(server.handler.SocketHandler.class);
    private static final ConcurrentHashMap<Integer, SocketHandler> clientMap = new ConcurrentHashMap<>();
    private static final AtomicInteger clientIDS = new AtomicInteger();
    private final int id;

    public SocketHandler(Socket socket) throws IOException {
        super(socket);
        id = clientIDS.addAndGet(1);
        clientMap.put(id, this);
    }

    public static SocketHandler getSocketHandler(int id) {
        return clientMap.get(id);
    }

    public int getId() {
        return id;
    }

    public void listenPacket(Packet packet) {
        packet.put("handler", String.valueOf(id));
        RequestHandler requestHandler = new RequestHandler(packet);
        Packet response = requestHandler.next();
        if (response != null)
            sendPacket(response);
    }

    public void sendPacket(Packet packet) {
        try {
            super.sendPacket(packet);
        } catch (SocketException e) {
            logger.error("server failed to send response - " + e.getMessage());
            System.out.println("Unexpected broken pipe");
            e.printStackTrace();
        }
    }
}
