package listener;

import model.ChatGroup;
import model.Group;
import model.User;

import java.util.ArrayList;

public interface ForwardListener {
    void listen(ArrayList<User> users, ArrayList<Group> groups, ArrayList<ChatGroup> chatGroups);
}
