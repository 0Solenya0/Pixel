package server.controllers.message;

import server.controllers.Controller;
import shared.models.Message;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

import java.util.ArrayList;

public class MessageListController extends Controller {

    @SuppressWarnings("unchecked")
    public Packet respond(Packet req) {
        Packet res = new Packet(StatusCode.OK);
        User user = (User) session.get(User.class, req.getInt("user-id"));
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
        for (Message message: messages) {
            if (!message.delivers.contains(user)) {
                message.delivers.add(user);
                session.save(message);
            }
        }
        res.putObject("list", messages);
        return res;
    }
}
