package server.controllers.message;

import server.controllers.Controller;
import shared.models.Group;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

public class CreateGroupController extends Controller {

    public Packet respond(Packet req) {
        User user = (User) session.get(User.class, req.getInt("user-id"));
        Group group = new Group();
        group.setName(req.get("name"));
        group.getUsers().add(user);
        session.save(group);
        return new Packet(StatusCode.OK);
    }
}
