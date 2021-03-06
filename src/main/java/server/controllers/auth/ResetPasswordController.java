package server.controllers.auth;

import org.hibernate.Session;
import server.controllers.Controller;
import server.db.HibernateUtil;
import shared.exception.ValidationException;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

public class ResetPasswordController extends Controller {

    public Packet respond(Packet req) {
        Session session = HibernateUtil.getSession();
        User user = (User) session
                .createQuery("from User where id = :i")
                .setParameter("i", req.getInt("user-id"))
                .uniqueResult();
        if (!user.checkPassword(req.get("old-pass")))
            return new Packet(StatusCode.FORBIDDEN).put("error", "wrong pass");
        user.setPassword(req.get("new-pass"));
        try {
            user.validate();
        } catch (ValidationException e) {
            return new Packet(StatusCode.BAD_REQUEST).putObject("error", e);
        }
        session.close();
        this.session.save(user);
        this.session.close();
        return new Packet(StatusCode.OK);
    }
}
