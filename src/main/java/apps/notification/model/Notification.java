package apps.notification.model;

import apps.auth.model.User;
import apps.notification.model.field.NotificationType;
import model.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Notification extends Model {
    private static final Logger logger = LogManager.getLogger(Notification.class);

    private int user1, user2;
    protected String message;
    private NotificationType type;

    public Notification(int user1, int user2, String content) {
        super();
        this.user1 = user1;
        this.user2 = user2;
        this.type = NotificationType.INFO;
        this.message = content;
    }

    public Notification(int user1, int user2, NotificationType type) {
        super();
        this.user1 = user1;
        this.user2 = user2;
        this.type = type;
        this.message = "";
    }

    public int getSender() {
        return user1;
    }

    public NotificationType getType() {
        return type;
    }

    public int getReceiver() {
        return user2;
    }

    public String getMessage(String senderUsername) {
        if (type == NotificationType.INFO)
            return message;
        else if (type == NotificationType.REPORT)
            return "You have been reported please BEHAVE...";
        else
            return senderUsername + " has requested to follow you";
    }

    public String getMessageForSender(String receiverUsername) {
        if (type == NotificationType.INFO)
            return message;
        else
            return "your request to " + receiverUsername + " is pending";
    }
}
