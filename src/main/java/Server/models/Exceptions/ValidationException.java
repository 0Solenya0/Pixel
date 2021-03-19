package Server.models.Exceptions;

public class ValidationException extends Exception {
    private String targetField;
    private String targetClass;
    public ValidationException(String tField, String tClass, String message) {
        super(message);
        this.targetField = tField;
        this.targetClass = tClass;
    }

    @Override
    public String getMessage() {
        return String.format("%s validation failed - %s", targetField, super.getMessage());
    }
}
