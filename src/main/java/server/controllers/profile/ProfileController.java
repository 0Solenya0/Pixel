package server.controllers.profile;

import server.controllers.Controller;
import server.controllers.user.ActionController;
import server.db.HibernateUtil;
import server.middlewares.Auth;
import shared.models.User;
import shared.models.fields.AccessLevel;
import shared.request.Packet;
import shared.request.StatusCode;

import java.util.ArrayList;

public class ProfileController extends Controller {

    public Packet respond(Packet req) {
        Packet response = new Packet(StatusCode.OK);
        User user = (User) session.get(User.class, req.getInt("user-id"));
        User target = (User) session.get(User.class, req.getInt("id"));
        if (target == null)
            return new Packet(StatusCode.NOT_FOUND);
        response.putObject("followers", target.getFollowers());
        response.putObject("following", target.getFollowings());
        if (target.id == user.id)
            response.putObject("blocked", target.getBlocked());
        else
            response.putObject("blocked", new ArrayList<User>());
        response.putObject("user", target);
        response.put("is-blocked", target.blocked.contains(user));
        response.put("is-contact", target.followers.contains(user));
        response.put("is-user", target.id == user.id);
        response.put("is-muted", user.getMuted().contains(target));
        response.put("can-message",
                (target.followers.contains(user) || target.followings.contains(user))
                && target.getVisibility() != AccessLevel.PRIVATE
        );
        response.put("online", Auth.isUserOnline(target.id));
        session.close();
        ActionController controller = new ActionController();
        response.put("follow-requested", controller.getRequest(user, target) != null);
        return response;
    }
}
