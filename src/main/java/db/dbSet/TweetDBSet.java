package db.dbSet;

import model.User;
import model.Tweet;
import util.Config;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import db.queryBuilder.TweetQueryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TweetDBSet extends DBSet<Tweet> {
    private static final Logger logger = LogManager.getLogger(UserDBSet.class);
    private Config config = Config.getConfig("mainConfig");
    public TweetDBSet() {
        super(Tweet.class);
    }

    @Override
    public void validate(Tweet model) throws ValidationException {
        ValidationException validationException = new ValidationException();
        if (model.getAuthor() == 0)
            validationException.addError("Author", "Tweet author is not valid.");
        if ((model.getContent() == null || model.getContent().isBlank()) && model.getReTweet() == 0)
            validationException.addError("Content", "Tweet doesn't have any content.");

        if (validationException.hasError()) {
            logger.debug(validationException.getLog());
            throw validationException;
        }
    }

    public void likeTweet(Tweet tweet, User user) throws ConnectionException {
        tweet = get(tweet.id);
        tweet.addLike(user);
        try {
            save(tweet);
        }
        catch (ValidationException e) {
            logger.error("like tweet failed by unexpected validation error");
            logger.error(e.getLog());
        }
    }

    public void dislikeTweet(Tweet tweet, User user) throws ConnectionException {
        tweet = get(tweet.id);
        tweet.removeLike(user);
        try {
            save(tweet);
        }
        catch (ValidationException e) {
            logger.error("dislike tweet failed by unexpected validation error");
            logger.error(e.getLog());
        }
    }

    public void reportTweet(Tweet tweet, User user) throws ConnectionException {
        tweet = get(tweet.id);
        tweet.addReport(user);
        try {
            save(tweet);
        }
        catch (ValidationException e) {
            logger.error("dislike tweet failed by unexpected validation error");
            logger.error(e.getLog());
        }
        if (tweet.getReports().size() > Integer.parseInt(config.getProperty("MAX_TWEET_REPORTS")))
            delete(tweet);
    }

    @Override
    public TweetQueryBuilder getQueryBuilder() {
        return new TweetQueryBuilder();
    }
}
