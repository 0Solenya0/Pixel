package Client.CLI;

import Client.CLI.Exceptions.LoginException;
import Client.CLI.Pages.Lists;
import Server.models.Exceptions.ConnectionException;
import Server.models.Fields.AccessLevel;
import Server.models.Fields.RelType;
import Server.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class UserUtility {
    private static final Logger logger = LogManager.getLogger(UserUtility.class);

    public static Scanner scanner = new Scanner(System.in);
    public static User user;

    public static void login(String username, String password) throws LoginException, ConnectionException {
        if (User.getFilter().getByUsername(username) == null)
            throw new LoginException("User does not exist");
        if (User.getFilter().getByUsername(username).checkPassword(password)) {
            user = User.getFilter().getByUsername(username);
            return;
        }
        throw new LoginException("Password is wrong");
    }

    public static boolean checkAccess(User u, AccessLevel accessLevel) {
        if (u.id == user.id)
            return true;
        try {
            if (accessLevel == AccessLevel.PUBLIC)
                return true;
            if (accessLevel == AccessLevel.PRIVATE)
                return false;
            RelType t = user.getRel(u.id).getType();
            if (accessLevel == AccessLevel.CONTACTS && t == RelType.FOLLOW)
                return true;
            return false;
        }
        catch (Exception e) {
            logger.error("Checking access failed - " + e.getMessage());
            return false;
        }
    }

    public static void updateStatus() {
        try {
            user.updateLastSeen();
        }
        catch (Exception e) { }
    }
}
