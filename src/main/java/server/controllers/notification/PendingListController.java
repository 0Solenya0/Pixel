package server.controllers.notification;

import server.controllers.Controller;
import server.db.fields.RequestState;
import server.db.models.FollowRequest;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

import java.util.ArrayList;

public class PendingListController extends Controller {

    @SuppressWarnings("unchecked")
    public Packet respond(Packet req) {
        Packet res = new Packet(StatusCode.OK);
        ArrayList<FollowRequest> requests =
                (ArrayList<FollowRequest>) session.getInnerSession().createQuery(
                                "FROM FollowRequest as req " +
                                        "WHERE req.sender.id = :sender " +
                                        "AND req.state = :state"
                        ).setParameter("sender", req.getInt("user-id"))
                        .setParameter("state", RequestState.PENDING).list();
        ArrayList<User> users = new ArrayList<User>();
        for (FollowRequest request: requests)
            users.add(request.getReceiver());
        res.putObject("users", users);
        return res;
    }
}
