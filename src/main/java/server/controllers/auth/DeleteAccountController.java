package server.controllers.auth;

import server.controllers.Controller;
import shared.models.User;
import shared.models.fields.AccessLevel;
import shared.request.Packet;
import shared.request.StatusCode;

public class DeleteAccountController extends Controller {
    @Override
    public Packet respond(Packet req) {
        Packet res = new Packet(StatusCode.OK);
        User user = (User) session.get(User.class, req.getInt("user-id"));
        user.setUsername("Deleted Account - " + user.id);
        user.getFollowings().clear();
        user.getFollowers().clear();
        user.getBlocked().clear();
        user.getBlockedBy().clear();
        user.setVisibility(AccessLevel.PRIVATE);
        user.getBirthdate().setAccessLevel(AccessLevel.PRIVATE);
        user.getPhone().setAccessLevel(AccessLevel.PRIVATE);
        user.getLastSeen().setAccessLevel(AccessLevel.PRIVATE);
        user.getMail().setAccessLevel(AccessLevel.PRIVATE);
        session.save(user);
        return res;
    }
}
