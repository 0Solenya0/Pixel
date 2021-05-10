package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TreeSet;

public class Group extends Model {
    private static final Logger logger = LogManager.getLogger(Group.class);

    public String name;
    private TreeSet<Integer> users;
    private int owner;

    public Group(int owner, String name) {
        this.name = name;
        this.owner = owner;
        this.users = new TreeSet<>();
    }

    public int getOwner() {
        return owner;
    }

    public TreeSet<Integer> getUsers() {
        return users;
    }

    public void addUser(int user) {
        users.add(user);
    }
    public void deleteUser(int user) {
        users.remove(user);
    }
}
