package Server;

import Server.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Requests {
    private static final Logger logger = LogManager.getLogger(User.class);

    public static boolean login(String username, String password) throws Exception {
        User u = User.getFilter().getUsername(username);
        if (u == null)
            throw new Exception("Username does not exist.");
        if (u.checkPassword(password)) {
            logger.info(String.format("Login successful. Username: %s", username));
            return true;
        }
        else
            throw new Exception("Password is incorrect.");
    }

    public static boolean register(String username, String password, String mail, String name, String surname) throws Exception{
        User user = new User(name, surname, username, mail, password);
        user.save();
        logger.info(String.format("new user created - %s", user.getJSON()));
        return true;
    }
}