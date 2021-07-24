package server.controllers;

import org.hibernate.Session;
import server.db.HibernateUtil;
import server.db.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

public class RegisterController extends Controller {

    public static Packet process(Packet req) {
        User user = new User();
        user.setUsername(req.get("username", null));
        user.setName(req.get("name", null));
        user.setSurname(req.get("surname", null));
        user.setMailAddress(req.get("mail", null));
        HibernateUtil.save(user);
        return new Packet(StatusCode.CREATED);
    }
}
