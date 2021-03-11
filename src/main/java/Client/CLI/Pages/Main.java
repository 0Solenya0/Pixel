package Client.CLI.Pages;

import Client.CLI.ConsoleColors;
import Client.CLI.UserUtility;
import Server.Validators;
import Server.models.Relation;
import Server.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        while (true) {
            System.out.println(ConsoleColors.YELLOW + "Already have an account? (y/n)");
            String response = UserUtility.scanner.nextLine();
            if (response.equals("y"))
                login();
            else if (response.equals("n"))
                register();
            else
                System.out.println(ConsoleColors.RED + "Please enter valid response.");
        }
    }

    public static void login() {
        logger.info("directed to login page");
        System.out.println(ConsoleColors.PURPLE + "\t---Login Page---");
        while (true) {
            System.out.println(ConsoleColors.YELLOW + "Enter your username:");
            String username = UserUtility.scanner.nextLine();
            System.out.println(ConsoleColors.YELLOW + "Enter your password:");
            String password = UserUtility.scanner.nextLine();
            try {
                if (username.isEmpty() || password.isEmpty())
                    break;
                UserUtility.login(username, password);
                System.out.println(ConsoleColors.GREEN + "Logged in successfully");
                Menu.show();
                break;
            }
            catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Login failed please try again - " + e.getMessage());
            }
        }
    }
    public static void register() {
        logger.info("directed to registration page");
        System.out.println(ConsoleColors.PURPLE + "\t\t---Registration Page---");
        while (true) {
            System.out.println(ConsoleColors.YELLOW + "Enter your name and surname in separate lines:");
            String name = UserUtility.scanner.nextLine();
            String surname = UserUtility.scanner.nextLine();
            System.out.println(ConsoleColors.YELLOW + "Enter username:");
            String username = UserUtility.scanner.nextLine();
            String mail;
            while (true) {
                System.out.println(ConsoleColors.YELLOW + "Enter email:");
                mail = UserUtility.scanner.nextLine();
                if (!Validators.isValidMail(mail))
                    System.out.println(ConsoleColors.RED + "Email is not valid");
                else
                    break;
            }
            System.out.println(ConsoleColors.YELLOW + "Enter password:");
            String password = UserUtility.scanner.nextLine();
            try {
                User user = new User(name, surname, username, mail, password);
                user.save();
                System.out.println(ConsoleColors.GREEN + "User have been registered");
                break;
            }
            catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Registration failed please try again - " + e.getMessage());
            }
        }
    }
}
