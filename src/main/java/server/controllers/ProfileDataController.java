package server.controllers;

import server.db.HibernateUtil;
import server.db.models.User;
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
        response.put("username", user.getUsername());
        response.put("name", user.getName());
        response.put("surname", user.getSurname());
        response.put("bio", user.getBio());
        response.putObject("visibility", user.getVisibility());
        response.putObject("birthday", user.getBirthdate());
        response.putObject("mail", user.getMail());
        response.putObject("phone", user.getPhone());
        response.putObject("last-seen", user.getLastSeen());
        return response;
    }
}
