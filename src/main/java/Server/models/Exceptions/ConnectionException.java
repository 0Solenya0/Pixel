package Server.models.Exceptions;

public class ConnectionException extends Exception {
    public ConnectionException(String message) {
        super(message);
    }
    public ConnectionException() {
        super("Database Access Failed");
    }
}
