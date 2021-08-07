package server.controllers.notification;

import server.controllers.Controller;
import server.db.fields.RequestState;
import server.db.models.FollowRequest;
import shared.models.Notification;
import shared.models.User;
import shared.models.fields.NotificationType;
import shared.request.Packet;
import shared.request.StatusCode;

public class ActionController extends Controller {

    public Packet respond(Packet req) {
        Packet res = new Packet(StatusCode.OK);
        User user = (User) session.get(User.class, req.getInt("user-id"));
        FollowRequest request = (FollowRequest) session.get(FollowRequest.class, req.getInt("req-id"));
        if (request.getState() != RequestState.PENDING)
            return res;
        switch (req.get("type")) {
            case "accept":
                request.setState(RequestState.ACCEPTED);
                user.followers.add(request.getSender());
                session.save(user);
                session.save(request);
                break;
            case "reject":
                request.setState(RequestState.REJECTED);
                sendRejectNotification(request.getReceiver(), request.getSender());
                session.save(request);
                break;
            case "silent-reject":
                request.setState(RequestState.REJECTED);
                session.save(request);
                break;
        }
        return res;
    }

    public void sendRejectNotification(User user, User target) {
        Notification notification = new Notification();
        notification.setSender(user);
        notification.setReceiver(target);
        notification.setType(NotificationType.INFO);
        notification.setMessage(user.getUsername() + " rejected your follow request");
        session.save(notification);
    }
}
