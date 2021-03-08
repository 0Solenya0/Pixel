package Server;

import Server.models.Tweet;
import Server.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Requests {
    private static final Logger logger = LogManager.getLogger(User.class);

    public static User login(String username, String password) throws Exception {
        User u = User.getFilter().getUsername(username);
        if (u == null)
            throw new Exception("Username does not exist.");
        if (u.checkPassword(password)) {
            logger.info(String.format("Login successful. Username: %s", username));
            return u;
        }
        else
            throw new Exception("Password is incorrect.");
    }
    public static User register(String username, String password, String mail, String name, String surname) throws Exception{
        User user = new User(name, surname, username, mail, password);
        user.save();
        logger.info(String.format("new user created - %s", user.getJSON()));
        return user;
    }

    public static User getProfile(User client, String targetUser) throws Exception {
        if (!User.getFilter().getUsername(client.username).checkPassword(client.getPassword())) {
            logger.debug("Authentication failed - " + client.getJSON());
            throw new Exception("Authentication failed.");
        }
        User user = User.getFilter().getUsername(targetUser);
        if (client.username.equals(targetUser))
            return user;
        return null;
    }

    public static Tweet postTweet(User client, Tweet tweet) throws Exception {
        if (!User.getFilter().getUsername(client.username).checkPassword(client.getPassword())) {
            logger.debug("Authentication failed - " + client.getJSON());
            throw new Exception("Authentication failed.");
        }
        if (tweet.getAuthorId() == client.id) {
            tweet.save();
            return tweet;
        }
        else {
            logger.warn("Illegal access user created a tweet with different author - " + client.username + " - " + tweet.getJSON());
            throw new Exception("User and tweets author doesn't match.");
        }
    }
}