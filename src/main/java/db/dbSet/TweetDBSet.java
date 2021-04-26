package db.dbSet;

import apps.auth.model.User;
import apps.tweet.model.Tweet;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TweetDBSet extends DBSet<Tweet> {
    private static final Logger logger = LogManager.getLogger(UserDBSet.class);
    public TweetDBSet() {
        super(Tweet.class);
    }

    @Override
    public void validate(Tweet model) throws ValidationException {
        ValidationException validationException = new ValidationException();
        if (model.getAuthor() == 0)
            validationException.addError("Author", "Tweet author is not valid.");
        if ((model.getContent() == null || model.getContent().isEmpty()) && model.getReTweet() != 0)
            validationException.addError("Content", "Tweet doesn't have any content.");

        if (validationException.hasError()) {
            logger.debug(validationException.getLog());
            throw validationException;
        }
    }
}
