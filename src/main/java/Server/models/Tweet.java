package Server.models;

import Client.CLI.ConsoleColors;
import Server.models.Filters.TweetFilter;
import Server.models.Filters.UserFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;

public class Tweet extends Model {
    private static final Logger logger = LogManager.getLogger(Tweet.class);

    public static final String datasrc = "./db/Tweets";
    public String getdatasrc() {
        return datasrc;
    }

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
    public User getAuthor() throws Exception {
        return User.get(author);
    }

    public Tweet(User author, String text) {
        this.author = author.id;
        this.content = text;
        this.isActive = true;
    }
    public Tweet(int id) throws IOException {
        JSONObject tweet = loadJSON(id, datasrc);
        this.id = id;
        this.parentTweet = Integer.parseInt(tweet.get("parentTweet").toString());
        this.reTweet = Integer.parseInt(tweet.get("reTweet").toString());
        this.author = Integer.parseInt(tweet.get("author").toString());
        this.content = tweet.getString("content");
        this.isActive = Boolean.parseBoolean(tweet.get("isActive").toString());

        logger.info(String.format("TweetId %s fetched successfully. %s", id, this.getJSON()));
    }

    /** Must be in every model section **/
    public static Tweet get(int id) throws IOException {
        return new Tweet(id);
    }
    public static TweetFilter getFilter() throws IOException {
        return new TweetFilter();
    }

    public boolean isValid() throws Exception {
        if (author == 0)
            throw new Exception("Tweet author is not valid.");
        if (content == null || content.isEmpty())
            throw new Exception("Tweet doesn't have any content.");
        return true;
    }
    public JSONObject getJSON() {
        JSONObject tweet = new JSONObject();
        tweet.put("id", id);
        tweet.put("author", author);
        tweet.put("content", content);
        tweet.put("parentTweet", parentTweet);
        tweet.put("reTweet", reTweet);
        return tweet;
    }
}
