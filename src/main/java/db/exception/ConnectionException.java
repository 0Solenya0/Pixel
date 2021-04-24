package db.exception;

import org.apache.logging.log4j.Logger;

public class ConnectionException extends Exception {
    public ConnectionException(String message) {
        super(message);
    }
    public ConnectionException() {
        super("Database Access Failed");
    }
    public void logException(Logger logger) {
        logger.error("Connection to database failed - " + this.getMessage());
    }
}
