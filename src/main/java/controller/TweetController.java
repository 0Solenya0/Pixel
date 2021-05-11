package controller;

import db.dbSet.UserDBSet;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import model.Tweet;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Config;

public class TweetController extends Controller {
    private static final Logger logger = LogManager.getLogger(TweetController.class);
    private final Config config = Config.getConfig("mainConfig");

    public void likeTweet(Tweet tweet, User user) throws ConnectionException {
        tweet = context.tweets.get(tweet.id);
        tweet.addLike(user);
        try {
            context.tweets.save(tweet);
        }
        catch (ValidationException e) {
            logger.error("like tweet failed by unexpected validation error");
            logger.error(e.getLog());
        }
    }

    public void dislikeTweet(Tweet tweet, User user) throws ConnectionException {
        tweet = context.tweets.get(tweet.id);
        tweet.removeLike(user);
        try {
            context.tweets.save(tweet);
        }
        catch (ValidationException e) {
            logger.error("dislike tweet failed by unexpected validation error");
            logger.error(e.getLog());
        }
    }

    public void reportTweet(Tweet tweet, User user) throws ConnectionException {
        tweet = context.tweets.get(tweet.id);
        tweet.addReport(user);
        try {
            context.tweets.save(tweet);
        }
        catch (ValidationException e) {
            logger.error("dislike tweet failed by unexpected validation error");
            logger.error(e.getLog());
        }
        if (tweet.getReports().size() > Integer.parseInt(config.getProperty("MAX_TWEET_REPORTS")))
            context.tweets.delete(tweet);
    }
}
