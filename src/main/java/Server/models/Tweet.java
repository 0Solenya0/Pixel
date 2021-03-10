package Server.models;

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

    private int author;
    private String content;

    public String getContent() {
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
    }
    public Tweet(int id) throws IOException {
        JSONObject tweet = loadJSON(id, datasrc);
        this.id = id;
        this.author = Integer.parseInt(tweet.get("author").toString());
        this.content = tweet.getString("content");

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
        return tweet;
    }
}
