package bots.xo;

import bots.xo.store.GameStore;
import client.request.SocketHandler;
import client.request.exception.ConnectionException;
import client.store.MessageStore;
import client.store.MyProfileStore;
import shared.exception.ValidationException;
import shared.models.fields.AccessLevel;
import shared.request.Packet;

public class RunXO {
    public static String main() throws ValidationException, ConnectionException {
        return "abbas";
        /*register();
        login();
        MyProfileStore.getInstance().updateUserProfile();
        MyProfileStore.getInstance().getUser().setVisibility(AccessLevel.PUBLIC);
        MyProfileStore.getInstance().commitChanges();
        MessageStore.getInstance().refreshData();
        GameStore.setInstance((GameStore) GameStore.getInstance().load());
        MessageHandler messageHandler = new MessageHandler();*/
    }

    public static void register() {
        Packet packet = new Packet("register");
        packet.put("username", "xo_bot");
        packet.put("name", "XO");
        packet.put("surname", "bot");
        packet.put("mail", "xo@u.com");
        packet.put("password", "abbas");
        Packet response = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
    }

    public static void login() {
        Packet packet = new Packet("login");
        packet.put("username", "xo_bot");
        packet.put("password", "abbas");
        Packet response = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        MyProfileStore.getInstance().setAuthToken(response.get("auth-token", null));
    }
}
