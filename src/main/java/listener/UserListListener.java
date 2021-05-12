package listener;

import model.User;

import java.util.ArrayList;

public interface UserListListener {
    void listen(ArrayList<User> users);
}
