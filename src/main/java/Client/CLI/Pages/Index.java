package Client.CLI.Pages;

import Client.CLI.ConsoleColors;

public class Index {
    public static void showMenu() {
        while (true) {
            System.out.println(ConsoleColors.PURPLE + "Press desired key for target page.");
            System.out.print(ConsoleColors.YELLOW);
            System.out.println("a. New tweet!");
            System.out.println("b. Past tweets");
            System.out.println("c. Edit profile");
            System.out.println("d. Followers");
            System.out.println("e. Following");
            System.out.println("f. Blacklist");
            System.out.println("g. Profile info");
            System.out.print(ConsoleColors.RESET);
        }
    }
}
