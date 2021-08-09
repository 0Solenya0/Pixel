package server.controllers.message;

import server.controllers.Controller;
import shared.request.Packet;
import shared.request.StatusCode;

public class MessageListController extends Controller {

    public Packet respond(Packet req) {
        Packet res = new Packet(StatusCode.OK);
        res.putObject("list", session.getInnerSession().createQuery(
                "SELECT message FROM Message AS message " +
                        "JOIN message.receiverGroup AS receiver \n" +
                        "LEFT JOIN receiver.users as user \n" +
                        "WHERE message.receiver.id = :u \n" +
                        "OR message.sender.id = :u \n" +
                        "OR user.id = :u \n" +
                        "ORDER BY message.schedule"
        ).setParameter("u", req.getInt("user-id")).list());
        return res;
    }
}
