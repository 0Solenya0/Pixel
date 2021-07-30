package client.store;

import client.request.SocketHandler;
import client.request.exception.ConnectionException;
import shared.exception.ValidationException;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

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

    public void commitChanges() throws ConnectionException, ValidationException {
        Packet packet = new Packet("update-profile");
        packet.put("name", user.getName());
        packet.put("surname", user.getSurname());
        packet.put("phone-number", user.getPhone().get());
        packet.putObject("phone-access", user.getPhone().getAccessLevel());
        packet.putObject("birthday", user.getBirthdate().get());
        packet.putObject("birthday-access", user.getBirthdate().getAccessLevel());
        packet.put("bio", user.getBio());
        packet.putObject("mail-access", user.getMail().getAccessLevel());
        packet.putObject("last-seen-access", user.getLastSeen().getAccessLevel());
        packet.putObject("visibility", user.getVisibility());
        Packet response = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        switch (response.getStatus()){
            case BAD_GATEWAY:
                throw new ConnectionException(ConnectionException.ErrorType.CONNECTION_ERROR);
            case BAD_REQUEST:
                throw response.getObject("error", ValidationException.class);
            case INTERNAL_SERVER_ERROR:
                throw new ConnectionException(ConnectionException.ErrorType.INTERNAL_SERVER_ERROR);
        }
        updateUserProfile();
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
