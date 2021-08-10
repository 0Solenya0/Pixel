package server.controllers.tweet;

import org.hibernate.Session;
import server.controllers.Controller;
import server.db.HibernateUtil;
import shared.models.Tweet;
import shared.models.User;
import shared.models.fields.AccessLevel;
import shared.request.Packet;
import shared.request.StatusCode;
import shared.util.Config;

import javax.management.Query;
import java.util.ArrayList;
import java.util.Comparator;

public class TweetListController extends Controller {

    private Config config = Config.getConfig("mainConfig");

    @SuppressWarnings("unchecked")
    public Packet respond(Packet req) {
        Packet response = new Packet(StatusCode.OK);
        User user = (User) session.get(User.class, req.getInt("user-id"));
        ArrayList<Tweet> tweets = new ArrayList<>();

        Session session = HibernateUtil.getSession();
        switch (req.target) {
            case "tweet-list-home":
                tweets = (ArrayList<Tweet>) session.createQuery(
                        "SELECT tweet FROM Tweet AS tweet " +
                                "JOIN tweet.author AS author " +
                                "LEFT JOIN author.followers AS follower " +
                                "WHERE follower.id = :userId"
                ).setParameter("userId", req.getInt("user-id")).list();
                tweets.removeIf(
                        (t) -> t.getAuthor().getMutedBy().contains(user));
                tweets.removeIf(
                        (t) -> t.getReports().size() > config.getProperty(Integer.class, "MAX_TWEET_REPORT"));
                break;
            case "tweet-list-explorer":
                tweets = (ArrayList<Tweet>) session.createQuery(
                        "SELECT tweet FROM Tweet AS tweet " +
                                "JOIN tweet.author AS author " +
                                "where author.visibility = :vis " +
                                "and author.id != :u"
                ).setParameter("vis", AccessLevel.PUBLIC)
                        .setParameter("u", req.getInt("user-id")).list();
                tweets.sort(Comparator.comparingInt((t) -> -t.getLikes().size()));
                tweets.removeIf(
                        (t) -> t.getAuthor().getMutedBy().contains(user));
                tweets.removeIf(
                        (t) -> t.getReports().size() > config.getProperty(Integer.class, "MAX_TWEET_REPORT"));
                break;
            case "tweet-list-user":
                tweets = (ArrayList<Tweet>) session.createQuery(
                        "SELECT tweet FROM Tweet AS tweet " +
                                "where tweet.author.id = :u"
                ).setParameter("u", req.getInt("target")).list();
                break;
            case "tweet-list-comment":
                tweets = (ArrayList<Tweet>) session.createQuery(
                        "FROM Tweet as tweet " +
                                "where tweet.parent.id = :t"
                ).setParameter("t", req.getInt("tweet-id")).list();
                break;
        }
        tweets.removeIf(tweet -> user.getBlocked().contains(tweet.getAuthor()));
        tweets.removeIf(tweet -> tweet.getAuthor().getVisibility().equals(AccessLevel.PRIVATE));

        response.putObject("tweets", tweets);
        session.close();

        return response;
    }
}
