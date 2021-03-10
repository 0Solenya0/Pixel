package Client.CLI.Pages;

import Client.CLI.ConsoleColors;
import Client.CLI.Pages.Profile.Index;
import Client.CLI.UserUtility;

public class Menu {
    public static void show() {
        while (true) {
            System.out.println(ConsoleColors.PURPLE + "\t---Main Menu---");
            System.out.print(ConsoleColors.YELLOW);
            System.out.println("a. Personal page");
            System.out.println("b. Timeline");
            System.out.println("d. Explorer");
            System.out.println("e. Messages");
            System.out.println("f. Settings");

            String response = UserUtility.scanner.nextLine();
            switch (response) {
                case "a":
                    (new Client.CLI.Pages.Profile.Index(UserUtility.user.username)).show();
                    break;
                case "d":
                    Client.CLI.Pages.Explorer.Index.show();
                    break;
            }
        }
    }
}
