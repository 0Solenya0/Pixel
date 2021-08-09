package server.controllers.message;

import server.controllers.Controller;
import shared.request.Packet;
import shared.request.StatusCode;

public class MessageListController extends Controller {

    public Packet respond(Packet req) {
        Packet res = new Packet(StatusCode.OK);
        res.putObject("list", session.getInnerSession().createQuery(
                "SELECT message FROM Message AS message " +
                        "WHERE message.receiver.id = :u \n" +
                        "OR message.sender.id = :u"
        ).setParameter("u", req.getInt("user-id")).list());
        // TO DO add group messages
        return res;
    }
}
