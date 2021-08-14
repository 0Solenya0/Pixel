package server.handler;

import bots.bot.RunBot;
import bots.bot.request.PacketManager;
import shared.request.Packet;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BotHandler {
    private static final ConcurrentHashMap<Integer, BotHandler> clientMap = new ConcurrentHashMap<>();
    private static final AtomicInteger clientIDS = new AtomicInteger();
    private final int id;
    private final PacketManager packetManager;

    @SuppressWarnings("unchecked")
    public BotHandler(String path, String claz) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        File myJar = new File(path);
        URLClassLoader child = new URLClassLoader(
                new URL[] {myJar.toURI().toURL()},
                BotHandler.class.getClassLoader()
        );
        Class<RunBot> classToLoad = (Class<RunBot>) Class.forName(claz, true, child);
        RunBot instance = classToLoad.getDeclaredConstructor().newInstance();
        packetManager = instance.getPacketManager();
        packetManager.setServerListener(this::respond);
        instance.run();
        id = -clientIDS.addAndGet(1);
        clientMap.put(id, this);
    }

    public Packet respond(Packet packet) {
        System.out.println("Bot Packet - " + packet.target);
        packet.put("handler", String.valueOf(id));
        RequestHandler requestHandler = new RequestHandler(packet);
        return requestHandler.next();
    }

    public void sendPacket(Packet packet) {
        new Thread(() -> packetManager.listen(packet)).start();
    }

    public int getId() {
        return id;
    }

    public static BotHandler getBotHandler(int hId) {
        return clientMap.get(hId);
    }
}
