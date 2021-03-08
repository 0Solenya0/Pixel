package Client.CLI.Pages;

import Client.CLI.ConsoleColors;
import Client.RequestSender;
import Server.models.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Profile {
    public static void main(String username) {
        Scanner scanner = new Scanner(System.in);
        User user = RequestSender.getProfile(username);
        System.out.println(ConsoleColors.PURPLE + '\t' + user.name + "'s Profile");
        System.out.print(ConsoleColors.YELLOW);
        System.out.println("Username: " + user.username);
        System.out.println("Full name: " + user.name + " " + user.surname);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd");
        if (user.getBirthdate().get() != null && !user.getBirthdate().get().equals(LocalDate.MIN))
            System.out.println("Birthday: " + user.getBirthdate().get().format(formatter));
        if (user.getPhone().get() != null && !user.getPhone().get().isEmpty())
            System.out.println("Phone number: " + user.getPhone().get());
        if (user.getMail().get() != null)
            System.out.println("Email: " + user.getMail().get());
        System.out.println("Biography: \n\t" + user.bio);

        System.out.println("Type b and press enter to go back");
        String response = scanner.next();
    }
}
