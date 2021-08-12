package server.controllers.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import server.controllers.Controller;
import shared.models.Message;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

import java.util.ArrayList;

import static server.utils.Functions.notifyRefreshMessage;

public class MessageActionController extends Controller {
    private static final Logger logger = LogManager.getLogger(MessageActionController.class);

    @Override
    public Packet respond(Packet req) {
        User user = (User) session.get(User.class, req.getInt("user-id"));
        Message message = (Message) session.get(Message.class, req.getInt("message-id"));
        switch (req.get("type")) {
            case "delete":
                session.delete(message);
                notifyUser(message);
                logger.info("message " + message.id + " was deleted");
                break;
            case "edit":
                message.setContent(req.get("content"));
                session.save(message);
                notifyUser(message);
                logger.info("message " + message.id + " was edited");
                break;
            case "see":
                if (!message.isSeen() && user.id != message.getSender().id) {
                    message.setSeen(true);
                    session.save(message);
                    notifyUser(message);
                }
                logger.info("message " + message.id + " was seen by " + user.getUsername());
                break;
        }

        return new Packet(StatusCode.OK);
    }

    public void notifyUser(Message message) {
        if (message.getReceiver() != null) {
            notifyRefreshMessage(message.getReceiver());
            notifyRefreshMessage(message.getSender());
        }
        else
            notifyRefreshMessage(message.getReceiverGroup().getUsers());
    }
}
