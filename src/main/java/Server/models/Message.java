package Server.models;

import Server.models.Exceptions.ConnectionException;
import Server.models.Exceptions.ValidationException;
import Server.models.Filters.MessageFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Message extends Model {
    private static final Logger logger = LogManager.getLogger(Message.class);

    public int user1, user2, tweetId = 0;
    public boolean seen = false;
    private String content;

    public String getContent() throws ConnectionException {
        if (tweetId == 0)
            return content;
        return "\tForwarded Tweet \n" + Tweet.get(tweetId).getContent();
    }

    public Message(int user1, int user2, String content) {
        super();
        this.user1 = user1;
        this.user2 = user2;
        this.content = content;
    }
    public Message(int user1, int user2, int tweetId) {
        super();
        this.user1 = user1;
        this.user2 = user2;
        this.tweetId = tweetId;
    }
    public Message(int tweetId) {
        this.tweetId = tweetId;
    }
    public Message(String content) {
        this.content = content;
    }

    public void see() throws ConnectionException {
        seen = true;
        try {
            save();
        }
        catch (ValidationException e) { }
    }

    public static MessageFilter getFilter() throws ConnectionException {
        return (new MessageFilter());
    }
    public void isValid() throws ValidationException {
        if (!(user1 > 0 && user1 <= Model.getLastId(User.class))) {
            logger.debug("user with id " + user1 + " is not valid");
            throw new ValidationException("User", "Message", "Message sender user is not valid");
        }
        if (!(user2 > 0 && user2 <= Model.getLastId(User.class))) {
            logger.debug("user with id " + user2 + " is not valid");
            throw new ValidationException("User", "Message", "Message receiver user is not valid");
        }
        if (content.isBlank() && tweetId == 0) {
            logger.debug("empty message validation failed");
            throw new ValidationException("Content", "Message", "Message does not have any content");
        }
    }
}