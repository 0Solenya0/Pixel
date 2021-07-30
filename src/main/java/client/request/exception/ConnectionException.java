package client.request.exception;

public class ConnectionException extends Exception {
    private ErrorType errorType;

    public ConnectionException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    public enum ErrorType {
        INTERNAL_SERVER_ERROR,
        CONNECTION_ERROR
    }
}
