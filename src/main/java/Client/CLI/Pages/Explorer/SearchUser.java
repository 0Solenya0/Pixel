package Client.CLI.Pages.Explorer;

import Client.CLI.ConsoleColors;
import Client.CLI.Pages.Main;
import Client.CLI.UserUtility;
import Server.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class SearchUser {
    private static final Logger logger = LogManager.getLogger(SearchUser.class);

    public static void main() {
        while (true) {
            System.out.println(ConsoleColors.PURPLE + "\t---Explore Users---");
            System.out.println("(~) back");
            System.out.println(ConsoleColors.YELLOW + "Search username:");

            String response = UserUtility.scanner.nextLine();
            if (response == "~")
                break;
            try {
                ArrayList<User> list = User.getFilter().getUsernamePrefix(response).getList();
                for (int i = 0; i < Math.min(list.size(), 10); i++) {
                    System.out.println(ConsoleColors.BLUE + (i + 1) + ". " + list.get(i).username + " - " + list.get(i).getFullName());
                }
                System.out.println(ConsoleColors.YELLOW + "Enter user number to see profile");
                System.out.println("(0) back");
                while (true) {
                    response = UserUtility.scanner.nextLine();
                    if (response.equals("0"))
                        break;
                    try {
                        int num = Integer.parseInt(response);
                        if (num > list.size() || num > 10)
                            throw new Exception("");
                        (new Client.CLI.Pages.Profile.Index(list.get(num - 1).username)).show();
                    }
                    catch (Exception e) {
                        System.out.println(ConsoleColors.RED + "Please enter valid response");
                    }
                }
            }
            catch (Exception e) {
                logger.warn("Failed to load users");
                System.out.println("Fetching users failed");
            }
        }
    }
}
