package Client.CLI;

import Server.models.User;

import java.util.Scanner;

public class UserUtility {
    public static Scanner scanner = new Scanner(System.in);
    public static User user;

    public static void login(String username, String password) throws Exception {
        if (User.getFilter().getByUsername(username).checkPassword(password)) {
            user = User.getFilter().getByUsername(username);
            return;
        }
        throw new Exception("Password is wrong");
    }
}
