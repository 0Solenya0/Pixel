package Client.CLI.Pages;

import Client.CLI.ConsoleColors;
import Client.RequestSender;
import Server.Validators;
import Server.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class Main {
    private static final Logger logger = LogManager.getLogger(User.class);

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println(ConsoleColors.YELLOW + "Already have an account? (y/n)");
            String response = scanner.next();
            if (response.equals("y"))
                login();
            else if (response.equals("n"))
                register();
            else
                System.out.println(ConsoleColors.RED + "Please enter valid response.");
        }
    }

    public static void login() {
        logger.info("directed to login page.");
        while (true) {
            System.out.println(ConsoleColors.YELLOW + "Enter your username:");
            String username = scanner.next();
            System.out.println(ConsoleColors.YELLOW + "Enter your password:");
            String password = scanner.next();
            try {
                RequestSender.config(username, password);
                RequestSender.login();
                System.out.println(ConsoleColors.GREEN + "Logged in successfully.");
                break;
            }
            catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Login failed - " + e.getMessage());
            }
        }
    }

    public static void register() {
        logger.info("directed to registration page.");
        while (true) {
            System.out.println(ConsoleColors.YELLOW + "Enter your full name in separate lines:");
            String name = scanner.next();
            String surname = scanner.next();
            System.out.println(ConsoleColors.YELLOW + "Enter username:");
            String username = scanner.next();
            String mail;
            while (true) {
                System.out.println(ConsoleColors.YELLOW + "Enter email:");
                mail = scanner.next();
                if (!Validators.isValidMail(mail))
                    System.out.println(ConsoleColors.RED + "Email is not valid.");
                else
                    break;
            }
            System.out.println(ConsoleColors.YELLOW + "Enter password:");
            String password = scanner.next();
            try {
                RequestSender.register(username, password, mail, name, surname);
                System.out.println(ConsoleColors.GREEN + "User have been registered.");
                break;
            }
            catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Registration failed - " + e.getMessage());
            }
        }
    }
}
