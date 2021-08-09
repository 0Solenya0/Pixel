package server.controllers.message;

import server.controllers.Controller;
import server.db.HibernateUtil;
import shared.models.Message;
import shared.request.Packet;
import shared.request.StatusCode;

import java.time.LocalDateTime;

public class SendMessageController extends Controller {

    public Packet respond(Packet req) {
        Packet packet = new Packet(StatusCode.CREATED);
        Message message = req.getObject("message", Message.class);
        session.refresh(message.getReceiver());
        session.refresh(message.getSender());
        session.refresh(message.getReceiverGroup());
        if (message.getSchedule() == null)
            message.setSchedule(LocalDateTime.now());
        session.save(message);
        // TO DO notify user
        // TO DO validate access
        return packet;
    }
}
