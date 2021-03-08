package Server.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class Tweet extends Model {
    private static final Logger logger = LogManager.getLogger(User.class);

    public static final String datasrc = "./db/Tweets";
    public String getdatasrc() {
        return datasrc;
    }

    public int id;
    public User author;
    public String text;

    public JSONObject getJSON() {
        JSONObject tweet = new JSONObject();
        tweet.put("author", author.id);
    }
}
