package Client.CLI.Pages.TimeLine;

import Client.CLI.ConsoleColors;
import Client.CLI.Pages.Forward;
import Client.CLI.Pages.Profile.ProfileInfo;
import Client.CLI.Pages.Tweets;
import Client.CLI.UserUtility;
import Server.models.Exceptions.ConnectionException;
import Server.models.Exceptions.ValidationException;
import Server.models.Message;
import Server.models.Tweet;
import Server.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;

public class Index {
    private static final Logger logger = LogManager.getLogger(Index.class);

    public static ArrayList<Tweet> getTweets() throws ConnectionException {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (User u: UserUtility.user.getFollowings()) {
            tweets.addAll(Tweet.getFilter().getByUser(u.username).getByParentTweet(0).getList());
        }
        tweets.sort(Comparator.comparingInt(t -> -t.id));
        return tweets;
    }

    public static void show() {
        UserUtility.updateStatus();
        try {
            showTweetList(getTweets(), "Timeline");
        }
        catch (ConnectionException e) {
            System.out.println(ConsoleColors.RED + "Failed fetching tweets");
            logger.error("Failed loading tweets - " + e.getMessage());
            return;
        }
    }

    public static void showTweetList(ArrayList<Tweet> tweets, String title) throws ConnectionException {
        int cur = 0;
        while (true) {
            System.out.println(ConsoleColors.PURPLE + "\t\t---" + title + "---");
            if (tweets.isEmpty()) {
                System.out.println(ConsoleColors.RED + "There are no tweets here :(");
                return;
            }
            Tweets.showTweet(tweets.get(cur), true);
            if (tweets.get(cur).likes.contains(UserUtility.user.id))
                System.out.print(ConsoleColors.YELLOW + "(l)" + ConsoleColors.RED + " ❤ " + ConsoleColors.YELLOW + " unLike");
            else
                System.out.print(ConsoleColors.YELLOW + "(l)" + ConsoleColors.RED + " ♡" + ConsoleColors.YELLOW + " Like");
            System.out.println(ConsoleColors.RED + " (" + tweets.get(cur).likes.size() + ")" + ConsoleColors.YELLOW);

            System.out.println("(p) Go to user's personal page");
            System.out.println("(n) Add comment");
            System.out.println("(c) Comments");
            System.out.println("(r) Retweet");
            System.out.println("(f) Forward");
            System.out.println("(s) Save to saved messages");
            if (UserUtility.user.id != tweets.get(cur).getAuthorId())
                System.out.println("(block) Block author");
            System.out.println("(b) back");
            if (cur > 0)
                System.out.print("(-) ← ");
            if (cur + 1 < tweets.size())
                System.out.print("→ (+)");
            System.out.println();

            String response = UserUtility.scanner.nextLine();
            switch (response) {
                case "s":
                    Message t = new Message(tweets.get(cur).id);
                    try {
                        UserUtility.user.sendMessage(UserUtility.user.id, t);
                        System.out.println(ConsoleColors.GREEN + "Tweet saved successfully");
                    }
                    catch (ValidationException e) {
                        logger.error("Failed to save a message " + " - tweet id " + tweets.get(cur).id + " user " + UserUtility.user.id + " - " + e.getMessage());
                        System.out.println(ConsoleColors.RED + "Failed to save the message - " + e.getMessage());
                    }
                    break;
                case "f":
                    Message m = new Message(tweets.get(cur).id);
                    (new Forward(m)).show();
                    break;
                case "l":
                    if (tweets.get(cur).likes.contains(UserUtility.user.id))
                        UserUtility.user.disLike(tweets.get(cur).id);
                    else
                        UserUtility.user.like(tweets.get(cur).id);
                    tweets.set(cur, Tweet.get(tweets.get(cur).id));
                    break;
                case "block":
                    if (UserUtility.user.id != tweets.get(cur).getAuthorId()) {
                        UserUtility.user.block(tweets.get(cur).id);
                        int del = 0;
                        for (int i = 0; i <= cur; i++) {
                            if (tweets.get(i).id == tweets.get(cur).id)
                                del += 1;
                        }
                        cur -= del;
                        cur = Math.max(0, cur);
                    }
                    return;
                case "r":
                    Tweets.postTweet(0, tweets.get(cur).id);
                    break;
                case "c":
                    try {
                        int tarId = tweets.get(cur).id;
                        showTweetList(Tweet.getFilter().getByParentTweet(tarId).getEnabled().getList(), "Comment Section");
                    }
                    catch (Exception e) {
                        System.out.println(ConsoleColors.RED + "Failed loading comments");
                        logger.error("Failed loading comments - " + e.getMessage());
                    }
                    break;
                case "n":
                    Tweets.postTweet(tweets.get(cur).id, 0);
                    break;
                case "b":
                    return;
                case "-":
                    if (cur > 0)
                        cur -= 1;
                    break;
                case "+":
                    if (cur + 1 < tweets.size())
                        cur += 1;
                    break;
                case "p":
                    try {
                        (new Client.CLI.Pages.Profile.Index(tweets.get(cur).getAuthor().username)).show();
                    }
                    catch (Exception e) {
                        System.out.println(ConsoleColors.RED + "Failed to load user page");
                        logger.error("Failed loading user personal page - " + e.getMessage());
                    }
                    break;
            }
        }
    }
}
