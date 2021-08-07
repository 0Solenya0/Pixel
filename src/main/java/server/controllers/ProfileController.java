package server.controllers;

import server.db.HibernateUtil;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

import java.util.ArrayList;

public class ProfileController extends Controller {

    public Packet respond(Packet req) {
        Packet response = new Packet(StatusCode.OK);
        User cur = (User) session.get(User.class, req.getInt("user-id"));
        User user = (User) session.get(User.class, req.getInt("id"));
        if (user == null)
            return new Packet(StatusCode.NOT_FOUND);
        response.putObject("followers", user.getFollowers());
        response.putObject("following", user.getFollowings());
        if (user.id == cur.id)
            response.putObject("blocked", user.getBlocked());
        else
            response.putObject("blocked", new ArrayList<User>());
        response.putObject("user", user);
        response.put("is-blocked", user.blocked.contains(cur));
        response.put("is-contact", user.followers.contains(cur));
        response.put("is-user", user.id == cur.id);
        response.put("is-muted", cur.getMuted().contains(user));
        response.put("can-message", user.followers.contains(cur) || user.followings.contains(cur));
        response.put("follow-requested", false);
        response.put("online", true);
        // TO DO follow request
        // TO DO check for online
        return response;
    }
}
