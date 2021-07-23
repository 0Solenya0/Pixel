package client.request.exception;

public class ResponseException extends Exception {
    public ResponseException(String error) {
        super(error);
    }
}
