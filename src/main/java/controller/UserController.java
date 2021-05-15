package controller;

import db.dbSet.UserDBSet;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import model.Notification;
import model.Relation;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class UserController extends Controller {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    public void muteUser(User user, User user2) throws ConnectionException {
        user = context.users.get(user.id);
        user.muteUser(user2);
        try {
            context.users.save(user);
        }
        catch (ValidationException e) {
            logger.error("mute user failed by unexpected validation error");
            logger.error(e.getLog());
        }
    }

    public void unMuteUser(User user, User user2) throws ConnectionException {
        user = context.users.get(user.id);
        user.unMuteUser(user2);
        try {
            context.users.save(user);
        }
        catch (ValidationException e) {
            logger.error("mute user failed by unexpected validation error");
            logger.error(e.getLog());
        }
    }

    public void deleteAcc(User user) throws ConnectionException {
        context.users.delete(user);
        ArrayList<Relation> relations = new ArrayList<>();
        relations.addAll(context.relations.getAll(context.relations.getQueryBuilder().getByUser1(user).getQuery()));
        relations.addAll(context.relations.getAll(context.relations.getQueryBuilder().getByUser2(user).getQuery()));
        for (Relation rel : relations)
            context.relations.delete(rel);

        ArrayList<Notification> notifications = new ArrayList<>();
        notifications.addAll(context.notifications.getAll(context.notifications.getQueryBuilder().getByUser1(user).getQuery()));
        notifications.addAll(context.notifications.getAll(context.notifications.getQueryBuilder().getByUser2(user).getQuery()));
        for (Notification notification : notifications)
            context.notifications.delete(notification);
    }

    public void toggleDisableUser(User user) throws ConnectionException {
        user = context.users.get(user.id);
        user.setEnabled(!user.isEnabled());
        try {
            context.users.save(user);
        }
        catch (ValidationException e) {
            logger.error("disable/enable account failed unexpected validation error");
            logger.error(e.getLog());
        }
    }
}
