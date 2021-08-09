package client.store;

import client.request.SocketHandler;
import com.google.gson.reflect.TypeToken;
import shared.models.Group;
import shared.models.Message;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class MessageStore extends Store {
    private static MessageStore instance;
    private HashMap<Group, ArrayList<Message>> groupMessages = new HashMap<>();
    private HashMap<User, ArrayList<Message>> userMessages = new HashMap<>();
    private ArrayList<Message> pendingMessages = new ArrayList<>();
    private ArrayList<Group> groups = new ArrayList<>();
    private LocalDateTime lastFetch = LocalDateTime.MIN;

    public static MessageStore getInstance() {
        if (instance == null)
            instance = new MessageStore();
        return instance;
    }

    public void arrangeMessages(ArrayList<Message> messages) {
        groupMessages.clear();
        userMessages.clear();
        MyProfileStore.getInstance().updateUserProfile();
        User user = MyProfileStore.getInstance().getUser();

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

    public ArrayList<Object> getChatGroups() {
        ArrayList<Object> list = new ArrayList<>();
        list.addAll(groups);
        list.addAll(userMessages.keySet());
        list.sort(Comparator.comparing((m) -> {
            ArrayList<Message> messages = new ArrayList<>();
            if (m instanceof User)
                messages = userMessages.getOrDefault(m, new ArrayList<>());
            else if (m instanceof Group)
                messages = groupMessages.getOrDefault(m, new ArrayList<>());
            if (messages.size() == 0)
                return LocalDateTime.MIN;
            return messages.get(messages.size() - 1).getSchedule();
        }).reversed());
        return list;
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

        packet = new Packet("group-list");
        res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        listType = new TypeToken<ArrayList<Group>>(){}.getType();
        if (res.getStatus() == StatusCode.OK)
            groups = res.getObject("list", listType);
    }

    public void updateData() {
        refreshAllData(); // DELETE THIS
        // TO DO
    }

    public ArrayList<Message> getByUser(User user) {
        ArrayList<Message> res = userMessages.getOrDefault(user, new ArrayList<>());
        res.sort(Comparator.comparing(Message::getSchedule));
        return res;
    }

    public ArrayList<Message> getByGroup(Group group) {
        ArrayList<Message> res = groupMessages.getOrDefault(group, new ArrayList<>());
        res.sort(Comparator.comparing(Message::getSchedule));
        return res;
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
