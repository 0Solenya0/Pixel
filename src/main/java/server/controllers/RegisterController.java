package server.controllers;

import server.db.HibernateUtil;
import shared.exception.ValidationException;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

public class RegisterController extends Controller {

    public Packet respond(Packet req) {
        User user = new User();
        user.setUsername(req.get("username", null));
        user.setName(req.get("name", null));
        user.setSurname(req.get("surname", null));
        user.setMailAddress(req.get("mail", null));
        user.setPassword(req.get("password", null));
        try {
            user.validate();
        } catch (ValidationException e) {
            Packet response = new Packet(StatusCode.BAD_REQUEST);
            response.putObject("error", e);
            return response;
        }
        session.save(user);
        session.close();
        return new Packet(StatusCode.CREATED);
    }
}
