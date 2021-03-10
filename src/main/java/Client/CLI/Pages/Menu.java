package Client.CLI.Pages;

import Client.CLI.ConsoleColors;
import Client.CLI.Pages.ProfilePage.Index;
import Client.CLI.UserUtility;

import java.util.Scanner;

public class Menu {
    public static void show() {
        while (true) {
            System.out.println(ConsoleColors.PURPLE + "\t---Main Menu---");
            System.out.print(ConsoleColors.YELLOW);
            System.out.println("a. Personal page");

            String response = UserUtility.scanner.nextLine();
            switch (response) {
                case "a":
                    (new Index(UserUtility.user.username)).show();
                    break;
            }
        }
    }
}
