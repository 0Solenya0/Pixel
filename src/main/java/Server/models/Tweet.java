package Server.models;

import Client.CLI.ConsoleColors;
import Server.models.Exceptions.ConnectionException;
import Server.models.Exceptions.ValidationException;
import Server.models.Filters.TweetFilter;
import Server.models.Filters.UserFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;

public class Tweet extends Model {
    private static final Logger logger = LogManager.getLogger(Tweet.class);
    public static final String datasrc = "./db/" + Tweet.class.getName();

    public int parentTweet;
    public int reTweet;
    private int author;
    private String content;

    public String getContent() throws Exception {
        if (reTweet != 0)
            return content + "\nRetweeted from " + Tweet.get(reTweet).getAuthor().username  + ":\n" + Tweet.get(reTweet).content;
        return content;
    }
    public int getAuthorId() {
        return author;
    }
    public User getAuthor() throws ConnectionException {
        return User.get(author);
    }

    public Tweet(User author, String text) {
        super();
        this.author = author.id;
        this.content = text;
    }
    public Tweet(int id) throws ConnectionException {
        this.id = id;
        this.isDeleted = loadJSON(id, getDataSource()).getBoolean("isDeleted");

        JSONObject tweet = loadJSON(id, datasrc);
        this.parentTweet = Integer.parseInt(tweet.get("parentTweet").toString());
        this.reTweet = Integer.parseInt(tweet.get("reTweet").toString());
        this.author = Integer.parseInt(tweet.get("author").toString());
        this.content = tweet.getString("content");

        logger.info(String.format("TweetId %s fetched successfully. %s", id, this.getJSON()));
    }

    /** Must be in every model section **/
    public static Tweet get(int id) throws ConnectionException {
        return new Tweet(id);
    }
    public static TweetFilter getFilter() throws ConnectionException {
        return new TweetFilter();
    }

    public void isValid() throws ValidationException {
        if (author == 0)
            throw new ValidationException("Author", "Tweet", "Tweet author is not valid.");
        if (content == null || content.isEmpty())
            throw new ValidationException("Content", "Tweet", "Tweet doesn't have any content.");
    }
    public JSONObject getJSON() {
        JSONObject tweet = new JSONObject();
        tweet.put("author", author);
        tweet.put("content", content);
        tweet.put("parentTweet", parentTweet);
        tweet.put("reTweet", reTweet);
        return tweet;
    }
}
