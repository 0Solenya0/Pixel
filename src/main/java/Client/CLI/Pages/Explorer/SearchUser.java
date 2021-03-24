package Client.CLI.Pages.Explorer;

import Client.CLI.ConsoleColors;
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
            System.out.println(ConsoleColors.BLUE + "(~) back");
            System.out.println(ConsoleColors.YELLOW + "Enter username:");

            String response = UserUtility.scanner.nextLine();
            if (response.equals("~"))
                break;
            try {
                ArrayList<User> list = User.getFilter().getEnabled().getByUsernamePrefix(response).getList();
                for (int i = 0; i < Math.min(list.size(), 10); i++) {
                    System.out.println(ConsoleColors.BLUE + (i + 1) + ". " + list.get(i).username + " - " + list.get(i).getFullName());
                }
                System.out.println(ConsoleColors.YELLOW + "Enter row number to navigate to user's profile");
                System.out.println("(b) back");
                while (true) {
                    response = UserUtility.scanner.nextLine();
                    if (response.equals("b"))
                        break;
                    try {
                        int num = Integer.parseInt(response);
                        if (num > list.size() || num > 10)
                            throw new Exception("");
                        (new Client.CLI.Pages.Profile.Index(list.get(num - 1).username)).show();
                        break;
                    }
                    catch (Exception e) {
                        System.out.println(ConsoleColors.RED + "Please enter valid response");
                    }
                }
            }
            catch (Exception e) {
                logger.warn("Failed to load users - " + e.getMessage());
                System.out.println("Fetching users failed");
            }
        }
    }
}
