package server.controllers;

import server.db.HibernateUtil;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

public class ProfileDataController extends Controller {

    public static Packet respond(Packet req) {
        Packet response = new Packet(StatusCode.OK);
        int id = req.getInt("user-id");
        User user = (User) HibernateUtil.getSession()
                .createQuery("from User where id = :i")
                .setParameter("i", id)
                .uniqueResult();
        response.putObject("user", user);
        return response;
    }
}
