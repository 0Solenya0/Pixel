package Client.CLI.Pages;

import Client.CLI.ConsoleColors;
import Client.RequestSender;

import java.util.Scanner;

public class Index {
    public static void main() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println(ConsoleColors.PURPLE + "\tPress desired key for target page.");
            System.out.print(ConsoleColors.YELLOW);
            System.out.println("a. New tweet!");
            System.out.println("b. Past tweets");
            System.out.println("c. Edit profile");
            System.out.println("d. Followers");
            System.out.println("e. Following");
            System.out.println("f. Blacklist");
            System.out.println("g. Profile info");
            System.out.print(ConsoleColors.RESET);
            String response = scanner.next();
            switch (response) {
                case "g":
                    Profile.main(RequestSender.getInstance().getUsername());
                    break;
            }
        }
    }
}
