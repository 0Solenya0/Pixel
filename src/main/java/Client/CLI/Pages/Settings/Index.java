package Client.CLI.Pages.Settings;

import Client.CLI.ConsoleColors;
import Client.CLI.UserUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintStream;
import java.util.Scanner;

public class Index {
    private static final Logger logger = LogManager.getLogger(Client.CLI.Pages.Profile.Index.class);

    public static boolean show() {
        UserUtility.updateStatus();
        while (true) {
            System.out.println(ConsoleColors.PURPLE + "\t---Settings---");
            System.out.print(ConsoleColors.YELLOW);
            System.out.println("(p) Privacy");
            System.out.println("(d) Delete account");
            System.out.println("(l) Logout");
            System.out.println("(b) back");

            String response = UserUtility.scanner.nextLine();
            switch (response) {
                case "p":
                    Privacy.show();
                    break;
                case "d":
                    System.out.println(ConsoleColors.RED + "ARE YOU SURE? (y/n)");
                    response = UserUtility.scanner.nextLine();
                    if (response.equals("y")) {
                        try {
                            UserUtility.user.deleteUserDependencies();
                            UserUtility.user.delete();
                        } catch (Exception e) {
                            logger.error("User deletion failed");
                        }
                    }
                    return false;
                case "l":
                    return false;
                case "b":
                    return true ;
            }
        }
    }
}
