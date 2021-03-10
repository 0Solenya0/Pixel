package Client.CLI.Pages.Explorer;

import Client.CLI.ConsoleColors;
import Client.CLI.UserUtility;

public class Index {

    public static void show() {
        mainloop: while (true) {
            System.out.println(ConsoleColors.PURPLE + "\t---Explorer Page---");
            System.out.print(ConsoleColors.YELLOW);
            System.out.println("(f) Find friends!");
            System.out.println("(e) Explore trending tweets");
            System.out.println("(b) back");

            String response = UserUtility.scanner.nextLine();
            switch (response) {
                case "f":
                    SearchUser.main();
                    break;
                case "b":
                    break mainloop;
            }
        }
    }
}
