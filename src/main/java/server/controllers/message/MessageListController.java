package server.controllers.message;

import server.controllers.Controller;
import shared.models.Message;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static server.utils.Functions.notifyRefreshMessage;

public class MessageListController extends Controller {

    @SuppressWarnings("unchecked")
    public Packet respond(Packet req) {
        Packet res = new Packet(StatusCode.OK);
        User user = (User) session.get(User.class, req.getInt("user-id"));
        LocalDateTime after = req.getObject("after", LocalDateTime.class, LocalDateTime.MIN);
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
        messages.removeIf((m) -> m.getSchedule().isAfter(LocalDateTime.now()) || m.getSchedule().isBefore(after));
        for (Message message: messages) {
            if (!message.isDelivered() && user.id != message.getSender().id) {
                message.setDelivered(true);
                session.save(message);
                if (message.getReceiver() != null) {
                    notifyRefreshMessage(message.getReceiver());
                    notifyRefreshMessage(message.getSender());
                } else
                    notifyRefreshMessage(message.getReceiverGroup().getUsers());
            }
        }
        res.putObject("list", messages);
        session.close();
        return res;
    }
}
