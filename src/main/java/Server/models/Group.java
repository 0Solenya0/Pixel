package Server.models;

import Server.models.Exceptions.ConnectionException;
import Server.models.Exceptions.ValidationException;
import Server.models.Filters.GroupFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.TreeSet;

public class Group extends Model {
    private static final Logger logger = LogManager.getLogger(Group.class);

    public String name;
    private TreeSet<Integer> users;
    private int owner;

    public int getOwner() {
        return owner;
    }

    public ArrayList<User> getUsers() throws ConnectionException {
        ArrayList<User> res = new ArrayList<>();
        for (int u: users) {
            if (User.get(u).isEnabled())
                res.add(User.get(u));
        }
        return res;
    }

    public Group(int owner, String name) {
        this.name = name;
        this.owner = owner;
        this.users = new TreeSet<>();
    }
    public void addUser(int user) throws ValidationException, ConnectionException {
        users.add(user);
        save();
    }
    public void deleteUser(int user) throws ValidationException, ConnectionException {
        users.remove(user);
        save();
    }

    public static GroupFilter getFilter() throws ConnectionException {
        return new GroupFilter();
    }

    public static Group get(int id) throws ConnectionException {
        return loadObj(id, Group.class);
    }
    public void isValid() throws ValidationException {
        if (users.contains(owner)) {
            logger.debug("validation error owner can't be in his own group");
            throw new ValidationException("Users", "Group", "Group owner can't be added to it");
        }
    }
}