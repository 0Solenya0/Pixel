package server.controllers.message;

import server.controllers.Controller;
import shared.models.Group;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

import java.util.ArrayList;

public class GroupListController extends Controller {

    @SuppressWarnings("unchecked")
    public Packet respond(Packet req) {
        Packet res = new Packet(StatusCode.OK);
        User user = (User) session.get(User.class, req.getInt("user-id"));
        ArrayList<Group> groups = (ArrayList<Group>) session.getInnerSession().createQuery(
                "SELECT g FROM Group AS g " +
                        "LEFT JOIN g.users as us " +
                        "WHERE us.id = :u"
        ).setParameter("u", user.id).list();
        System.out.println(groups);
        res.putObject("list", groups);
        return res;
    }
}
