package Client.CLI.Pages.Settings;

import Client.CLI.ConsoleColors;
import Client.CLI.UserUtility;
import Server.models.Exceptions.ConnectionException;
import Server.models.Fields.AccessLevel;
import Server.models.Fields.NotificationType;
import Server.models.Notification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

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
            switch (UserUtility.user.getMail().getAccessLevel()) {
                case PUBLIC:
                    System.out.println("(c) Toggle mail visibility " + ConsoleColors.GREEN_BOLD + "PUBLIC" + ConsoleColors.YELLOW + "/PRIVATE/CONTACTS");
                    break;
                case PRIVATE:
                    System.out.println("(c) Toggle mail visibility PUBLIC/" + ConsoleColors.GREEN_BOLD + "PRIVATE" + ConsoleColors.YELLOW + "/CONTACTS");
                    break;
                case CONTACTS:
                    System.out.println("(c) Toggle mail visibility PUBLIC/PRIVATE" + ConsoleColors.GREEN_BOLD + "/CONTACTS");
                    break;
            }
            System.out.print(ConsoleColors.YELLOW);
            switch (UserUtility.user.getPhone().getAccessLevel()) {
                case PUBLIC:
                    System.out.println("(d) Toggle phone number visibility " + ConsoleColors.GREEN_BOLD + "PUBLIC" + ConsoleColors.YELLOW + "/PRIVATE/CONTACTS");
                    break;
                case PRIVATE:
                    System.out.println("(d) Toggle phone number visibility PUBLIC/" + ConsoleColors.GREEN_BOLD + "PRIVATE" + ConsoleColors.YELLOW + "/CONTACTS");
                    break;
                case CONTACTS:
                    System.out.println("(d) Toggle phone number visibility PUBLIC/PRIVATE" + ConsoleColors.GREEN_BOLD + "/CONTACTS");
                    break;
            }
            System.out.print(ConsoleColors.YELLOW);
            if (UserUtility.user.isEnabled())
                System.out.println("(e) Disable account");
            else
                System.out.println("(e) Enable account");
            System.out.println("(g) Change password");
            System.out.println("(back)");

            String response = UserUtility.scanner.nextLine();
            switch (response) {
                case "a":
                    toggleVisibility();
                    break;
                case "b":
                    toggleLastseen();
                    break;
                case "c":
                    toggleMail();
                    break;
                case "d":
                    togglePhone();
                    break;
                case "e":
                    try {
                        UserUtility.user.setEnabled(!UserUtility.user.isEnabled());
                        UserUtility.user.save();
                    }
                    catch (Exception e) {
                        logger.error("Cannot disable/enable account - userId: " + UserUtility.user.id + " - " + e.getMessage());
                        System.out.println(ConsoleColors.RED + "Disabling account failed");
                    }
                    break;
                case "g":
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
                case "back":
                    return;
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
    public static void toggleMail() {
        switch (UserUtility.user.getMail().getAccessLevel()) {
            case PUBLIC:
                UserUtility.user.getMail().setAccessLevel(AccessLevel.PRIVATE);
                break;
            case PRIVATE:
                UserUtility.user.getMail().setAccessLevel(AccessLevel.CONTACTS);
                break;
            case CONTACTS:
                UserUtility.user.getMail().setAccessLevel(AccessLevel.PUBLIC);
                break;
        }
        try {
            UserUtility.user.save();
        }
        catch (Exception e) {
            logger.error("Can't save changes to mail - " + e.getMessage());
            System.out.println(ConsoleColors.RED + "Change failed");
        }
    }
    public static void togglePhone() {
        switch (UserUtility.user.getPhone().getAccessLevel()) {
            case PUBLIC:
                UserUtility.user.getPhone().setAccessLevel(AccessLevel.PRIVATE);
                break;
            case PRIVATE:
                UserUtility.user.getPhone().setAccessLevel(AccessLevel.CONTACTS);
                break;
            case CONTACTS:
                UserUtility.user.getPhone().setAccessLevel(AccessLevel.PUBLIC);
                break;
        }
        try {
            UserUtility.user.save();
        }
        catch (Exception e) {
            logger.error("Can't save changes to phone - " + e.getMessage());
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
                try {
                    ArrayList<Notification> notifs = Notification.getFilter().
                            getByUser2(UserUtility.user.id)
                            .getByType(NotificationType.REQUEST)
                            .getList();
                    for (Notification n: notifs)
                        n.accept();
                }
                catch (ConnectionException e) {

                }
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
