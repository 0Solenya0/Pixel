package server.controllers.message;

import server.controllers.Controller;
import shared.models.Group;
import shared.request.Packet;
import shared.request.StatusCode;

public class GetGroupController extends Controller {
    @Override
    public Packet respond(Packet req) {
        Packet packet = new Packet(StatusCode.OK);
        Group group = (Group) session.get(Group.class, req.getInt("group-id"));
        packet.putObject("group", group);
        session.close();
        return packet;
    }
}
