package Client.CLI.Pages.ProfilePage;

import Client.CLI.ConsoleColors;
import Client.CLI.Pages.Tweets;
import Client.CLI.UserUtility;

public class Index {
    public String username;

    public Index(String username) {
        this.username = username;
    }

    public void show() {
        while (true) {
            System.out.println(ConsoleColors.PURPLE + "\t---" + username + "'s Personal Page---");
            System.out.print(ConsoleColors.YELLOW);
            boolean isOwner = UserUtility.user.username.equals(username);
            if (isOwner)
                System.out.println("a. New tweet!");
            System.out.println("b. Past tweets");
            if (isOwner)
                System.out.println("c. Edit profile");
            System.out.println("d. Followers");
            System.out.println("e. Following");
            if (isOwner)
                System.out.println("f. Blacklist");
            System.out.println("g. Profile info");
            if (isOwner)
                System.out.println("h. Notifications");
            System.out.print(ConsoleColors.RESET);

            String response = UserUtility.scanner.nextLine();
            switch (response) {
                case "g":
                    ProfileInfo.main(UserUtility.user.username);
                    break;
                case "a":
                    if (isOwner)
                        Tweets.postTweet();
                    break;
                case "b":
                    Tweets.showUserTweets(username);
                    break;
                case "c":
                    if (isOwner)
                        EditProfile.show();
                    break;
            }
        }
    }
}
