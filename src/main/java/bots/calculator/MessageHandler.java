package bots.calculator;

import bots.calculator.store.BotStore;
import client.request.SocketHandler;
import client.store.MessageStore;
import client.store.MyProfileStore;
import com.google.gson.reflect.TypeToken;
import org.mariuszgromada.math.mxparser.Expression;
import shared.models.Message;
import shared.request.Packet;
import shared.request.StatusCode;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageHandler {
    ExecutorService pool = Executors.newFixedThreadPool(10);

    public MessageHandler() {
        MessageStore.getInstance().setOnDataRefreshListener(this::getNewMessages);
    }

    public void getNewMessages() {
        Packet packet = new Packet("message-list");
        LocalDateTime lastFetch = BotStore.getInstance().getLastFetch();
        if (lastFetch != null)
            packet.putObject("after", lastFetch.minusMinutes(1));
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        if (res.getStatus() != StatusCode.OK)
            return;

        BotStore.getInstance().setLastFetch(LocalDateTime.now());
        Type listType = new TypeToken<ArrayList<Message>>(){}.getType();
        answerMessages(res.getObject("list", listType));
    }

    public synchronized void answerMessages(ArrayList<Message> messages) {
        for (Message message: messages) {
            if (message.getSender().id == MyProfileStore.getInstance().getUser().id)
                continue;
            if (!message.getContent().startsWith("/calc") || message.getContent().length() <= 6)
                continue;
            if (BotStore.getInstance().hasAnswered(message.id))
                continue;
            message.setContent(message.getContent().substring(6));
            BotStore.getInstance().answer(message.id);
            pool.execute(() -> answerMessage(message));
        }
    }

    public void answerMessage(Message req) {
        Message message = new Message();
        message.setSender(MyProfileStore.getInstance().getUser());
        if (message.getReceiverGroup() == null)
            message.setReceiver(req.getSender());
        else
            message.setReceiverGroup(message.getReceiverGroup());

        try {
            Expression expression = new Expression(req.getContent());
            message.setContent(req.getContent() + " = " + expression.calculate());
            System.out.println(message.getContent());
        }
        catch (Exception e) {
            message.setContent(req.getContent() + "\n is an invalid expression");
            System.out.println("Invalid");
        }

        Packet packet = new Packet("send-message");
        packet.putObject("message", message);
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        if (res.getStatus() != StatusCode.CREATED)
            BotStore.getInstance().unAnswer(req.id);
        BotStore.getInstance().save();
    }
}
