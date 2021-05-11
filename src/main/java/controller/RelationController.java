package controller;

import db.dbSet.UserDBSet;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import model.Notification;
import model.Relation;
import model.User;
import model.field.AccessLevel;
import model.field.NotificationType;
import model.field.RelStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class RelationController extends Controller {
    private static final Logger logger = LogManager.getLogger(RelationController.class);
    public ArrayList<User> getFollowers(User user) throws ConnectionException {
        ArrayList<Relation> rel = context.relations.getAll(
                context.relations.getQueryBuilder()
                .getByUser2(user).getByType(RelStatus.FOLLOW).getEnabled().getQuery());
        ArrayList<User> res = new ArrayList<>();
        for (Relation relation : rel)
            res.add(context.users.get(relation.getSender()));
        return res;
    }

    public ArrayList<User> getFollowing(User user) throws ConnectionException {
        UserDBSet userDBSet = new UserDBSet();
        ArrayList<Relation> rel = context.relations.getAll(
                context.relations.getQueryBuilder()
                .getByUser1(user).getByType(RelStatus.FOLLOW).getEnabled().getQuery());
        ArrayList<User> res = new ArrayList<>();
        for (Relation relation : rel)
            res.add(userDBSet.get(relation.getReceiver()));
        return res;
    }

    public ArrayList<User> getBlackList(User user) throws ConnectionException {
        ArrayList<Relation> rel = context.relations.getAll(context.relations.getQueryBuilder()
                .getByUser1(user).getByType(RelStatus.BLOCKED).getEnabled().getQuery());
        ArrayList<User> res = new ArrayList<>();
        for (Relation relation : rel)
            res.add(context.users.get(relation.getReceiver()));
        return res;
    }

    public void follow(User user, User user2) throws ConnectionException {
        Relation relation = context.relations.getFirst(
                context.relations.getQueryBuilder().getByTwoUser(user.id, user2.id).getQuery());
        try {
            if (relation != null && relation.getType() == RelStatus.FOLLOW)
                return;
            resetRel(user, user2);
            if (user2.getVisibility() == AccessLevel.PRIVATE) {
                Notification notification = new Notification(user.id, user2.id, NotificationType.REQUEST);
                context.notifications.save(notification);
            }
            else {
                relation = new Relation(user, user2, RelStatus.FOLLOW);
                context.relations.save(relation);
                Notification notification1 = new Notification(0, user2.id,
                        user.getUsername() + " has started following you");
                context.notifications.save(notification1);
            }
        }
        catch (ValidationException e) {
            logger.error("follow user failed by unexpected validation error");
            logger.error(e.getLog());
        }
    }

    public void block(User user, User user2) throws ConnectionException {
        Relation relation2 = context.relations.getFirst(
                context.relations.getQueryBuilder().getByTwoUser(user2.id, user.id).getQuery());
        try {
            if (relation2 != null && relation2.getType() == RelStatus.FOLLOW)
                resetRel(user2, user);
            if (relation2 != null && relation2.getType() == RelStatus.BLOCKED)
                return;
            resetRel(user, user2);
            Relation relation = new Relation(user, user2, RelStatus.BLOCKED);
            context.relations.save(relation);
            Notification tmp = context.notifications.getFirst(
                    context.notifications.getQueryBuilder()
                            .getByTwoUser(user, user2)
                            .getByType(NotificationType.REQUEST)
                            .getQuery()
            );
            if (tmp != null)
                context.notifications.delete(tmp);
            tmp = context.notifications.getFirst(
                    context.notifications.getQueryBuilder()
                            .getByTwoUser(user2, user)
                            .getByType(NotificationType.REQUEST)
                            .getQuery()
            );
            if (tmp != null)
                context.notifications.delete(tmp);
        }
        catch (ValidationException e) {
            logger.error("block user failed by unexpected validation error");
            logger.error(e.getLog());
        }
    }

    public void resetRel(User user, User user2) throws ConnectionException {
        Relation relation = context.relations.getFirst(
                context.relations.getQueryBuilder().getByTwoUser(user.id, user2.id).getQuery());
        try {
            if (relation != null) {
                if (relation.getType() == RelStatus.FOLLOW) {
                    Notification notification2 = new Notification(0, user2.id,
                            user.getUsername() + " has stopped following you");
                    context.notifications.save(notification2);
                }
                context.relations.delete(relation);
            }
        }
        catch (ValidationException e) {
            logger.error("reset relation failed by unexpected validation error");
            logger.error(e.getLog());
        }
    }
}
