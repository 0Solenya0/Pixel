package db.queryBuilder;

import apps.auth.model.User;
import apps.tweet.model.Tweet;
import config.Config;
import db.Context;
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
}
