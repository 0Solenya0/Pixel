package server.controllers.user;

import org.hibernate.Session;
import server.controllers.Controller;
import server.db.HibernateUtil;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

import java.util.ArrayList;

public class UserSearchController extends Controller {

    @SuppressWarnings("unchecked")
    public Packet respond(Packet req) {
        Packet res = new Packet(StatusCode.OK);
        Session session = HibernateUtil.getSession();
        ArrayList<User> users = (ArrayList<User>) session
                .createQuery("FROM User WHERE username LIKE '%" + req.get("username") + "%'")
                .list();
        res.putObject("users", users);
        session.close();
        this.session.close();
        return res;
    }
}
