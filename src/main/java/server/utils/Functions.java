package server.utils;

import server.handler.SocketHandler;
import server.middlewares.Auth;
import shared.models.User;
import shared.request.Packet;

import java.util.ArrayList;
import java.util.List;

public class Functions {

    public static void notifyRefreshMessage(List<User> users) {
        for (User user: users) {
            for (SocketHandler socketHandler: Auth.getUserSocketHandler(user.id)) {
                Packet packet = new Packet("refresh-message");
                socketHandler.sendPacket(packet);
            }
        }
    }

    public static void notifyRefreshMessage(User user) {
        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        notifyRefreshMessage(users);
    }
}
