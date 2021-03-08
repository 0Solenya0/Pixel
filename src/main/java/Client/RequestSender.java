package Client;

import Server.Requests;
import Server.models.Tweet;
import Server.models.User;

public class RequestSender {
    private static RequestSender instance;
    private User client;

    public static RequestSender getInstance() {
        if (instance == null)
            instance = new RequestSender();
        return instance;
    }

    public String getUsername() {
        return client.username;
    }

    public static void login(String username, String password) throws Exception {
        getInstance().client = Server.Requests.login(username, password);
    }
    public static void register(String username, String password, String mail, String name, String surname) throws Exception {
        Requests.register(username, password, mail, name, surname);
    }

    public static User getProfile(String targetuser) {
        try {
            return Server.Requests.getProfile(getInstance().client, targetuser);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static Tweet postTweet(String content) throws Exception {
        Tweet tweet = new Tweet(getInstance().client, content);
        return Server.Requests.postTweet(getInstance().client, tweet);
    }
}
