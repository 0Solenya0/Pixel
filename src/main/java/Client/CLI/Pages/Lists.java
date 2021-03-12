package Client.CLI.Pages;

import Client.CLI.ConsoleColors;
import Client.CLI.UserUtility;
import Server.models.Tweet;
import Server.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Lists {
    private static final Logger logger = LogManager.getLogger(Lists.class);

    public static void showFollowers(int userid) throws Exception {
        User target = User.get(userid);
        System.out.println(ConsoleColors.PURPLE + "\t---" + target.username + "'s Followers---");
        showUserList(target.getFollowers());
    }
    public static void showFollowing(int userid) throws Exception {
        User target = User.get(userid);
        System.out.println(ConsoleColors.PURPLE + "\t---" + target.username + "'s Followings---");
        showUserList(target.getFollowings());
    }
    public static void showBlackList(int userid) throws Exception {
        User target = User.get(userid);
        System.out.println(ConsoleColors.PURPLE + "\t---" + target.username + "'s Blacklist---");
        showUserList(target.getBlackList());
    }

    public static void showUserList(ArrayList<User> list) {
        while (true) {
            System.out.print(ConsoleColors.BLUE);
            for (int i = 0; i < list.size(); i++)
                System.out.println((i + 1) + ". " + list.get(i).username + " - " + list.get(i).getFullName());
            if (list.size() > 0)
                System.out.println(ConsoleColors.YELLOW + "Enter user's row number to enter profile");
            else
                System.out.println(ConsoleColors.RED + "List is empty :(");
            System.out.println(ConsoleColors.YELLOW + "(b) back");
            String response = UserUtility.scanner.nextLine();
            if (response.equals("b"))
                return;
            try {
                int x = Integer.parseInt(response);
                (new Client.CLI.Pages.Profile.Index(list.get(x - 1).username)).show();
                return;
            } catch (Exception e) {
                logger.warn("List page response wasn't valid - " + e.getMessage());
                System.out.println("Please enter a valid response");
            }
        }
    }
}
