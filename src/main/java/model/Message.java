package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Message extends Model {
    private static final Logger logger = LogManager.getLogger(Message.class);

    private int user1, user2, tweetId = 0;
    private boolean seen = false;
    private String content;

    public Message(int user1, int user2, String content) {
        super();
        this.user1 = user1;
        this.user2 = user2;
        this.content = content;
    }

    public Message(int user1, int user2, int tweetId) {
        super();
        content = "";
        this.user1 = user1;
        this.user2 = user2;
        this.tweetId = tweetId;
    }

    public int getSender() {
        return user1;
    }

    public void setSender(int user1) {
        this.user1 = user1;
    }

    public void setReceiver(int user2) {
        this.user2 = user2;
    }

    public void setTweetId(int tweetId) {
        this.tweetId = tweetId;
    }

    public int getReceiver() {
        return user2;
    }

    public int getTweetId() {
        return tweetId;
    }

    public boolean isSeen() {
        return seen;
    }

    public String getContent() {
        return content;
    }
}
