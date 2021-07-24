package server.controllers;

import org.hibernate.Session;
import server.db.HibernateUtil;
import server.middlewares.Auth;
import server.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

public class LoginController extends Controller {

    public static Packet respond(Packet req) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        User user = (User) session
                .createQuery("from User where username = :u")
                .setParameter("u", req.get("username", ""))
                .uniqueResult();
        if (user != null && user.checkPassword(req.get("password", ""))) {
            Packet response = new Packet(StatusCode.OK);
            String token = Auth.registerUser(user.getId(), req.getInt("handler"));
            response.put("auth-token", token);
            return response;
        }
        else
            return new Packet(StatusCode.FORBIDDEN);
    }
}
