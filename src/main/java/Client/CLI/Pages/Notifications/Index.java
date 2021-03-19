package Client.CLI.Pages.Notifications;

import Client.CLI.ConsoleColors;
import Client.CLI.Pages.Lists;
import Client.CLI.UserUtility;
import Server.models.Fields.NotificationType;
import Server.models.Filters.NotificationFilter;
import Server.models.Notification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.WrongMethodTypeException;
import java.util.ArrayList;
import java.util.Scanner;

public class Index {
    private static final Logger logger = LogManager.getLogger(Index.class);


    public static void show() {
        while (true) {
            System.out.println(ConsoleColors.PURPLE + "\t---Notifications---");
            System.out.println(ConsoleColors.YELLOW + "(a) Follow requests");
            System.out.println("(r) Your requests");
            System.out.println("(s) System Notifications");
            System.out.println("(b) back");

            String response = UserUtility.scanner.nextLine();
            switch (response) {
                case "b":
                    return;
                case "a":
                    showFollowReq();
                    break;
                case "r":
                    showPendingReq();
                    break;
                case "s":
                    showSystemNotif();
                    break;
            }
        }
    }

    public static void showFollowReq() {
        while (true) {
            System.out.println(ConsoleColors.PURPLE + "\t---Follow Requests---");
            ArrayList<Notification> list;
            try {
                list = Notification.getFilter().userCustomFilter(notification ->
                                notification.user2 == UserUtility.user.id && notification.type == NotificationType.REQUEST).getList();

            }
            catch (Exception e) {
                logger.error("Loading follow requests failed - " + e.getMessage());
                System.out.println(ConsoleColors.RED + "Failed loading requests");
                return;
            }
            if (list.isEmpty()) {
                System.out.println(ConsoleColors.RED + "There are no follow requests for you");
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                try {
                    System.out.println(ConsoleColors.BLUE + (i + 1) + ". " + list.get(i).getMessage());
                }
                catch (Exception e) {
                    System.out.println(ConsoleColors.RED + "Failed to load");
                }
            }
            System.out.println(ConsoleColors.YELLOW + "Choose notification row number and enter a response(+/-/*)");
            System.out.println(ConsoleColors.GREEN + "(+) Accept request");
            System.out.println(ConsoleColors.YELLOW + "(-) Refuse request - Don't notify the user");
            System.out.println(ConsoleColors.RED + "(-) Refuse request - Notify the user");
            System.out.println(ConsoleColors.YELLOW + "(b) back");

            String response = UserUtility.scanner.nextLine();
            if (response.equals("b"))
                return;
            int id;
            String pos;
            try {
                Scanner scanner = new Scanner(response);
                id = scanner.nextInt();
                pos = scanner.next();
                if (id > list.size())
                    throw new Exception("Index out of range");
            } catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Please enter a valid response");
                continue;
            }
            id--;
            try {
                switch (pos) {
                    case "+":
                        list.get(id).accept();
                        break;
                    case "-":
                        list.get(id).refuse();
                    case "*":
                        list.get(id).silentRefuse();
                        break;
                }
            }
            catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Error while saving the request");
                logger.error("Notification response wasn't saved - " + e.getMessage());
            }
        }
    }
    public static void showPendingReq() {
        while (true) {
            System.out.println(ConsoleColors.PURPLE + "\t---Pending Requests---");
            ArrayList<Notification> list;
            try {
                list = Notification.getFilter().userCustomFilter(notification ->
                        notification.user1 == UserUtility.user.id && notification.type == NotificationType.REQUEST).getList();
            }
            catch (Exception e) {
                logger.error("Loading follow requests failed - " + e.getMessage());
                System.out.println(ConsoleColors.RED + "Failed loading requests");
                return;
            }
            if (list.isEmpty()) {
                System.out.println(ConsoleColors.RED + "There are no follow requests for you");
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                try {
                    System.out.println(ConsoleColors.BLUE + list.get(i).getMessageForSender());
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(ConsoleColors.RED + "Failed to load");
                }
            }
            System.out.println(ConsoleColors.YELLOW + "(b) back");

            String response = UserUtility.scanner.nextLine();
            if (response.equals("b"))
                return;
        }
    }
    public static void showSystemNotif() {
        while (true) {
            System.out.println(ConsoleColors.PURPLE + "\t---Pending Requests---");
            ArrayList<Notification> list;
            try {
                list = Notification.getFilter().userCustomFilter(notification ->
                        notification.user2 == UserUtility.user.id &&
                                notification.type == NotificationType.INFO &&
                                notification.user1 == 0
                ).getList();
            }
            catch (Exception e) {
                logger.error("Loading notifications failed - " + e.getMessage());
                System.out.println(ConsoleColors.RED + "Failed loading notifications");
                return;
            }
            if (list.isEmpty()) {
                System.out.println(ConsoleColors.RED + "There are no notifications here");
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                try {
                    System.out.println(ConsoleColors.BLUE + list.get(i).getMessageForSender());
                }
                catch (Exception e) {
                    System.out.println(ConsoleColors.RED + "Failed to load");
                }
            }
            System.out.println(ConsoleColors.YELLOW + "(b) back");

            String response = UserUtility.scanner.nextLine();
            if (response.equals("b"))
                return;
        }
    }

}