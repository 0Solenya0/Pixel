package server.controllers;

import org.hibernate.Session;
import server.db.HibernateUtil;
import shared.models.Tweet;
import shared.models.fields.AccessLevel;
import shared.request.Packet;
import shared.request.StatusCode;

import javax.management.Query;
import java.util.ArrayList;

public class TweetListController extends Controller {

    @SuppressWarnings("unchecked")
    public Packet respond(Packet req) {
        Packet response = new Packet(StatusCode.OK);
        ArrayList<Tweet> tweets = new ArrayList<>();

        Session session = HibernateUtil.getSession();
        switch (req.target) {
            case "tweet-list-time-line":
                tweets = (ArrayList<Tweet>) session.createQuery(
                        "FROM Tweet AS tweet " +
                                "JOIN tweet.author.followers AS follower " +
                                "JOIN tweet.author.mutedBy AS mute " +
                                "WHERE follower.id = :userId " +
                                "WHERE mute.id != :userId"
                ).setParameter("userId", req.get("user-id")).list();
                break;
            case "tweet-list-explorer":
                tweets = (ArrayList<Tweet>) session.createQuery(
                        "FROM Tweet AS tweet " +
                                "JOIN tweet.author AS author " +
                                "where author.visibility = :vis"
                ).setParameter("vis", AccessLevel.PUBLIC).list();
                break;
        }
        return response;
    }
}
