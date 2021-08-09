package server.controllers.message;

import server.controllers.Controller;
import shared.models.Message;
import shared.request.Packet;
import shared.request.StatusCode;

import java.util.ArrayList;

public class MessageListController extends Controller {

    @SuppressWarnings("unchecked")
    public Packet respond(Packet req) {
        Packet res = new Packet(StatusCode.OK);
        ArrayList<Message> messages = (ArrayList<Message>) session.getInnerSession().createQuery(
                "SELECT message FROM Message AS message " +
                        "WHERE message.receiver.id = :u \n" +
                        "OR message.sender.id = :u"
        ).setParameter("u", req.getInt("user-id")).list();
        messages.addAll(session.getInnerSession().createQuery(
                "SELECT message FROM Message AS message " +
                        "JOIN message.receiverGroup as g \n" +
                        "LEFT JOIN g.users as us \n" +
                        "WHERE us.id = :u AND message.sender != :u"
        ).setParameter("u", req.getInt("user-id")).list());
        res.putObject("list", messages);
        return res;
    }
}
