package Client.CLI.Pages;

import Client.CLI.ConsoleColors;
import Client.CLI.Pages.Profile.Index;
import Client.CLI.UserUtility;

import java.sql.ClientInfoStatus;

public class Menu {
    public static void show() {
        mainloop: while (true) {
            System.out.println(ConsoleColors.PURPLE + "\t---Main Menu---");
            System.out.print(ConsoleColors.YELLOW);
            System.out.println("(p) Personal page");
            System.out.println("(t) Timeline");
            System.out.println("(e) Explorer");
            System.out.println("(m) Messages");
            System.out.println("(g) Groups");
            System.out.println("(s) Settings");

            String response = UserUtility.scanner.nextLine();
            switch (response) {
                case "g":
                    Client.CLI.Pages.Groups.Index.show();
                    break;
                case "m":
                    Client.CLI.Pages.Messages.Index.show();
                    break;
                case "t":
                    Client.CLI.Pages.TimeLine.Index.show();
                    break;
                case "p":
                    (new Client.CLI.Pages.Profile.Index(UserUtility.user.username)).show();
                    break;
                case "e":
                    Client.CLI.Pages.Explorer.Index.show();
                    break;
                case "s":
                    if (!Client.CLI.Pages.Settings.Index.show()) {
                        System.out.println(ConsoleColors.GREEN + "Logged out successfully");
                        return;
                    }
                    break;
            }
        }
    }
}
