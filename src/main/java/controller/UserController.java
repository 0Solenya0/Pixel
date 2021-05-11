package controller;

import db.dbSet.UserDBSet;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
}
