package Client.CLI.Pages;

import Client.CLI.ConsoleColors;
import Client.CLI.Pages.ProfilePage.Index;
import Client.RequestSender;

import java.util.Scanner;

public class Menu {
    public static void show() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println(ConsoleColors.PURPLE + "\tMain Menu");
            System.out.print(ConsoleColors.YELLOW);
            System.out.println("a. Personal page");

            String response = scanner.next();
            switch (response) {
                case "a":
                    (new Index(RequestSender.getInstance().getUsername())).show();
                    break;
            }

        }
    }
}
