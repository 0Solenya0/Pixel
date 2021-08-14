package bots.bot.request;

import shared.request.Packet;

public interface ServerPacketInterface {
    Packet respond(Packet packet);
}
