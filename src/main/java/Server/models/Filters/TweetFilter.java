package Server.models.Filters;

import Server.models.Exceptions.ConnectionException;
import Server.models.Tweet;
import Server.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TweetFilter extends ModelFilter<Tweet> {

    public TweetFilter() throws ConnectionException {
        super(Tweet.class);
    }

    public TweetFilter getByUser(String username) {
        customFilter(tweet -> {
            try {
                return tweet.getAuthor().username.equals(username);
            } catch (Exception e) {
                return false;
            }
        });
        return this;
    }
    public TweetFilter getByParentTweet(int id) {
        customFilter(tweet -> tweet.parentTweet == id);
        return this;
    }
    public TweetFilter getEnabled() {
        customFilter(tweet -> {
            try {
                return tweet.getAuthor().isEnabled();
            }
            catch (Exception e) {
                return false;
            }
        });
        return this;
    }
}
