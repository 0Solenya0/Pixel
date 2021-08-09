package client.store;

import client.request.SocketHandler;
import com.google.gson.reflect.TypeToken;
import shared.models.Group;
import shared.models.Message;
import shared.models.Tweet;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Messages extends Store {
    private static Messages instance;
    private HashMap<Group, ArrayList<Message>> groupMessages = new HashMap<>();
    private HashMap<User, ArrayList<Message>> userMessages = new HashMap<>();
    private LocalDateTime lastFetch = LocalDateTime.MIN;

    public static Messages getInstance() {
        if (instance == null)
            instance = new Messages();
        return instance;
    }

    public void arrangeMessages(ArrayList<Message> messages) {
        groupMessages.clear();
        userMessages.clear();
        MyProfile.getInstance().updateUserProfile();
        User user = MyProfile.getInstance().getUser();

        for (Message message: messages) {
            ArrayList<Message> target;
            if (message.getReceiverGroup() != null)
                target = groupMessages.getOrDefault(message.getReceiverGroup(), new ArrayList<>());
            else if (!message.getReceiver().equals(user))
                target = userMessages.getOrDefault(message.getReceiver(), new ArrayList<>());
            else
                target = userMessages.getOrDefault(message.getSender(), new ArrayList<>());
            target.remove(message);
            target.add(message);
        }
    }

    public void refreshAllData() {
        Packet packet = new Packet("message-list");
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        if (res.getStatus() != StatusCode.OK)
            return;

        lastFetch = LocalDateTime.now();
        Type listType = new TypeToken<ArrayList<Message>>(){}.getType();
        arrangeMessages(res.getObject("list", listType));
    }

    public void updateData() {
        // TO DO
    }
}
