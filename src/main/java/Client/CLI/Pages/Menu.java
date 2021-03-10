package Client.CLI.Pages;

import Client.CLI.ConsoleColors;
import Client.CLI.Pages.Profile.Index;
import Client.CLI.UserUtility;

public class Menu {
    public static void show() {
        mainloop: while (true) {
            System.out.println(ConsoleColors.PURPLE + "\t---Main Menu---");
            System.out.print(ConsoleColors.YELLOW);
            System.out.println("(p) Personal page");
            System.out.println("(t) Timeline");
            System.out.println("(e) Explorer");
            System.out.println("(m) Messages");
            System.out.println("(s) Settings");
            System.out.println("(exit) Logout");

            String response = UserUtility.scanner.nextLine();
            switch (response) {
                case "exit":
                    break mainloop;
                case "p":
                    (new Client.CLI.Pages.Profile.Index(UserUtility.user.username)).show();
                    break;
                case "e":
                    Client.CLI.Pages.Explorer.Index.show();
                    break;
            }
        }
    }
}
