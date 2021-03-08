package Server.models;

import Server.models.Filters.TweetFilter;
import Server.models.Filters.UserFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;

public class Tweet extends Model {
    private static final Logger logger = LogManager.getLogger(User.class);

    public static final String datasrc = "./db/Tweets";
    public String getdatasrc() {
        return datasrc;
    }

    public int id;
    public int author;
    public String content;

    public Tweet(User author, String text) {
        this.author = author.id;
        this.content = text;
    }
    public Tweet(int id) throws IOException {
        JSONObject tweet = loadJSON(id, datasrc);
        this.id = id;
        this.author = Integer.parseInt(tweet.getString("author"));
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
        if (id == 0)
            throw new Exception("Tweet author is not valid.");
        if (content == null || content.isEmpty())
            throw new Exception("Tweet doesn't have any content.");
        return true;
    }
    public JSONObject getJSON() {
        JSONObject tweet = new JSONObject();
        tweet.put("author", id);
        tweet.put("content", content);
        return tweet;
    }
}