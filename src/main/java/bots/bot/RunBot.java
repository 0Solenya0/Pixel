package bots.bot;

import bots.bot.request.PacketManager;

public abstract class RunBot {

    public abstract PacketManager getPacketManager();

    public abstract void run();
}
