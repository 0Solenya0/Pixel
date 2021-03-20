package Server.models;

import Server.models.Exceptions.ConnectionException;
import Server.models.Exceptions.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class Group extends Model {
    private static final Logger logger = LogManager.getLogger(Group.class);

    public String name;
    ArrayList<Integer> users;
    int owner;

    public Group(int owner, String name) {
        this.name = name;
        this.owner = owner;
    }
    public void addUser(int user) throws ValidationException, ConnectionException {
        users.add(user);
        save();
    }

    public static Group get(int id) throws ConnectionException {
        return (Group) loadObj(id, Group.class);
    }
    public void isValid() throws ValidationException {
        for (int i = 0; i < users.size(); i++)
            if (users.get(i) == owner) {
                logger.debug("validation error owner can't be in his own group");
                throw new ValidationException("Users", "Group", "Group owner can't be added to it");
            }
    }
}