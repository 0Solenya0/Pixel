package shared.request;

import shared.request.Packet;

public interface PacketListener {
    void listenPacket(Packet packet);
}
