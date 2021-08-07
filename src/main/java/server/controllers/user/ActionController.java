package server.controllers.user;

import server.controllers.Controller;
import server.db.HibernateUtil;
import server.db.fields.RequestState;
import server.db.models.FollowRequest;
import shared.models.Notification;
import shared.models.User;
import shared.models.fields.AccessLevel;
import shared.models.fields.NotificationType;
import shared.request.Packet;
import shared.request.StatusCode;

public class ActionController extends Controller {

    public Packet respond(Packet req) {
        Packet res = new Packet(StatusCode.OK);
        int t = req.getInt("target-id");
        User target = (User) session.get(User.class, t);
        User user = (User) session.get(User.class, req.getInt("user-id"));
        switch (req.get("type")) {
            case "toggle-block":
                if (user.blocked.contains(target))
                    user.blocked.remove(target);
                else
                    user.blocked.add(target);
                session.save(user);
                break;
            case "toggle-mute":
                if (user.muted.contains(target))
                    user.muted.remove(target);
                else
                    user.muted.add(target);
                session.save(user);
                break;
            case "toggle-follow":
                FollowRequest request = getRequest(user, target);
                if (request != null) {
                    request.setState(RequestState.CANCELED);
                    session.save(request);
                    break;
                }
                if (user.followings.contains(target))
                    unFollow(user, target);
                else
                    follow(user, target);
                break;
            case "report":
                Notification notification = new Notification();
                notification.setReceiver(target);
                notification.setMessage("You have been reported.");
                notification.setType(NotificationType.REPORT);
                session.save(notification);
                break;
        }
        return res;
    }

    public void unFollow(User user, User target) {
        user.followings.remove(target);
        session.save(user);
        sendUnFollowNotification(user, target);
    }

    public void sendFollowNotification(User user, User target) {
        Notification notification = new Notification();
        notification.setSender(user);
        notification.setReceiver(target);
        notification.setMessage(user.getUsername() + " has started following you!");
        notification.setType(NotificationType.INFO);
        session.save(notification);
    }

    public void sendUnFollowNotification(User user, User target) {
        Notification notification = new Notification();
        notification.setSender(user);
        notification.setReceiver(target);
        notification.setMessage(user.getUsername() + " has stopped following you!");
        notification.setType(NotificationType.INFO);
        session.save(notification);
    }

    public void follow(User user, User target) {
        if (target.getVisibility() == AccessLevel.PUBLIC) {
            user.followings.add(target);
            sendFollowNotification(user, target);
            return;
        }
        FollowRequest request = new FollowRequest();
        request.setSender(user);
        request.setReceiver(target);
        request.setState(RequestState.PENDING);
        session.save(request);
    }

    public FollowRequest getRequest(User sender, User receiver) {
        return (FollowRequest) session.getInnerSession()
                .createQuery(
                        "FROM FollowRequest as f " +
                                "WHERE f.sender.id = :sender " +
                                "AND f.receiver.id = :receiver " +
                                "AND f.state = :state"
                ).setParameter("sender", sender.id)
                .setParameter("receiver", receiver.id)
                .setParameter("state", RequestState.PENDING)
                .uniqueResult();
    }
}
