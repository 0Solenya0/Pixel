package server.controllers;

import server.db.HibernateUtil;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

public class ActionController extends Controller {

    public Packet respond(Packet req) {
        Packet res = new Packet(StatusCode.OK);
        int t = req.getInt("target-id");
        User user = (User) session.get(User.class, t);
        User cur = (User) session.get(User.class, req.getInt("user-id"));
        switch (req.get("type")) {
            case "toggle-block":
                if (cur.blocked.contains(user))
                    cur.blocked.remove(user);
                else
                    cur.blocked.add(user);
                session.save(cur);
                break;
            case "toggle-mute":
                if (cur.muted.contains(user))
                    cur.muted.remove(user);
                else
                    cur.muted.add(user);
                session.save(cur);
                break;
        }
        return res;
    }
}
