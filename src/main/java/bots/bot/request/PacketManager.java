package bots.bot.request;

import shared.request.Packet;

public abstract class PacketManager {

    private ServerPacketInterface serverListener;
    private String authToken;

    public abstract void listen(Packet packet);

    public Packet sendAndGetResponse(Packet packet) {
        if (authToken != null)
            packet.put("auth-token", authToken);
        return serverListener.respond(packet);
    }

    public void send(Packet packet) {
        Thread thread = new Thread(() -> sendAndGetResponse(packet));
        thread.start();
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setServerListener(ServerPacketInterface serverListener) {
        this.serverListener = serverListener;
    }
}
