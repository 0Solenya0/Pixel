package server.controllers.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import server.controllers.Controller;
import server.db.HibernateUtil;
import shared.exception.ValidationException;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

import java.time.LocalDateTime;

public class RegisterController extends Controller {
    private static final Logger logger = LogManager.getLogger(RegisterController.class);

    public Packet respond(Packet req) {
        User user = new User();
        user.setUsername(req.get("username", null));
        user.setName(req.get("name", null));
        user.setSurname(req.get("surname", null));
        user.setMailAddress(req.get("mail", null));
        user.setPassword(req.get("password", null));
        user.getLastSeen().set(LocalDateTime.now());
        try {
            user.validate();
        } catch (ValidationException e) {
            Packet response = new Packet(StatusCode.BAD_REQUEST);
            response.putObject("error", e);
            return response;
        }
        session.save(user);
        logger.info("new user " + user.getUsername() + " has registered with id " + user.id);
        session.close();
        return new Packet(StatusCode.CREATED);
    }
}
