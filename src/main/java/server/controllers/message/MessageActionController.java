package server.controllers.message;

import server.controllers.Controller;
import shared.models.Message;
import shared.request.Packet;
import shared.request.StatusCode;

public class MessageActionController extends Controller {
    @Override
    public Packet respond(Packet req) {
        Message message = (Message) session.get(Message.class, req.getInt("message-id"));
        switch (req.get("type")) {
            case "delete":
                session.delete(message);
                break;
            case "edit":
                message.setContent(req.get("content"));
                session.save(message);
                break;
        }
        // TO DO notify user
        return new Packet(StatusCode.OK);
    }
}
