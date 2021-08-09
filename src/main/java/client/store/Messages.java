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
    private ArrayList<Message> pendingMessages = new ArrayList<>();
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
            if (message.getReceiverGroup() != null) {
                target = groupMessages.getOrDefault(message.getReceiverGroup(), new ArrayList<>());
                groupMessages.put(message.getReceiverGroup(), target);
            }
            else if (!message.getReceiver().equals(user)) {
                target = userMessages.getOrDefault(message.getReceiver(), new ArrayList<>());
                userMessages.put(message.getReceiver(), target);
            }
            else {
                target = userMessages.getOrDefault(message.getSender(), new ArrayList<>());
                userMessages.put(message.getSender(), target);
            }
            target.remove(message);
            target.add(message);
        }
    }

    public void sendMessage(Message message) {
        pendingMessages.add(message);
    }

    public void commitChanges() {
        ArrayList<Message> messages = new ArrayList<>(pendingMessages);
        for (Message message: messages) {
            Packet packet = new Packet("send-message");
            packet.putObject("message", message);
            Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
            if (res.getStatus() == StatusCode.CREATED)
                pendingMessages.remove(message);
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
        refreshAllData(); // DELETE THIS
        // TO DO
    }

    public ArrayList<Message> getByUser(User user) {
        return userMessages.getOrDefault(user, new ArrayList<>());
    }

    public HashMap<Group, ArrayList<Message>> getGroupMessages() {
        return groupMessages;
    }

    public void setGroupMessages(HashMap<Group, ArrayList<Message>> groupMessages) {
        this.groupMessages = groupMessages;
    }

    public HashMap<User, ArrayList<Message>> getUserMessages() {
        return userMessages;
    }

    public void setUserMessages(HashMap<User, ArrayList<Message>> userMessages) {
        this.userMessages = userMessages;
    }
}
