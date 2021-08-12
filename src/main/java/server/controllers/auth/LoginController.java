package server.controllers.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import server.Server;
import server.controllers.Controller;
import server.db.HibernateUtil;
import server.middlewares.Auth;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

public class LoginController extends Controller {
    private static final Logger logger = LogManager.getLogger(Server.class);

    public Packet respond(Packet req) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        User user = (User) session
                .createQuery("from User where username = :u")
                .setParameter("u", req.get("username", ""))
                .uniqueResult();
        session.close();
        if (user != null && user.checkPassword(req.get("password", ""))) {
            logger.info("login request for user " + user.getUsername() + " has been made");
            Packet response = new Packet(StatusCode.OK);
            String token = Auth.registerUser(user.id, req.getInt("handler"));
            response.put("auth-token", token);
            return response;
        }
        else
            return new Packet(StatusCode.FORBIDDEN);
    }
}
