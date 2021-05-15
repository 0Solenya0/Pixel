package controller;

import controller.Controller;
import db.dbSet.RelationDBSet;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import model.Relation;
import model.User;
import model.field.AccessLevel;
import model.field.RelStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Predicate;

public class AuthController extends Controller {

    public User getByAccess(User request, int id) throws ConnectionException {
        User user = context.users.get(id);
        RelationDBSet relationDBSet = new RelationDBSet();
        Relation relation = relationDBSet.getFirst(
                relationDBSet.getQueryBuilder()
                        .getByTwoUser(request.id, id).getQuery()
        );
        AccessLevel level = AccessLevel.PUBLIC;
        if (relation != null && relation.getType() == RelStatus.FOLLOW)
            level = AccessLevel.CONTACTS;
        if (request.id == id)
            level = AccessLevel.PRIVATE;
        if (level != AccessLevel.PRIVATE) {
            if (user.getPhone().getAccessLevel() == AccessLevel.PRIVATE
                    || (user.getPhone().getAccessLevel() == AccessLevel.CONTACTS && level != AccessLevel.CONTACTS))
                user.getPhone().set("");
            if (user.getMail().getAccessLevel() == AccessLevel.PRIVATE
                    || (user.getMail().getAccessLevel() == AccessLevel.CONTACTS && level != AccessLevel.CONTACTS))
                user.getMail().set("");
            if (user.getBirthdate().getAccessLevel() == AccessLevel.PRIVATE
                    || (user.getBirthdate().getAccessLevel() == AccessLevel.CONTACTS && level != AccessLevel.CONTACTS))
                user.getBirthdate().set(LocalDate.MIN);
            if (user.getLastseen().getAccessLevel() == AccessLevel.PRIVATE
                    || (user.getLastseen().getAccessLevel() == AccessLevel.CONTACTS && level != AccessLevel.CONTACTS))
                user.getLastseen().set(LocalDateTime.MIN);
        }
        return user;
    }

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
