package controller;

import db.exception.ConnectionException;
import db.exception.ValidationException;
import model.ChatGroup;
import model.Message;
import model.Relation;
import model.User;
import model.field.RelStatus;

import java.util.ArrayList;

public class MessageController extends Controller {

    public Message getLastMessage(User user1, User user2) throws ConnectionException {
        ArrayList<Message> messages =  context.messages.getAll(
                context.messages.getQueryBuilder()
                .getTwoUserChats(user1.id, user2.id).getQuery()
        );
        if (messages.isEmpty())
            return null;
        else
            return messages.get(messages.size() - 1);
    }

    public Message getLastMessage(ChatGroup group) throws ConnectionException {
        ArrayList<Message> messages =  context.messages.getAll(
                context.messages.getQueryBuilder()
                        .getByGroup(group.id).getQuery()
        );
        if (messages.isEmpty())
            return null;
        else
            return messages.get(messages.size() - 1);
    }

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

    public void sendMessage(User user, User user2, String content, String photoId) throws ConnectionException, ValidationException {
        Message message = new Message(user.id, content);
        message.setReceiver(user2.id);
        message.setPhoto(photoId);
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

    public void sendMessage(User user, ChatGroup group, String content, String photoId) throws ConnectionException, ValidationException {
        Message message = new Message(user.id, content);
        message.setChatGroup(group.id);
        message.setPhoto(photoId);
        context.messages.save(message);
    }
}
