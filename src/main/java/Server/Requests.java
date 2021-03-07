package Server;

import Server.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Requests {
    private static final Logger logger = LogManager.getLogger(User.class);

    public static boolean login(String username, String password) {
        try {
            User u = User.getFilter().getUsername(username);
            logger.debug("Login request for target user : " + u.getJSON());
            if (u == null)
                throw new Exception("Username does not exist.");
            if (u.checkPassword(password))
                return true;
            else
                throw new Exception("Password is incorrect.");
        }
        catch (Exception e) {}
        return false;
    }
}
