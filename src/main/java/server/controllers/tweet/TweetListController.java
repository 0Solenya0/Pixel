package server.controllers.tweet;

import org.hibernate.Session;
import server.controllers.Controller;
import server.db.HibernateUtil;
import shared.models.Tweet;
import shared.models.fields.AccessLevel;
import shared.request.Packet;
import shared.request.StatusCode;

import javax.management.Query;
import java.util.ArrayList;
import java.util.Comparator;

public class TweetListController extends Controller {

    @SuppressWarnings("unchecked")
    public static Packet respond(Packet req) {
        Packet response = new Packet(StatusCode.OK);
        ArrayList<Tweet> tweets = null;

        Session session = HibernateUtil.getSession();
        switch (req.target) {
            case "tweet-list-time-line":
                tweets = (ArrayList<Tweet>) session.createQuery(
                        "SELECT tweet FROM Tweet AS tweet " +
                                "JOIN tweet.author.followers AS follower " +
                                "JOIN tweet.author.mutedBy AS mute " +
                                "WHERE follower.id = :userId " +
                                "WHERE mute.id != :userId"
                ).setParameter("userId", req.getInt("user-id")).list();
                // TO DO remove muted user
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
                // TO DO remove muted user
                break;
            case "tweet-list-user":
                tweets = (ArrayList<Tweet>) session.createQuery(
                        "SELECT tweet FROM Tweet AS tweet " +
                                "where tweet.author.id = :u"
                ).setParameter("u", req.getInt("target")).list();
                break;
        }
        response.putObject("tweets", tweets);
        session.close();

        return response;
    }
}
