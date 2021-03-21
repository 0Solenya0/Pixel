package Client.CLI;

import Client.CLI.Exceptions.LoginException;
import Server.models.Exceptions.ConnectionException;
import Server.models.User;

import java.util.Scanner;

public class UserUtility {
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
}
