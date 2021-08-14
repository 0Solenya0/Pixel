package bots.vote;

import bots.vote.MessageHandler;
import shared.request.Packet;

public class PacketManager extends bots.bot.request.PacketManager {
    private static PacketManager instance;
    MessageHandler messageHandler = new MessageHandler();

    @Override
    public void listen(Packet packet) {
        if (packet.target.equals("refresh-message"))
            messageHandler.getNewMessages();
    }

    public static PacketManager getInstance() {
        if (instance == null)
            instance = new PacketManager();
        return instance;
    }
}
