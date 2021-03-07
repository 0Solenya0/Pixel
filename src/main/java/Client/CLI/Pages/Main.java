package Client.CLI.Pages;

import Client.CLI.ConsoleColors;
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
            if (response.equals("y")) {
                login();
                break;
            }
            else if (response.equals("n"))
                register();
            else
                System.out.println(ConsoleColors.RED + "Please enter valid response.");
        }
    }

    public static void login() {
        while (true) {
            System.out.println(ConsoleColors.YELLOW + "Enter your username:");
            String username = scanner.next();
            System.out.println(ConsoleColors.YELLOW + "Enter your password:");
            String password = scanner.next();
            if (Server.Requests.login(username, password))
                System.out.println(ConsoleColors.GREEN + "Logged in successfully.");
            else
                System.out.println(ConsoleColors.RED + "Username or password is wrong.");
        }
    }

    public static void register() {
        while (true) {
            String mail;
            while (true) {
                System.out.println(ConsoleColors.YELLOW + "Enter email:");
                mail = scanner.next();
                if (!Validators.isValidMail(mail))
                    System.out.println(ConsoleColors.RED + "Email is not valid.");
                else
                    break;
            }
            System.out.println(ConsoleColors.YELLOW + "Enter username:");
            String username = scanner.next();
            System.out.println(ConsoleColors.YELLOW + "Enter password:");
            String password = scanner.next();
            System.out.println(ConsoleColors.YELLOW + "Enter your name:");
            String name = scanner.next();
            System.out.println(ConsoleColors.YELLOW + "Enter your surname:");
            String surname = scanner.next();
            if (Server.Requests.register(username, password, mail, name, surname))
                break;
            else
                System.out.println(ConsoleColors.RED + "Registration failed.");
        }
    }
}
