package model;

import java.util.ArrayList;
import java.util.TreeSet;

public class ChatGroup extends Model {

    private String name;
    private TreeSet<Integer> users = new TreeSet<>();
    private TreeSet<Integer> admins = new TreeSet<>();
    private int owner;

    public ChatGroup(String name, int owner) {
        this.name = name;
        this.owner = owner;
        users.add(owner);
    }

    public String getName() {
        return name;
    }

    public TreeSet<Integer> getUsers() {
        return users;
    }

    public TreeSet<Integer> getAdmin() {
        return admins;
    }

    public int getOwner() {
        return owner;
    }

    public boolean isMember(int user) {
        return users.contains(user);
    }

    public boolean isAdmin(User user) {
        return users.contains(user.id);
    }

    public void addMember(User user) {
        users.add(user.id);
    }

    public void addAdmin(User user) {
        admins.add(user.id);
    }
}
