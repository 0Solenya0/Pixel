package client.listeners;

import shared.models.User;

import java.util.ArrayList;

public interface UserListListener {
    void listen(ArrayList<User> users);
}
