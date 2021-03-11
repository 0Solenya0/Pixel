package Client.CLI.Pages.Profile;

import Client.CLI.ConsoleColors;
import Client.CLI.Pages.Lists;
import Client.CLI.Pages.Tweets;
import Client.CLI.UserUtility;
import Server.models.Fields.RelType;
import Server.models.Relation;
import Server.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Index {
    private static final Logger logger = LogManager.getLogger(Index.class);

    public String username;

    public Index(String username) {
        this.username = username;
    }

    public boolean unblock() {
        System.out.println(ConsoleColors.BLUE + "(!) Unblock");
        String response = UserUtility.scanner.nextLine();
        if (response.equals("!")) {
            try {
                UserUtility.user.resetRel(User.getFilter().getByUsername(username).id);
            }
            catch (Exception e) {
                logger.error("New relation request failed - " + e.getMessage());
                return false;
            }
            return true;
        }
        else
            return false;
    }

    public void show() {
        boolean isOwner = UserUtility.user.username.equals(username);
        while (true) {
            System.out.println(ConsoleColors.PURPLE + "\t---" + username + "'s Personal Page---");
            /** Follow status **/
            if (!isOwner) {
                try {
                    Relation rel = UserUtility.user.getRel(User.getFilter().getByUsername(username).id);
                    Relation relr = User.getFilter().getByUsername(username).getRel(UserUtility.user.id);
                    if (relr != null && relr.type == RelType.BLOCKED) {
                        System.out.println(ConsoleColors.RED + "User has blocked you");
                        if (rel == null || rel.type != RelType.BLOCKED) {
                            System.out.println(ConsoleColors.BLUE + "(!) Block");
                            if (UserUtility.scanner.nextLine().equals("!"))
                                UserUtility.user.block(User.getFilter().getByUsername(username).id);
                        }
                        else
                            unblock();
                        return;
                    }
                    if (rel == null)
                        System.out.println(ConsoleColors.BLUE + "(1) Follow (2) Block");
                    else if (rel.type == RelType.FOLLOW)
                        System.out.println(ConsoleColors.BLUE + "(1) Unfollow (2) Block");
                    else if (rel.type == RelType.BLOCKED) {
                        System.out.println(ConsoleColors.RED + "You have blocked this user");
                        if (unblock())
                            continue;
                        else
                            return;
                    }
                }
                catch (Exception e) {
                    logger.error("Failed to load relation status - " + e.getMessage());
                    e.printStackTrace();
                }
            }

            System.out.print(ConsoleColors.YELLOW);
            if (isOwner)
                System.out.println("(c) New tweet!");
            System.out.println("(p) Past tweets");
            if (isOwner)
                System.out.println("(e) Edit profile");
            System.out.println("(z) Followers");
            System.out.println("(y) Following");
            if (isOwner)
                System.out.println("(l) Blacklist");
            System.out.println("(i) Profile info");
            if (isOwner)
                System.out.println("(n) Notifications");
            System.out.println("(b) back");
            System.out.print(ConsoleColors.RESET);

            String response = UserUtility.scanner.nextLine();
            switch (response) {
                case "b":
                    return;
                case "l":
                    if (isOwner) {
                        try {
                            Lists.showBlackList(User.getFilter().getByUsername(username).id);
                        } catch (Exception e) {
                            logger.error("Failed loading lists - " + e.getMessage());
                            System.out.println(ConsoleColors.RED + "Failed to load page");
                        }
                    }
                    break;
                case "y":
                    try {
                        Lists.showFollowing(User.getFilter().getByUsername(username).id);
                    }
                    catch (Exception e) {
                        logger.error("Failed loading lists - " + e.getMessage());
                        System.out.println(ConsoleColors.RED + "Failed to load page");
                    }
                    break;
                case "z":
                    try {
                        Lists.showFollowers(User.getFilter().getByUsername(username).id);
                    }
                    catch (Exception e) {
                        logger.error("Failed loading lists - " + e.getMessage());
                        System.out.println(ConsoleColors.RED + "Failed to load page");
                    }
                    break;
                case "1":
                    try {
                        if (!isOwner) {
                            int x = User.getFilter().getByUsername(username).id;
                            if (UserUtility.user.getRel(x) == null)
                                UserUtility.user.follow(x);
                            else
                                UserUtility.user.resetRel(x);
                        }
                    }
                    catch (Exception e) {
                        logger.error("New relation request has failed - " + e.getMessage());
                        System.out.println(ConsoleColors.RED + "Request Failed");
                    }
                    break;
                case "2":
                    try {
                        if (!isOwner)
                            UserUtility.user.block(User.getFilter().getByUsername(username).id);
                    }
                    catch (Exception e) {
                        logger.error("New relation request has failed - " + e.getMessage());
                        System.out.println(ConsoleColors.RED + "Request Failed");
                    }
                    break;
                case "i":
                    ProfileInfo.main(UserUtility.user.username);
                    break;
                case "c":
                    if (isOwner)
                        Tweets.postTweet();
                    break;
                case "p":
                    Tweets.showUserTweets(username);
                    break;
                case "e":
                    if (isOwner)
                        EditProfile.show();
                    break;
            }
        }
    }
}
