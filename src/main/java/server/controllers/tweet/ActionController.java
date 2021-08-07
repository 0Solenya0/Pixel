package server.controllers.tweet;

import server.controllers.Controller;
import shared.models.Tweet;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

public class ActionController extends Controller {

    public Packet respond(Packet req) {
        User user = (User) session.get(User.class, req.getInt("user-id"));
        Tweet tweet = (Tweet) session.get(Tweet.class, req.getInt("tweet-id"));
        if (tweet == null)
            return new Packet(StatusCode.NOT_FOUND);
        switch (req.get("type")) {
            case "toggle-like":
                if (tweet.getLikes().contains(user))
                    tweet.getLikes().remove(user);
                else
                    tweet.getLikes().add(user);
                break;
            case "report":
                tweet.getReports().add(user);
                break;
        }
        session.save(tweet);
        return new Packet(StatusCode.OK);
    }
}
