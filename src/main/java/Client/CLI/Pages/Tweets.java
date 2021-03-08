package Client.CLI.Pages;

import Client.CLI.ConsoleColors;
import Client.RequestSender;
import Server.models.Tweet;
import Server.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Scanner;

public class Tweets {
    private static final Logger logger = LogManager.getLogger(User.class);

    public static void postTweet() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ConsoleColors.PURPLE + "Type ^ in a new line and press enter to save");
        System.out.println(ConsoleColors.PURPLE + "Type ~ in a new line and press enter to go back");
        System.out.println(ConsoleColors.PURPLE + "\t" + "Write your tweet content");
        StringBuilder content = new StringBuilder();
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("^"))
                break;
            if (line.equals("~"))
                return;
            content.append(line + "\n");
        }
        try {
            Tweet tweet = RequestSender.postTweet(content.toString());
            System.out.println(ConsoleColors.GREEN + "Saved successfully.");
            logger.info("New tweet saved - " + tweet.getJSON());
        }
        catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Tweet failed - " + e.getMessage());
        }
    }
}
