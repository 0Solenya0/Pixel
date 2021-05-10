package db.queryBuilder;

import model.User;
import model.field.AccessLevel;
import model.Tweet;
import db.exception.ConnectionException;

public class TweetQueryBuilder extends QueryBuilder<Tweet> {

    public TweetQueryBuilder() {
        super();
    }

    public TweetQueryBuilder getByAuthorId(int id) {
        addCustomFilter(tweet -> tweet.getAuthor() == id);
        return this;
    }

    public TweetQueryBuilder getByAuthorUsername(String username) throws ConnectionException {
        User user = context.users.getFirst(context.users.getQueryBuilder().getByUsername(username).getQuery());
        return getByAuthorId(user.id);
    }

    public TweetQueryBuilder getByParentTweet(int id) {
        addCustomFilter(tweet -> tweet.getParentTweet() == id);
        return this;
    }

    public TweetQueryBuilder getEnabled() {
        addCustomFilter(tweet -> {
            try {
                return context.users.get(tweet.getAuthor()).isEnabled();
            }
            catch (Exception e) {
                return false;
            }
        });
        return this;
    }

    public TweetQueryBuilder excludeUser(User user) {
        addCustomFilter(tweet -> tweet.getAuthor() != user.id);
        return this;
    }

    public TweetQueryBuilder getPublicTweet() {
        addCustomFilter(tweet -> {
            try {
                return context.users.get(tweet.getAuthor()).getVisibility() == AccessLevel.PUBLIC;
            }
            catch (Exception e) {
                return false;
            }
        });
        return this;
    }

    public TweetQueryBuilder getNotMuted(User user) {
        addCustomFilter(tweet -> {
            try {
                return !user.isMuted(context.users.get(tweet.getAuthor()));
            }
            catch (Exception e) {
                return false;
            }
        });
        return this;
    }
}
