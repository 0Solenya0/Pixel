package controller;

import db.exception.ConnectionException;
import db.exception.ValidationException;
import model.ChatGroup;
import model.Message;
import model.Relation;
import model.User;
import model.field.RelStatus;

public class MessageController extends Controller {

    public boolean canMessage(User sender, User receiver) throws ConnectionException {
        receiver = context.users.get(receiver.id);
        if (!receiver.isEnabled())
            return false;
        if (sender.id == receiver.id)
            return true;
        Relation r1 = context.relations.getFirst(
                context.relations.getQueryBuilder()
                        .getByTwoUser(sender.id, receiver.id)
                        .getByType(RelStatus.FOLLOW).getQuery()
        );
        Relation r2 = context.relations.getFirst(
                context.relations.getQueryBuilder()
                        .getByTwoUser(receiver.id, sender.id)
                        .getByType(RelStatus.FOLLOW).getQuery()
        );
        return r1 != null || r2 != null;
    }

    public void sendMessage(User user, User user2, String content) throws ConnectionException, ValidationException {
        Message message = new Message(user.id, content);
        message.setReceiver(user2.id);
        context.messages.save(message);
    }

    public void sendMessage(User user, User user2, int tweetId) throws ConnectionException, ValidationException {
        Message message = new Message(user.id, tweetId);
        message.setReceiver(user2.id);
        context.messages.save(message);
    }

    public void sendMessage(User user, ChatGroup group, int tweetId) throws ConnectionException, ValidationException {
        Message message = new Message(user.id, tweetId);
        message.setChatGroup(group.id);
        context.messages.save(message);
    }

    public void sendMessage(User user, ChatGroup group, String content) throws ConnectionException, ValidationException {
        Message message = new Message(user.id, content);
        message.setChatGroup(group.id);
        context.messages.save(message);
    }
}
