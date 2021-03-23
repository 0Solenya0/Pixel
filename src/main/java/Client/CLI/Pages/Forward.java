package Client.CLI.Pages;

import Client.CLI.ConsoleColors;
import Client.CLI.UserUtility;
import Server.models.Exceptions.ConnectionException;
import Server.models.Message;
import Server.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class Forward {
    private static final Logger logger = LogManager.getLogger(Forward.class);

    Message targetMessage;

    public Forward(Message m) {
        targetMessage = m;
    }

    public void show() throws ConnectionException {
        System.out.println(ConsoleColors.PURPLE + "\t---Forward Page---");
        for (User u : UserUtility.user.getFollowings())
            System.out.println(ConsoleColors.BLUE + u.username);
        System.out.println(ConsoleColors.YELLOW + "Enter the usernames you want to forward to");
        System.out.println("then press ^ in a new line");

        ArrayList<User> list = new ArrayList<>();
        while (true) {
            String line = UserUtility.scanner.nextLine();
            if (line.equals("^"))
                break;
            if (User.getFilter().getByUsername(line) == null)
                System.out.println(ConsoleColors.RED + "This user does not exist");
            else if (!UserUtility.user.canMessage(User.getFilter().getByUsername(line).id))
                System.out.println(ConsoleColors.RED + "You can't message this user");
            else {
                list.add(User.getFilter().getByUsername(line));
                System.out.println(ConsoleColors.GREEN + "Added to forward list");
            }
        }
        for (User user : list) {
            try {
                Message m;
                if (targetMessage.tweetId == 0)
                    m = new Message(targetMessage.getContent());
                else
                    m = new Message(targetMessage.tweetId);
                UserUtility.user.sendMessage(user.id, m);
                System.out.println(ConsoleColors.GREEN + "Message sent to " + user.username + " successfully");
            }
            catch (Exception e) {
                logger.warn("Message cannot be sent " + user.id + " - " + e.getMessage());
                System.out.println(ConsoleColors.RED + "Message couldn't be sent to " + user.username);
            }
        }
    }
}
