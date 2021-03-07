package Client.CLI.Pages;

import Client.CLI.ConsoleColors;
import Client.RequestSender;
import Server.models.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Profile {
    public static void main(String username) {
        User user = RequestSender.getProfile(username);
        System.out.println(ConsoleColors.PURPLE + '\t' + user.name + "'s Profile");
        System.out.print(ConsoleColors.YELLOW);
        System.out.println("Username: " + user.username);
        System.out.println("Full name: " + user.name + " " + user.surname);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd");
        if (user.birthdate.get() != null && !user.birthdate.get().equals(LocalDate.MIN))
            System.out.println("Birthday: " + user.birthdate.get().format(formatter));
        if (user.phone.get() != null && !user.phone.get().isEmpty())
            System.out.println("Phone number: " + user.phone.get());
        if (user.mail.get() != null)
            System.out.println("Email: " + user.mail.get());
        System.out.println("Biography: \n\t" + user.bio);

        System.out.println("Type b and press enter to go back");
        String response = Main.scanner.next();
    }
}
