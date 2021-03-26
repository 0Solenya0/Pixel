package Client.CLI.Pages;

import Client.CLI.ConsoleColors;
import Client.CLI.UserUtility;
import Server.models.Tweet;
import Server.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Tweets {
    private static final Logger logger = LogManager.getLogger(User.class);

    public static void postTweet(int parent, int retweet) {
        System.out.println(ConsoleColors.PURPLE + "\t ---Post tweet---");
        System.out.println(ConsoleColors.PURPLE + "Type ^ in a new line and press enter to save");
        System.out.println(ConsoleColors.PURPLE + "Type ~ in a new line and press enter to go back");
        System.out.println(ConsoleColors.PURPLE + "Write your tweet:");
        StringBuilder content = new StringBuilder();
        while (true) {
            String line = UserUtility.scanner.nextLine();
            if (line.equals("^"))
                break;
            if (line.equals("~"))
                return;
            content.append(line + "\n");
        }
        try {
            Tweet tweet = new Tweet(UserUtility.user, content.toString().trim());
            tweet.setParentTweetId(parent);
            tweet.setReTweetId(retweet);
            tweet.save();
            System.out.println(ConsoleColors.GREEN + "Saved successfully");
            logger.info("New tweet saved - Id: " + tweet.id);
        }
        catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Tweet failed - " + e.getMessage());
        }
    }
    public static void showTweet(Tweet tweet, boolean showSender) {
        try {
            if (tweet.getParentTweetId() != 0)
                System.out.println(ConsoleColors.PURPLE + "Replied to " + Tweet.get(tweet.getParentTweetId()).getAuthor().username);
            if (showSender)
                System.out.println(ConsoleColors.PURPLE + "Sent by " + tweet.getAuthor().username + ":");
            if (showSender)
                System.out.println("\t" + ConsoleColors.BLUE_BOLD + tweet.getContent().trim().replace("\n", "\n\t"));
            else
                System.out.println(ConsoleColors.BLUE_BOLD + tweet.getContent().trim());
            DateTimeFormatter formatLastseen = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            System.out.println(ConsoleColors.CYAN + "Sent At " + tweet.createdAt.format(formatLastseen));
        }
        catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Loading tweet failed");
        }
    }
}
