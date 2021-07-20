package server.request;

import shared.request.Packet;

public interface ridListener {
    Packet listenPacket(Packet packet);
}
