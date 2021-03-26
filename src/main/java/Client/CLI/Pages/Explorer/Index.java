package Client.CLI.Pages.Explorer;

import Client.CLI.ConsoleColors;
import Client.CLI.UserUtility;
import Server.models.Exceptions.ConnectionException;
import Server.models.Fields.AccessLevel;
import Server.models.Tweet;

import java.util.ArrayList;
import java.util.Comparator;

public class Index {

    public static void show() {
        UserUtility.updateStatus();
        mainloop: while (true) {
            System.out.println(ConsoleColors.PURPLE + "\t---Explorer Page---");
            System.out.print(ConsoleColors.YELLOW);
            System.out.println("(f) Find friends!");
            System.out.println("(e) Explore trending tweets");
            System.out.println("(b) back");

            String response = UserUtility.scanner.nextLine();
            switch (response) {
                case "e":
                    try {
                        ArrayList<Tweet> t = Tweet.getFilter().getEnabled().customFilter(tweet -> {
                            if (tweet.getAuthorId() == UserUtility.user.id)
                                return false;
                            try {
                                return tweet.getAuthor().visibility == AccessLevel.PUBLIC;
                            } catch (ConnectionException e) {
                                return false;
                            }
                        }).getList();
                        t.sort(Comparator.comparingInt(tweet -> -tweet.getLikeCount()));
                        Client.CLI.Pages.TimeLine.Index.showTweetList(t, "Explore Trending Tweets");
                    }
                    catch (ConnectionException e) {
                        e.showError();
                    }
                    break;
                case "f":
                    SearchUser.main();
                    break;
                case "b":
                    break mainloop;
            }
        }
    }
}
