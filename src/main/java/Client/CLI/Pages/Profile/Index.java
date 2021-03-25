package Client.CLI.Pages.Profile;

import Client.CLI.ConsoleColors;
import Client.CLI.Pages.Lists;
import Client.CLI.Pages.Messages.DirectMessage;
import Client.CLI.Pages.Tweets;
import Client.CLI.UserUtility;
import Server.models.Exceptions.ConnectionException;
import Server.models.Exceptions.ValidationException;
import Server.models.Fields.NotificationType;
import Server.models.Fields.RelStatus;
import Server.models.Fields.RelType;
import Server.models.Notification;
import Server.models.Relation;
import Server.models.Tweet;
import Server.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class Index {
    private static final Logger logger = LogManager.getLogger(Index.class);

    public String username;

    public Index(String username) {
        UserUtility.updateStatus();
        this.username = username;
    }

    public boolean unblock() {
        System.out.println(ConsoleColors.BLUE + "(!) Unblock");
        String response = UserUtility.scanner.nextLine();
        if (response.equals("!")) {
            try {
                UserUtility.user.resetRel(User.getFilter().getByUsername(username).id);
            }
            catch (ConnectionException e) {
                e.showError();
                return false;
            }
            return true;
        }
        else
            return false;
    }

    public void show() {
        boolean isOwner = UserUtility.user.username.equals(username);
        logger.info("User opened " + username + "'s personal page");
        if (!isOwner) {
            try {
                if (!User.getFilter().getByUsername(username).isEnabled()) {
                    System.out.println(ConsoleColors.RED + "User account is disabled");
                    return;
                }
            } catch (ConnectionException e) {
                logger.error(e.getMessage());
                System.out.println("Loading page failed - " + e.getMessage());
            }
        }
        while (true) {
            System.out.println(ConsoleColors.PURPLE + "\t---" + username + "'s Personal Page---");
            RelStatus rel = RelStatus.NORELATION;
            RelStatus relr = RelStatus.NORELATION;
            /** Follow status **/
            if (!isOwner) {
                try {
                    rel = UserUtility.user.getRelationStatus(User.getFilter().getByUsername(username).id);
                    relr = User.getFilter().getByUsername(username).getRelationStatus(UserUtility.user.id);

                    if (relr == RelStatus.BLOCKED) {
                        System.out.println(ConsoleColors.RED + "User has blocked you");
                        if (rel != RelStatus.BLOCKED) {
                            System.out.println(ConsoleColors.BLUE + "(!) Block");
                            if (UserUtility.scanner.nextLine().equals("!"))
                                UserUtility.user.block(User.getFilter().getByUsername(username).id);
                            else
                                return;
                        }
                        else
                            unblock();
                        return;
                    }
                    if (rel == RelStatus.NORELATION)
                        System.out.println(ConsoleColors.BLUE + "(1) Follow (2) Block");
                    else if (rel == RelStatus.FOLLOW)
                        System.out.println(ConsoleColors.BLUE + "(1) Unfollow (2) Block");
                    else if (rel == RelStatus.BLOCKED) {
                        System.out.println(ConsoleColors.RED + "You have blocked this user");
                        if (unblock())
                            continue;
                        else
                            return;
                    }
                    else if (rel == RelStatus.REQUESTED)
                        System.out.println(ConsoleColors.BLUE + "(1) Cancel Follow Request");
                }
                catch (ConnectionException e) {
                    logger.error("Failed to load relation status - " + e.getMessage());
                    System.out.println("Failed to load page - " + e.getMessage());
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
            if (!isOwner && (rel == RelStatus.FOLLOW || relr == RelStatus.FOLLOW))
                System.out.println("(m) Message");
            if (!isOwner) {
                try {
                    if (!UserUtility.user.isMuted(User.getFilter().getByUsername(username).id))
                        System.out.println("(mute) Mute user");
                    else
                        System.out.println("(unmute) Unmute user");
                }
                catch (Exception e) {}
            }
            if (!isOwner)
                System.out.println("(report) Report user");
            System.out.println("(b) back");
            System.out.print(ConsoleColors.RESET);

            String response = UserUtility.scanner.nextLine();
            switch (response) {
                case "unmute":
                    try {
                        UserUtility.user.unMuteUser(User.getFilter().getByUsername(username).id);
                    }
                    catch (ConnectionException e) {
                        logger.error("request has failed - " + e.getMessage());
                        System.out.println(ConsoleColors.RED + "Request Failed");
                    }
                    break;
                case "mute":
                    try {
                        UserUtility.user.muteUser(User.getFilter().getByUsername(username).id);
                    }
                    catch (ConnectionException e) {
                        logger.error("request has failed - " + e.getMessage());
                        System.out.println(ConsoleColors.RED + "Request Failed");
                    }
                    break;
                case "m":
                    try {
                        (new DirectMessage(User.getFilter().getByUsername(username).id)).show();
                    }
                    catch (ConnectionException e) {
                        logger.error("Failed loading page - " + e.getMessage());
                        System.out.println("Loading page failed - " + e.getMessage());
                    }
                    break;
                case "n":
                    Client.CLI.Pages.Notifications.Index.show();
                    break;
                case "b":
                    return;
                case "report":
                    try {
                        UserUtility.user.reportUser(User.getFilter().getByUsername(username).id);
                    }
                    catch (Exception e) {

                    }
                    break;
                case "l":
                    if (isOwner) {
                        try {
                            Lists.showBlackList(User.getFilter().getByUsername(username).id);
                        } catch (ConnectionException e) {
                            logger.error("Failed loading lists - " + e.getMessage());
                            System.out.println(ConsoleColors.RED + "Failed to load page");
                        }
                    }
                    break;
                case "y":
                    try {
                        Lists.showFollowing(User.getFilter().getByUsername(username).id);
                    }
                    catch (ConnectionException e) {
                        logger.error("Failed loading lists - " + e.getMessage());
                        System.out.println(ConsoleColors.RED + "Failed to load page");
                    }
                    break;
                case "z":
                    try {
                        Lists.showFollowers(User.getFilter().getByUsername(username).id);
                    }
                    catch (ConnectionException e) {
                        logger.error("Failed loading lists - " + e.getMessage());
                        System.out.println(ConsoleColors.RED + "Failed to load page");
                    }
                    break;
                case "1":
                    try {
                        if (!isOwner) {
                            int x = User.getFilter().getByUsername(username).id;
                            switch (UserUtility.user.getRelationStatus(x)) {
                                case FOLLOW:
                                    UserUtility.user.resetRel(x);
                                    break;
                                case NORELATION:
                                    UserUtility.user.follow(x);
                                    break;
                                case REQUESTED:
                                    Notification.getFilter().getByType(NotificationType.REQUEST)
                                            .getByTwoUser(UserUtility.user.id, x).delete();
                                    break;
                            }
                        }
                    }
                    catch (ConnectionException e) {
                        logger.error("New relation request has failed - " + e.getMessage());
                        System.out.println(ConsoleColors.RED + "Request Failed");
                    }
                    break;
                case "2":
                    try {
                        if (!isOwner)
                            UserUtility.user.block(User.getFilter().getByUsername(username).id);
                    }
                    catch (ConnectionException e) {
                        logger.error("New relation request has failed - " + e.getMessage());
                        System.out.println(ConsoleColors.RED + "Request Failed");
                    }
                    break;
                case "i":
                    ProfileInfo.main(username);
                    break;
                case "c":
                    if (isOwner)
                        Tweets.postTweet(0, 0);
                    break;
                case "p":
                    try {
                        ArrayList<Tweet> tmp = Tweet.getFilter().getByUser(username).getList();
                        tmp.sort(Comparator.comparingInt(t -> -t.id));
                        Client.CLI.Pages.TimeLine.Index.showTweetList(
                                tmp,
                                username + "'s Tweets");
                    }
                    catch (ConnectionException e) {
                        System.out.println(ConsoleColors.RED + "Failed loading user tweets");
                        logger.error("Failed loading user tweets - " + e.getMessage());
                    }
                    break;
                case "e":
                    if (isOwner)
                        EditProfile.show();
                    break;
            }
        }
    }
}
