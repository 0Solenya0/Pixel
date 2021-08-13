package server.controllers.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import server.controllers.Controller;
import server.db.HibernateUtil;
import shared.models.Message;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static server.utils.Functions.notifyRefreshMessage;

public class SendMessageController extends Controller {
    private static final Logger logger = LogManager.getLogger(SendMessageController.class);

    public Packet respond(Packet req) {
        Packet packet = new Packet(StatusCode.CREATED);
        Message message = req.getObject("message", Message.class);
        session.refresh(message.getReceiver());
        session.refresh(message.getSender());
        session.refresh(message.getReceiverGroup());
        if (message.getSchedule() == null || message.getSchedule().isBefore(LocalDateTime.now()))
            message.setSchedule(LocalDateTime.now());
        session.save(message);
        logger.info("new message " + message.id + " has been sent");
        Thread thread = new Thread(() -> {
            if (message.getReceiver() != null) {
                notifyRefreshMessage(message.getReceiver());
                notifyRefreshMessage(message.getSender());
            } else
                notifyRefreshMessage(message.getReceiverGroup().getUsers());
            session.close();
        });
        thread.start();
        // TO DO validate access
        return packet.put("id", message.id);
    }
}
