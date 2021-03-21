package Client.CLI.Pages.Profile;

import Client.CLI.ConsoleColors;
import Client.CLI.UserUtility;
import Server.models.Exceptions.ConnectionException;
import Server.models.Exceptions.ValidationException;
import Server.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditProfile {
    private static final Logger logger = LogManager.getLogger(EditProfile.class);
    private static User user;

    public static void show() {
        logger.info("User opened edit profile page");
        mainloop: while (true) {
            user = UserUtility.user;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd");
            System.out.println(ConsoleColors.BLUE_BRIGHT);
            System.out.println("(a) Change name (" + user.name + ")");
            System.out.println("(b) Change surname (" + user.surname + ")");
            if (!user.getBirthdate().get().equals(LocalDate.MIN))
                System.out.println("(c) Change Birthday (" + user.getBirthdate().get().format(formatter) + ")");
            else
                System.out.println("(c) Add Birthdate");
            if (!user.getPhone().get().equals(""))
                System.out.println("(d) Change Phone Number (" + user.getPhone().get() + ")");
            else
                System.out.println("(d) Add Phone Number");
            System.out.println("(e) Change Email (" + user.getMail().get() + ")");
            System.out.println("(f) Change bio");
            System.out.println("(0) back");

            String response = Client.CLI.UserUtility.scanner.nextLine();
            switch (response) {
                case "0":
                    break mainloop;
                case "a":
                    changeName();
                    break;
                case "b":
                    changeSurname();
                    break;
                case "c":
                    changeBirthday();
                    break;
                case "d":
                    changePhonenumber();
                    break;
                case "e":
                    changeEmail();
                    break;
                case "f":
                    changeBio();
                    break;
            }
        }
    }

    private static void changeName() {
        System.out.println(ConsoleColors.YELLOW + "Enter your name:");
        user.name = Client.CLI.UserUtility.scanner.nextLine();
        try {
            user.save();
        }
        catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Edit failed - " + e.getMessage());
        }
    }
    private static void changeSurname() {
        System.out.println(ConsoleColors.YELLOW + "Enter your surname:");
        user.surname = Client.CLI.UserUtility.scanner.nextLine();
        try {
            user.save();
        }
        catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Edit failed - " + e.getMessage());
        }
    }
    private static void changeBirthday() {
        System.out.println(ConsoleColors.YELLOW + "Enter birthdate with YYYY-MM-DD format:");
        try {
            user.getBirthdate().set(LocalDate.parse(Client.CLI.UserUtility.scanner.nextLine()));
        }
        catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Date format is not valid");
        }
        try {
            user.save();
        }
        catch (ValidationException e) {
            System.out.println(ConsoleColors.RED + "Edit failed - " + e.getMessage());
        }
        catch (ConnectionException e) {
            logger.error("Saving edit profile changes failed for user " + UserUtility.user.id + " - " + e.getMessage());
            System.out.println(ConsoleColors.RED + e.getMessage());
        }
    }
    private static void changePhonenumber() {
        System.out.println(ConsoleColors.YELLOW + "Enter your phone number:");
        user.getPhone().set(Client.CLI.UserUtility.scanner.nextLine());
        try {
            user.save();
        }
        catch (ValidationException e) {
            System.out.println(ConsoleColors.RED + "Edit failed - " + e.getMessage());
        }
        catch (ConnectionException e) {
            logger.error("Saving edit profile changes failed for user " + UserUtility.user.id + " - " + e.getMessage());
            System.out.println(ConsoleColors.RED + e.getMessage());
        }
    }
    private static void changeEmail() {
        System.out.println(ConsoleColors.YELLOW + "Enter email:");
        user.getMail().set(Client.CLI.UserUtility.scanner.nextLine());
        try {
            user.save();
        }
        catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Edit failed - " + e.getMessage());
        }
    }
    private static void changeBio() {
        System.out.println(ConsoleColors.YELLOW + "Enter your bio:");
        user.bio = Client.CLI.UserUtility.scanner.nextLine();
        try {
            user.save();
        }
        catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Edit failed - " + e.getMessage());
        }
    }
}
