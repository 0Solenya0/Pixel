package server.controllers.tweet;

import server.controllers.Controller;
import server.db.HibernateUtil;
import shared.models.Tweet;
import shared.models.User;
import shared.models.fields.AccessLevel;
import shared.request.Packet;
import shared.request.StatusCode;

public class TweetGetController extends Controller {

    public Packet respond(Packet req) {
        Packet response = new Packet(StatusCode.OK);
        User user = (User) session.get(User.class, req.getInt("user-id"));
        Tweet tweet = null;
        try {
            tweet = (Tweet) session.get(Tweet.class, req.getInt("id"));
        }
        catch (Exception e) {
            return new Packet(StatusCode.NOT_FOUND);
        }
        response.put("same-user", tweet.getAuthor().id == req.getInt("user-id"));
        if (!response.getBool("same-user")) {
            if (!(tweet.getAuthor().getVisibility().equals(AccessLevel.PUBLIC) ||
                    (tweet.getAuthor().getVisibility().equals(AccessLevel.CONTACTS) && tweet.getAuthor().followers.contains(user))))
                return new Packet(StatusCode.FORBIDDEN);
        }
        response.putObject("tweet", tweet);
        response.put("liked", tweet.getLikes().contains(user));
        session.close();
        return response;
    }
}
