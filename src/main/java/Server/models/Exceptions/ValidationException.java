package Server.models.Exceptions;

import Client.CLI.ConsoleColors;

public class ValidationException extends Exception {
    private String targetField;
    private String targetClass;
    public ValidationException(String tField, String tClass, String message) {
        super(message);
        this.targetField = tField;
        this.targetClass = tClass;
    }
    public void showError() {
        System.out.println(ConsoleColors.RED + getMessage());
    }

    @Override
    public String getMessage() {
        return String.format("%s validation failed - %s", targetField, super.getMessage());
    }
}
