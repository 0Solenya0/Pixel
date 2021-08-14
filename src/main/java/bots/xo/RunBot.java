package bots.xo;

import bots.xo.store.GameStore;
import com.google.gson.reflect.TypeToken;
import shared.models.User;
import shared.models.fields.AccessLevel;
import shared.request.Packet;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RunBot extends bots.bot.RunBot {
    private final Thread botThread = new Thread(this::main);
    private final PacketManager packetManager = PacketManager.getInstance();

    public bots.bot.request.PacketManager getPacketManager() {
        return packetManager;
    }

    public void run() {
        botThread.start();
    }

    public void main() {
        register();
        login();
        setProfileSettings();
    }

    public void setProfileSettings() {
        Packet packet = new Packet("update-profile");
        packet.putObject("last-seen-access", AccessLevel.PUBLIC);
        packet.putObject("visibility", AccessLevel.PUBLIC);
        packetManager.send(packet);
    }

    public void register() {
        Packet packet = new Packet("register");
        packet.put("username", "xo_bot");
        packet.put("name", "XO");
        packet.put("surname", "bot");
        packet.put("mail", "xo@u.com");
        packet.put("password", "abbas");
        packetManager.sendAndGetResponse(packet);
    }

    public void login() {
        Packet packet = new Packet("login");
        packet.put("username", "xo_bot");
        packet.put("password", "abbas");
        Packet response = packetManager.sendAndGetResponse(packet);
        packetManager.setAuthToken(response.get("auth-token", null));

        packet = new Packet("my-profile");
        response = packetManager.sendAndGetResponse(packet);
        User user = response.getObject("user", User.class);

        packet = new Packet("profile");
        packet.put("id", user.id);
        Packet res = packetManager.sendAndGetResponse(packet);
        Type listType = new TypeToken<ArrayList<User>>(){}.getType();
        user.setFollowers(res.getObject("followers", listType));
        user.setFollowings(res.getObject("following", listType));
        user.setBlocked(res.getObject("blocked", listType));

        GameStore.getInstance().setUser(user);
        GameStore.getInstance().save();
    }
}
