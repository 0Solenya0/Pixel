package server.controllers.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import server.controllers.Controller;
import shared.models.Group;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

public class CreateGroupController extends Controller {
    private static final Logger logger = LogManager.getLogger(CreateGroupController.class);

    public Packet respond(Packet req) {
        User user = (User) session.get(User.class, req.getInt("user-id"));
        Group group = new Group();
        group.setName(req.get("name"));
        group.getUsers().add(user);
        session.save(group);
        logger.info("new group " + group.getName() + " was created by " + user.getUsername());
        return new Packet(StatusCode.OK);
    }
}
