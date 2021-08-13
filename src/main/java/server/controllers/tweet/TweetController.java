package server.controllers.tweet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import server.controllers.Controller;
import server.db.HibernateUtil;
import shared.exception.ValidationException;
import shared.models.Tweet;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

public class TweetController extends Controller {
    private static final Logger logger = LogManager.getLogger(TweetController.class);

    public Packet respond(Packet req) {
        Tweet tweet = new Tweet();
        int parent = req.getInt("parent", 0);
        boolean retweet = req.getBool("retweet", false);
        if (parent != 0) {
            Tweet par = (Tweet) session.get(Tweet.class, parent);
            if (retweet)
                tweet.setRetweet(par);
            else
                tweet.setParent(par);
        }
        tweet.setAuthor((User) session.get(User.class, req.getInt("user-id")));
        tweet.setContent(req.get("content", null));
        tweet.setPhoto(req.getObject("photo", byte[].class));
        try {
            tweet.validate();
        } catch (ValidationException e) {
            return new Packet(StatusCode.BAD_REQUEST).putObject("error", e);
        }
        session.save(tweet);
        logger.info("new tweet " + tweet.id + " was created");
        session.close();
        return new Packet(StatusCode.CREATED);
    }
}
