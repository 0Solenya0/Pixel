package server.controllers.userlist;

import server.controllers.Controller;
import shared.models.UserList;
import shared.request.Packet;
import shared.request.StatusCode;

import java.util.ArrayList;

public class GetUserListController extends Controller {

    @SuppressWarnings("unchecked")
    public Packet respond(Packet req) {
        Packet res = new Packet(StatusCode.OK);
        res.putObject("list", (ArrayList<UserList>) session.getInnerSession().createQuery(
                "FROM UserList as list " +
                        "WHERE list.owner.id = :u"
        ).setParameter("u", req.getInt("user-id")).list());
        return res;
    }
}
