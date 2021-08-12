package bots.calculator;

import bots.calculator.store.BotStore;
import client.request.SocketHandler;
import client.request.exception.ConnectionException;
import client.store.MessageStore;
import client.store.MyProfileStore;
import shared.exception.ValidationException;
import shared.models.fields.AccessLevel;
import shared.request.Packet;
import shared.request.StatusCode;

public class RunCalc {
    public static void main(String[] args) throws ValidationException, ConnectionException {
        register();
        login();
        MyProfileStore.getInstance().updateUserProfile();
        MyProfileStore.getInstance().getUser().setVisibility(AccessLevel.PUBLIC);
        MyProfileStore.getInstance().commitChanges();
        MessageStore.getInstance().refreshData();
        BotStore.setInstance((BotStore) BotStore.getInstance().load());
        MessageHandler messageHandler = new MessageHandler();
    }

    public static void register() {
        Packet packet = new Packet("register");
        packet.put("username", "calculator_bot");
        packet.put("name", "calculator");
        packet.put("surname", "bot");
        packet.put("mail", "calc@u.com");
        packet.put("password", "abbas");
        Packet response = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
    }

    public static void login() {
        Packet packet = new Packet("login");
        packet.put("username", "calculator_bot");
        packet.put("password", "abbas");
        Packet response = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        MyProfileStore.getInstance().setAuthToken(response.get("auth-token", null));
    }
}
