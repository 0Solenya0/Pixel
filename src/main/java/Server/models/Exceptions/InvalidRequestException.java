package Server.models.Exceptions;

import Client.CLI.ConsoleColors;

public class InvalidRequestException extends Exception {
    public InvalidRequestException(String message) {
        super(message);
    }
    public void showError() {
        System.out.println(ConsoleColors.RED + getMessage());
    }
}
