package controller;

import controller.Controller;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import model.User;

import java.util.function.Predicate;

public class AuthController extends Controller {

    public int checkLogin(String username, String password) throws ConnectionException {
        Predicate<User> q = context.users.getQueryBuilder()
                .getByUsername(username).getQuery();
        User user = context.users.getFirst(q);
        if (user != null && user.checkPassword(password))
            return user.id;
        return 0;
    }

    public void register(User user) throws ConnectionException, ValidationException {
        context.users.save(user);
    }
}
