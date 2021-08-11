package client.store;

import client.request.SocketHandler;
import client.request.exception.ConnectionException;
import com.google.gson.reflect.TypeToken;
import shared.exception.ValidationException;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MyProfileStore extends Store {
    private static MyProfileStore instance;

    private String authToken;
    private User user;

    public static MyProfileStore getInstance() {
        if (instance == null)
            instance = new MyProfileStore();
        return instance;
    }

    public void updateUserProfile() {
        Packet packet = new Packet("my-profile");
        Packet response = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        if (response.getStatus() != StatusCode.OK)
            return;
        user = response.getObject("user", User.class);

        packet = new Packet("profile");
        packet.put("id", user.id);
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        Type listType = new TypeToken<ArrayList<User>>(){}.getType();
        user.setFollowers(res.getObject("followers", listType));
        user.setFollowings(res.getObject("following", listType));
        user.setBlocked(res.getObject("blocked", listType));
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
        packet.putObject("photo", user.getPhoto());
        Packet response = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        switch (response.getStatus()){
            case BAD_GATEWAY:
                throw new ConnectionException(ConnectionException.ErrorType.CONNECTION_ERROR);
            case BAD_REQUEST:
                throw response.getObject("error", ValidationException.class);
            case INTERNAL_SERVER_ERROR:
                throw new ConnectionException(ConnectionException.ErrorType.INTERNAL_SERVER_ERROR);
        }
        if (response.getStatus() != StatusCode.OK) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        commitChanges();
                    } catch (Exception ignored) {}
                }
            }, config.getProperty(Integer.class, "STORE_COMMIT_RATE"));
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
