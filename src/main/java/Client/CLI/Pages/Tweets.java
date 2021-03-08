package Client.CLI.Pages;

import Client.CLI.ConsoleColors;

import java.util.ArrayList;
import java.util.Scanner;

public class Tweets {
    public void postTweet() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ConsoleColors.PURPLE + "Type ^ in a new line and press enter to save");
        System.out.println(ConsoleColors.PURPLE + "Type ~ in a new line and press enter to go back");
        System.out.println(ConsoleColors.PURPLE + "\t" + "Write your tweet content");
        StringBuilder content = new StringBuilder();
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("^"))
                break;
            if (line.equals("~"))
                return;
            content.append(content + "\n");
        }

    }
}
