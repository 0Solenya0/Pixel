package server.controllers.notification;

import server.controllers.Controller;
import server.db.fields.RequestState;
import server.db.models.FollowRequest;
import shared.models.Model;
import shared.models.Notification;
import shared.models.User;
import shared.models.fields.NotificationType;
import shared.request.Packet;
import shared.request.StatusCode;

import java.util.ArrayList;
import java.util.Comparator;

public class NotificationListController extends Controller {

    @SuppressWarnings("unchecked")
    public Packet respond(Packet req) {
        Packet res = new Packet(StatusCode.OK);
        ArrayList<Notification> notifications =
                (ArrayList<Notification>) session.getInnerSession().createQuery(
                        "FROM Notification as notification " +
                                "WHERE notification.receiver.id = :receiver " +
                                "AND notification.visible = :vis"
                ).setParameter("vis", true)
                .setParameter("receiver", req.getInt("user-id")).list();
        ArrayList<FollowRequest> requests =
                (ArrayList<FollowRequest>) session.getInnerSession().createQuery(
                        "FROM FollowRequest as req " +
                                "WHERE req.receiver.id = :receiver " +
                                "AND req.state = :state"
                ).setParameter("receiver", req.getInt("user-id"))
                .setParameter("state", RequestState.PENDING).list();
        for (FollowRequest request: requests) {
            Notification notification = new Notification();
            notification.setSender(request.getSender());
            notification.setReceiver(request.getReceiver());
            notification.setMessage(request.getSender().getUsername() + " has requested to follow you");
            notification.setType(NotificationType.REQUEST);
            notification.setCreatedAt(request.getCreatedAt());
            notifications.add(notification);
        }
        notifications.sort(Comparator.comparing(Model::getCreatedAt).reversed());
        res.putObject("notifications", notifications);
        return res;
    }
}
