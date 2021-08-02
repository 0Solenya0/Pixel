package server.controllers;

import server.db.HibernateUtil;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

public class ActionController extends Controller {

    public static Packet respond(Packet req) {
        Packet res = new Packet(StatusCode.OK);
        int t = req.getInt("target-id");
        User user = (User) HibernateUtil.get(User.class, t);
        User cur = (User) HibernateUtil.get(User.class, req.getInt("user-id"));
        if (req.get("type").equals("toggle-block")) {
            if (cur.blocked.contains(user))
                cur.blocked.remove(user);
            else
                cur.blocked.add(user);
            HibernateUtil.save(cur);
        }
        return res;
    }
}
