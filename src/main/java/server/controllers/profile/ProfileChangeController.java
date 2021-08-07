package server.controllers.profile;

import org.hibernate.Session;
import server.controllers.Controller;
import server.db.HibernateUtil;
import shared.exception.ValidationException;
import shared.models.User;
import shared.models.fields.AccessLevel;
import shared.request.Packet;
import shared.request.StatusCode;

import java.time.LocalDate;

public class ProfileChangeController extends Controller {

    public Packet respond(Packet req) {
        Packet response = new Packet(StatusCode.OK);
        Session session = HibernateUtil.getSession();
        User user = (User) session
                .createQuery("from User where id = :i")
                .setParameter("i", req.getInt("user-id"))
                .uniqueResult();
        session.close();
        user.setName(req.get("name", user.getName()));
        user.setSurname(req.get("surname", user.getSurname()));

        user.getPhone().set(req.get("phone-number", user.getPhone().get()));
        user.getPhone().setAccessLevel(
                req.getObject("phone-access", AccessLevel.class, user.getPhone().getAccessLevel())
        );
        user.getBirthdate().set(
                req.getObject("birthday", LocalDate.class, user.getBirthdate().get())
        );
        user.getBirthdate().setAccessLevel(
                req.getObject("birthday-access", AccessLevel.class, user.getBirthdate().getAccessLevel())
        );
        user.setBio(req.get("bio", user.getBio()));

        user.getMail().setAccessLevel(
                req.getObject("mail-access", AccessLevel.class, user.getMail().getAccessLevel())
        );
        user.getLastSeen().setAccessLevel(
                req.getObject("last-seen-access", AccessLevel.class, user.getLastSeen().getAccessLevel())
        );
        user.setVisibility(req.getObject("visibility", AccessLevel.class, user.getVisibility()));

        user.setPhoto(req.getObject("photo", byte[].class, user.getPhoto()));
        try {
            user.validate();
        } catch (ValidationException e) {
            return new Packet(StatusCode.BAD_REQUEST).putObject("error", e.getAllErrors().get(0));
        }
        this.session.save(user);
        return response;
    }
}
