package Client.CLI.Pages.Settings;

import Client.CLI.ConsoleColors;
import Client.CLI.UserUtility;
import Server.models.Fields.AccessLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Privacy {
    private static final Logger logger = LogManager.getLogger(Privacy.class);

    public static void show() {
        while (true) {
            System.out.println(ConsoleColors.PURPLE + "\t---Privacy settings---");
            System.out.print(ConsoleColors.YELLOW);
            switch (UserUtility.user.visibility) {
                case PUBLIC:
                    System.out.println("(a) Toggle page visibility " + ConsoleColors.GREEN_BOLD + "PUBLIC" + ConsoleColors.YELLOW + "/PRIVATE");
                    break;
                case PRIVATE:
                    System.out.println("(a) Toggle page visibility PUBLIC/" + ConsoleColors.GREEN_BOLD + "PRIVATE");
                    break;
            }
            System.out.print(ConsoleColors.YELLOW);
            switch (UserUtility.user.getLastSeen().getAccessLevel()) {
                case PUBLIC:
                    System.out.println("(b) Toggle last seen visibility " + ConsoleColors.GREEN_BOLD + "PUBLIC" + ConsoleColors.YELLOW + "/PRIVATE/CONTACTS");
                    break;
                case PRIVATE:
                    System.out.println("(b) Toggle last seen visibility PUBLIC/" + ConsoleColors.GREEN_BOLD + "PRIVATE" + ConsoleColors.YELLOW + "/CONTACTS");
                    break;
                case CONTACTS:
                    System.out.println("(b) Toggle last seen visibility PUBLIC/PRIVATE" + ConsoleColors.GREEN_BOLD + "/CONTACTS");
                    break;
            }
            System.out.print(ConsoleColors.YELLOW);
            if (UserUtility.user.isEnabled)
                System.out.println("(c) Disable account");
            else
                System.out.println("(c) Enable account");
            System.out.println("(d) Change password");

            String response = UserUtility.scanner.nextLine();
            switch (response) {
                case "a":
                    toggleVisibility();
                    break;
                case "b":
                    toggleLastseen();
                    break;
                case "c":
                    try {
                        UserUtility.user.isEnabled = !UserUtility.user.isEnabled;
                        UserUtility.user.save();
                    }
                    catch (Exception e) {
                        logger.error("Cannot disable/enable account - userId: " + UserUtility.user.id + " - " + e.getMessage());
                        System.out.println(ConsoleColors.RED + "Disabling account failed");
                    }
                    break;
                case "d":
                    try {
                        System.out.println("Enter your new password:");
                        String pass = UserUtility.scanner.nextLine();
                        UserUtility.user.changePassword(pass);
                    }
                    catch (Exception e) {
                        logger.error("Couldn't change password - userId: " + UserUtility.user.id + " - " + e.getMessage());
                        System.out.println(ConsoleColors.RED + "Changing password failed");
                    }
                    break;
            }

        }
    }
    public static void toggleLastseen() {
        switch (UserUtility.user.getLastSeen().getAccessLevel()) {
            case PUBLIC:
                    UserUtility.user.getLastSeen().setAccessLevel(AccessLevel.PRIVATE);
                break;
            case PRIVATE:
                UserUtility.user.getLastSeen().setAccessLevel(AccessLevel.CONTACTS);
                break;
            case CONTACTS:
                UserUtility.user.getLastSeen().setAccessLevel(AccessLevel.PUBLIC);
                break;
        }
        try {
            UserUtility.user.save();
        }
        catch (Exception e) {
            logger.error("Can't save changes to lastseen - " + e.getMessage());
            System.out.println(ConsoleColors.RED + "Change failed");
        }
    }
    public static void toggleVisibility() {
        switch (UserUtility.user.visibility) {
            case PUBLIC:
                UserUtility.user.visibility = AccessLevel.PRIVATE;
                break;
            case PRIVATE:
                UserUtility.user.visibility = AccessLevel.PUBLIC;
                break;
        }
        try {
            UserUtility.user.save();
        }
        catch (Exception e) {
            logger.error("Can't save changes to lastseen - " + e.getMessage());
            System.out.println(ConsoleColors.RED + "Change failed");
        }
    }
}
