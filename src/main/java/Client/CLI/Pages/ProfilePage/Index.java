package Client.CLI.Pages.ProfilePage;

import Client.CLI.ConsoleColors;
import Client.CLI.Pages.Tweets;
import Client.RequestSender;
import Server.models.Tweet;

import java.util.Scanner;

public class Index {
    public String username;

    public Index(String username) {
        this.username = username;
    }

    public void show() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println(ConsoleColors.PURPLE + "\tPress desired key for target page.");
            System.out.print(ConsoleColors.YELLOW);
            boolean isOwner = RequestSender.getInstance().getUsername().equals(username);
            if (isOwner)
                System.out.println("a. New tweet!");
            System.out.println("b. Past tweets");
            System.out.println("c. Edit profile");
            System.out.println("d. Followers");
            System.out.println("e. Following");
            if (isOwner)
                System.out.println("f. Blacklist");
            System.out.println("g. Profile info");
            if (isOwner)
                System.out.println("h. Notifications");
            System.out.print(ConsoleColors.RESET);

            String response = scanner.next();
            switch (response) {
                case "g":
                    ProfileInfo.main(RequestSender.getInstance().getUsername());
                    break;
                case "a":
                    Tweets.postTweet();
                    break;
            }
        }
    }
}
