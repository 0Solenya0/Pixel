package Client.CLI;

import Server.models.User;

import java.io.IOException;
import java.util.Scanner;

public class UserUtility {
    public static Scanner scanner = new Scanner(System.in);
    public static User user;

    public static void login(String username, String password) throws Exception {
        if (User.getFilter().getUsername(username).checkPassword(password)) {
            user = User.getFilter().getUsername(username);
            return;
        }
        throw new Exception("Password is wrong");
    }
}
