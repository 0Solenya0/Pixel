package server.controllers.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import server.controllers.Controller;
import shared.models.Group;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

import java.util.ArrayList;

import static server.utils.Functions.notifyRefreshMessage;

public class GroupActionController extends Controller {
    private static final Logger logger = LogManager.getLogger(GroupActionController.class);

    @Override
    public Packet respond(Packet req) {
        Group group = (Group) session.get(Group.class, req.getInt("group-id"));
        switch (req.get("type")) {
            case "add-user":
                User user = (User) session.get(User.class, req.getInt("target-id"));
                if (!group.getUsers().contains(user))
                    group.getUsers().add(user);
                logger.info("new user " + user.getUsername() + " was added to group " + group.getName());
                break;
            case "leave":
                User user1 = (User) session.get(User.class, req.getInt("user-id"));
                group.getUsers().remove(user1);
                logger.info("user " + user1.getUsername() + " was deleted from group " + group.getName());
                break;
        }
        session.save(group);
        notifyRefreshMessage(group.getUsers());
        session.close();
        return new Packet(StatusCode.OK);
    }
}
