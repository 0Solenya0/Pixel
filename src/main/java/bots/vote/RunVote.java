package bots.vote;

import bots.vote.store.VoteStore;
import client.request.SocketHandler;
import client.request.exception.ConnectionException;
import client.store.MessageStore;
import client.store.MyProfileStore;
import shared.exception.ValidationException;
import shared.models.fields.AccessLevel;
import shared.request.Packet;

public class RunVote {
    public static void main(String[] args) throws ValidationException, ConnectionException {
        register();
        login();
        MyProfileStore.getInstance().updateUserProfile();
        MyProfileStore.getInstance().getUser().setVisibility(AccessLevel.PUBLIC);
        MyProfileStore.getInstance().commitChanges();
        MessageStore.getInstance().refreshData();
        VoteStore.setInstance((VoteStore) VoteStore.getInstance().load());
        MessageHandler messageHandler = new MessageHandler();
    }

    public static void register() {
        Packet packet = new Packet("register");
        packet.put("username", "vote_bot");
        packet.put("name", "vote");
        packet.put("surname", "bot");
        packet.put("mail", "vote@u.com");
        packet.put("password", "abbas");
        Packet response = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
    }

    public static void login() {
        Packet packet = new Packet("login");
        packet.put("username", "vote_bot");
        packet.put("password", "abbas");
        Packet response = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        MyProfileStore.getInstance().setAuthToken(response.get("auth-token", null));
    }
}
