package Client.CLI.Pages.Groups;

import Client.CLI.ConsoleColors;
import Client.CLI.UserUtility;
import Server.models.Exceptions.ConnectionException;
import Server.models.Exceptions.InvalidRequestException;
import Server.models.Exceptions.ValidationException;
import Server.models.Group;
import Server.models.Message;
import Server.models.User;

import java.util.ArrayList;

public class Index {

    public static void showUserGroupsList() {
        try {
            for (Group u : UserUtility.user.getGroups()) {
                try {
                    System.out.println(ConsoleColors.BLUE_BOLD + u.name);
                } catch (Exception e) {
                }
            }
            if (UserUtility.user.getGroups().isEmpty())
                System.out.println(ConsoleColors.RED + "You have no group");
        }
        catch (ConnectionException e) {
            e.showError();
        }
    }

    public static void show() {
        while (true) {
            System.out.println(ConsoleColors.PURPLE + "\t---Groups---");
            System.out.println(ConsoleColors.YELLOW + "(n) Create a new group");
            System.out.println("(s) Send message");
            System.out.println("(m) Manage groups");
            System.out.println("(b) back");

            String response = UserUtility.scanner.nextLine();
            switch (response) {
                case "n":
                    createNewGroup();
                    break;
                case "s":
                    sendGroupMessage();
                    break;
                case "m":
                    manageGroup();
                    break;
                case "b":
                    return;
            }
        }
    }

    public static void createNewGroup() {
        System.out.println(ConsoleColors.PURPLE + "\t---New group---");
        System.out.println("Enter group name:");
        String name = UserUtility.scanner.nextLine();
        try {
            UserUtility.user.makeNewGroup(name);
        }
        catch (ConnectionException e) {
            e.showError();
            return;
        }
        catch (ValidationException e) {
            e.showError();
            return;
        }
        System.out.println(ConsoleColors.GREEN_BOLD + "Group created successfully");
    }

    public static void sendGroupMessage() {
        System.out.println(ConsoleColors.PURPLE + "\t---Send Group Message---");
        showUserGroupsList();
        System.out.println("Enter group name:");
        String response = UserUtility.scanner.nextLine();
        try {
            Group g = UserUtility.user.getGroup(response);
            System.out.println(ConsoleColors.PURPLE + "Type ^ in a new line and press enter to save");
            System.out.println(ConsoleColors.PURPLE + "Type ~ in a new line and press enter to go back");
            System.out.println(ConsoleColors.PURPLE + "Write your message:");
            StringBuilder content = new StringBuilder();
            while (true) {
                String line = UserUtility.scanner.nextLine();
                if (line.equals("^"))
                    break;
                if (line.equals("~"))
                    return;
                content.append(line + "\n");
            }
            Message m = new Message(content.toString().trim());
            UserUtility.user.sendGroupMessage(g.id, m);
        }
        catch (ConnectionException e) {
            e.showError();
        }
        catch (ValidationException e) {
            e.showError();
        }
        catch (InvalidRequestException e) {
            e.showError();
        }
    }

    public static void showGroupDetail(Group g) {
        System.out.println(ConsoleColors.YELLOW + "Members:");
        try {
            if (g.getUsers().isEmpty()) {
                System.out.println(ConsoleColors.RED + "Group has no members");
            }
            for (User u: g.getUsers()) {
                    System.out.println(ConsoleColors.BLUE + u.username + " - " + u.getFullName());
                }
        }
        catch (ConnectionException e) {
            e.showError();
        }
    }

    public static void manageGroup() {
        System.out.println(ConsoleColors.PURPLE + "\t---Manage Groups---");
        showUserGroupsList();
        System.out.println(ConsoleColors.YELLOW + "Enter group name:");
        String response = UserUtility.scanner.nextLine();
        try {
            Group g = UserUtility.user.getGroup(response);
            mainLoop: while (true) {
                showGroupDetail(g);
                System.out.print(ConsoleColors.YELLOW);
                System.out.println("(a) Add new user");
                System.out.println("(d) Delete user");
                System.out.println("(dg) Delete group");
                System.out.println("(b) back");
                String res = UserUtility.scanner.nextLine();
                switch (res) {
                    case "dg":
                        g.delete();
                        break mainLoop;
                    case "a":
                        System.out.println("Enter username:");
                        response = UserUtility.scanner.nextLine();
                        g.addUser(User.getFilter().getByUsername(response).id);
                        break;
                    case "d":
                        System.out.println("Enter username:");
                        response = UserUtility.scanner.nextLine();
                        g.deleteUser(User.getFilter().getByUsername(response).id);
                        break;
                    case "b":
                        return;
                }
            }
        }
        catch (InvalidRequestException | ConnectionException | ValidationException e) {
            System.out.println(e.getMessage());
        }
    }
}
