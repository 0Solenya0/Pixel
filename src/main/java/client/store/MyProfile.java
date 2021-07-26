package client.store;

import client.request.SocketHandler;
import shared.models.User;
import shared.request.Packet;

public class MyProfile extends Store {
    private static MyProfile instance;

    private String authToken;
    private User user;

    public static MyProfile getInstance() {
        if (instance == null)
            instance = new MyProfile();
        return instance;
    }

    public void updateUserProfile() {
        Packet packet = new Packet("my-profile");
        Packet response = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        user = response.getObject("user", User.class);
    }

    public static void reset() {
        instance = null;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public User getUser() {
        return user;
    }
}
