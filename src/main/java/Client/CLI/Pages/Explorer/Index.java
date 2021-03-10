package Client.CLI.Pages.Explorer;

import Client.CLI.ConsoleColors;
import Client.CLI.UserUtility;

public class Index {
    public static void show() {
        while (true) {
            System.out.println(ConsoleColors.PURPLE + "\t---Explorer Page---");
            System.out.println(ConsoleColors.YELLOW + "a. Search User");
            System.out.println("b. Explore tweets");
            System.out.println("(0) back");

            String response = UserUtility.scanner.nextLine();
            switch (response) {
                case "a":
                    SearchUser.main();
                    break;
            }
        }
    }
}
