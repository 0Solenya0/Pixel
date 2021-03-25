package Client.CLI.Pages.Messages;

import Client.CLI.ConsoleColors;
import Client.CLI.UserUtility;
import Server.models.Exceptions.ConnectionException;
import Server.models.Exceptions.ValidationException;
import Server.models.Message;
import Server.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Mac;
import java.util.ArrayList;

public class DirectMessage {
    private static final Logger logger = LogManager.getLogger(DirectMessage.class);

    private static final int onePageMessages = 10;
    private int targetUser;
    public DirectMessage(int user) {
        targetUser = user;
    }
    ArrayList<Message> messages = new ArrayList<>();

    public void show() {
        int l = 0;
        while (true) {
            try {
                messages.clear();
                System.out.println(ConsoleColors.PURPLE + "\t---" + User.get(targetUser).username + "'s Messages---");
                messages.addAll(Message.getFilter().getByTwoUser(UserUtility.user.id, targetUser).getList());
                if (UserUtility.user.id != targetUser)
                    messages.addAll(Message.getFilter().getByTwoUser(targetUser, UserUtility.user.id).getList());
                for (Message message : Message.getFilter().getByTwoUser(targetUser, UserUtility.user.id).getList())
                    message.see();

                l = Math.max(Math.min(messages.size() - onePageMessages, l), 0);
                int r = Math.min(l + onePageMessages, messages.size());
                messages.sort((m1, m2) -> -(m1.id - m2.id));
                for (int i = r - 1; i >= l; i--) {
                    if (i < r - 1 && messages.get(i).getSender() != messages.get(i + 1).getSender())
                        System.out.println();
                    showSingleMessage(messages.get(i));
                }
                System.out.println();
                System.out.print(ConsoleColors.YELLOW);
                if (l + onePageMessages < messages.size())
                    System.out.println("(o) Older messages");
                if (l - onePageMessages >= 0)
                    System.out.println("(n) Newer messages");
                if (UserUtility.user.canMessage(targetUser))
                    System.out.println("(s) Send message");
                else
                    System.out.println(ConsoleColors.RED + "You can't message this user");
                System.out.println(ConsoleColors.YELLOW + "(b) back");
                String response = UserUtility.scanner.nextLine();
                switch (response) {
                    case "o":
                        l += 10;
                        break;
                    case "n":
                        l -= 10;
                        break;
                    case "s":
                        sendMessage();
                        break;
                    case "b":
                        return;
                }
            } catch (ConnectionException e) {
                e.showError();
            } catch (ValidationException e) {
                logger.warn("Invalid message was sent - " + e.getMessage());
                e.showError();
            }
        }
    }

    public void sendMessage() throws ConnectionException, ValidationException {
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
        UserUtility.user.sendMessage(targetUser, m);
        System.out.println(ConsoleColors.GREEN_BOLD + "Message sent successfully!");
    }

    public void showSingleMessage(Message m) throws ConnectionException {
        if (m.getSender() == UserUtility.user.id) {
            System.out.println(ConsoleColors.CYAN + "ME: ");
            System.out.println(ConsoleColors.YELLOW + m.getContent());
        }
        else {
            System.out.println(ConsoleColors.GREEN + User.get(targetUser).username + ": ");
            System.out.println(ConsoleColors.YELLOW + m.getContent());
        }
    }
}
