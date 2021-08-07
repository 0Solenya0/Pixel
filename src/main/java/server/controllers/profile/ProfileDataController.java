package server.controllers.profile;

import org.hibernate.Session;
import server.controllers.Controller;
import server.db.HibernateUtil;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

public class ProfileDataController extends Controller {

    public Packet respond(Packet req) {
        Session session = HibernateUtil.getSession();
        Packet response = new Packet(StatusCode.OK);
        int id = req.getInt("user-id");
        User user = (User) session
                .createQuery("from User where id = :i")
                .setParameter("i", id)
                .uniqueResult();
        response.putObject("user", user);
        session.close();
        return response;
    }
}
