package server.controllers.message;

import server.controllers.Controller;
import shared.models.Group;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

public class GroupActionController extends Controller {
    @Override
    public Packet respond(Packet req) {
        Group group = (Group) session.get(Group.class, req.getInt("group-id"));
        switch (req.get("type")) {
            case "add-user":
                User user = (User) session.get(User.class, req.getInt("target-id"));
                if (!group.getUsers().contains(user))
                    group.getUsers().add(user);
                break;
            case "leave":
                User user1 = (User) session.get(User.class, req.getInt("user-id"));
                group.getUsers().remove(user1);
                break;
        }
        session.save(group);
        // TO DO notify user
        return new Packet(StatusCode.OK);
    }
}
