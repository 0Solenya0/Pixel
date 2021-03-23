package Client.CLI.Pages.Messages;

import Client.CLI.ConsoleColors;
import Client.CLI.UserUtility;
import Server.models.Exceptions.ConnectionException;
import Server.models.Message;
import Server.models.User;

import java.util.ArrayList;
import java.util.TreeSet;

public class Index {
    public static void show() {
        UserUtility.updateStatus();
        ArrayList<Integer> users = new ArrayList<>();
        TreeSet<Integer> unseenUsers = new TreeSet<>();
        while (true) {
            users.clear();
            unseenUsers.clear();
            System.out.println(ConsoleColors.PURPLE + "\t---Messages---");
            try {
                ArrayList<Message> list = Message.getFilter().getBySeen(false).getByUser2(UserUtility.user.id).getList();
                list.sort((m1, m2) -> {
                    if (m1.user1 < m2.user2)
                        return -1;
                    if (m1.user1 == m2.user2)
                        return m1.id - m2.id;
                    return 1;
                });
                if (!list.isEmpty()) {
                    MessageGroup m = new MessageGroup(list.get(0).user1, new ArrayList<>());
                    for (int i = 0; i < list.size(); i++) {
                        if (i != 0 && list.get(i).user1 != list.get(i - 1).user1) {
                            if (m.user != UserUtility.user.id) {
                                users.add(m.user);
                                showUnseenMessageGroup(m, users.size());
                                unseenUsers.add(list.get(i).user1);
                            }
                            m = new MessageGroup(list.get(i).user1, new ArrayList<>());
                        }
                        m.messages.add(list.get(i));
                    }
                    users.add(m.user);
                    unseenUsers.add(m.user);
                    showUnseenMessageGroup(m, users.size());
                }
                TreeSet<Integer> tmp = new TreeSet<>();
                list = Message.getFilter().getRelatedToUser(UserUtility.user.id).getList();
                for (int i = list.size() - 1; i >= 0; i--) {
                    if (tmp.contains(list.get(i).user1) || unseenUsers.contains(list.get(i).user1))
                        continue;
                    if (list.get(i).user1 != UserUtility.user.id) {
                        users.add(list.get(i).user1);
                        tmp.add(list.get(i).user1);
                        showMessageGroup(list.get(i).user1, users.size());
                    }
                }
            } catch (ConnectionException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }

            if (!users.isEmpty())
                System.out.println(ConsoleColors.YELLOW + "Enter row number to enter chat");
            else
                System.out.println(ConsoleColors.RED + "There are no message here :(");
            System.out.println("(s) Saved Messages");
            System.out.println(ConsoleColors.YELLOW + "(b) back");
            String response = UserUtility.scanner.nextLine();
            switch (response) {
                case "s":
                    (new DirectMessage(UserUtility.user.id)).show();
                    break;
                case "b":
                    return;
                default:
                    try {
                        int row = Integer.parseInt(response);
                        row--;
                        if (row >= users.size())
                            throw new Exception("row doesn't exist");
                        (new DirectMessage(users.get(row))).show();
                    } catch (Exception e) {
                        System.out.println(ConsoleColors.RED + "Enter valid response");
                    }
                    break;
            }
        }
    }

    public static void showUnseenMessageGroup(MessageGroup messageGroup, int index) throws ConnectionException {
        System.out.print(ConsoleColors.BLUE_BOLD + index + ". " + User.get(messageGroup.user).username);
        System.out.println(ConsoleColors.RED + " (" + messageGroup.messages.size() + ")");
        System.out.print(ConsoleColors.YELLOW);
    }
    public static void showMessageGroup(int user, int index) throws ConnectionException {
        System.out.println(ConsoleColors.BLUE + index + ". " + User.get(user).username);
        System.out.print(ConsoleColors.YELLOW);
    }
}

class MessageGroup {
    public int user;
    ArrayList<Message> messages;

    public MessageGroup(int user, ArrayList<Message> messages) {
        this.user = user;
        this.messages = messages;
    }
}
