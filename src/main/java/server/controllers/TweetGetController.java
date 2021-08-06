package server.controllers;

import server.db.HibernateUtil;
import shared.models.Tweet;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

public class TweetGetController extends Controller {

    public static Packet respond(Packet req) {
        Packet response = new Packet(StatusCode.OK);
        User user = (User) HibernateUtil.get(User.class, req.getInt("user-id"));
        Tweet tweet = null;
        try {
            tweet = (Tweet) HibernateUtil.get(Tweet.class, req.getInt("id"));
        }
        catch (Exception e) {
            return new Packet(StatusCode.NOT_FOUND);
        }
        response.putObject("tweet", tweet);
        response.put("same-user", tweet.getAuthor().id == req.getInt("user-id"));
        response.put("liked", tweet.getLikes().contains(user));
        return response;
    }
}