package Client.CLI.Pages.Profile;

import Client.CLI.ConsoleColors;
import Client.CLI.UserUtility;
import Server.models.Exceptions.ConnectionException;
import Server.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProfileInfo {
    private static final Logger logger = LogManager.getLogger(ProfileInfo.class);

    public static void main(String username) {
        boolean isOwner = UserUtility.user.username.equals(username);
        logger.info("User opened " + username + "'s profile info");
        if (!isOwner) {
            try {
                if (!User.getFilter().getByUsername(username).isEnabled) {
                    System.out.println(ConsoleColors.RED + "User account is disabled");
                    return;
                }
            } catch (ConnectionException e) {
                logger.error(e.getMessage());
                System.out.println("Loading page failed - " + e.getMessage());
            }
        }
        DateTimeFormatter formatLastseen = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        DateTimeFormatter formatBirthday = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            User user = User.getFilter().getByUsername(username);
            System.out.println(ConsoleColors.PURPLE + "\t---" + user.name + "'s Profile---");
            System.out.print(ConsoleColors.YELLOW);
            System.out.println("Username: " + user.username);
            System.out.println("Full name: " + user.name + " " + user.surname);
            if (user.getBirthdate().get() != null && !user.getBirthdate().get().equals(LocalDate.MIN)
                    && UserUtility.checkAccess(user, user.getBirthdate().accessLevel))
                        System.out.println("Birthday: " + user.getBirthdate().get().format(formatBirthday));
            if (user.getPhone().get() != null && !user.getPhone().get().isEmpty()
                    && UserUtility.checkAccess(user, user.getPhone().accessLevel))
                        System.out.println("Phone number: " + user.getPhone().get());
            if (user.getMail().get() != null
                    && UserUtility.checkAccess(user, user.getMail().accessLevel))
                        System.out.println("Email: " + user.getMail().get());
            System.out.println("Biography: \n\t" + ConsoleColors.YELLOW_BOLD + user.bio);
            if (UserUtility.checkAccess(user, user.getLastSeen().accessLevel))
                System.out.println("Last seen: " + user.getLastSeen().get().format(formatLastseen));
            else
                System.out.println("Last seen recently");
            System.out.println(ConsoleColors.YELLOW + "(b) back");
            String response = UserUtility.scanner.nextLine();
        }
        catch (ConnectionException e) {
            logger.error("User profile info wasn't loaded - " + e.getMessage());
            System.out.println(ConsoleColors.RED + "Loading profile failed - " + e.getMessage());
        }
    }
}
