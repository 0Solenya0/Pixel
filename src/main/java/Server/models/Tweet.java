package Server.models;

import Server.models.Exceptions.ConnectionException;
import Server.models.Exceptions.ValidationException;
import Server.models.Filters.TweetFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    /** Must be in every model section **/
    public static Tweet get(int id) throws ConnectionException {
        return (Tweet) loadObj(id, datasrc, Tweet.class);
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
}
