package bots.vote;

import bots.vote.models.Vote;
import bots.vote.store.VoteStore;
import client.request.SocketHandler;
import client.store.MessageStore;
import client.store.MyProfileStore;
import com.google.gson.reflect.TypeToken;
import org.mariuszgromada.math.mxparser.Expression;
import shared.models.Message;
import shared.request.Packet;
import shared.request.StatusCode;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageHandler {
    ExecutorService pool = Executors.newFixedThreadPool(10);

    public MessageHandler() {
        MessageStore.getInstance().setOnDataRefreshListener(this::getNewMessages);
    }

    public void getNewMessages() {
        Packet packet = new Packet("message-list");
        LocalDateTime lastFetch = VoteStore.getInstance().getLastFetch();
        if (lastFetch != null)
            packet.putObject("after", lastFetch.minusMinutes(1));
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        if (res.getStatus() != StatusCode.OK)
            return;

        VoteStore.getInstance().setLastFetch(LocalDateTime.now());
        Type listType = new TypeToken<ArrayList<Message>>(){}.getType();
        answerMessages(res.getObject("list", listType));
    }

    public synchronized void answerMessages(ArrayList<Message> messages) {
        for (Message message: messages) {
            if (message.getSender().id == MyProfileStore.getInstance().getUser().id)
                continue;
            if (!message.getContent().startsWith("/vote") || message.getContent().length() <= 6)
                continue;
            if (VoteStore.getInstance().hasAnswered(message.id))
                continue;
            message.setContent(message.getContent().substring(6));
            VoteStore.getInstance().answer(message.id);
            pool.execute(() -> answerMessage(message));
        }
    }

    public void answerMessage(Message req) {
        if (req.getContent().startsWith("create")) {
            req.setContent(req.getContent().substring(7));
            createVote(req);
        }
        else
            vote(req);
    }

    public void vote(Message req) {
        try {
            Scanner scanner = new Scanner(req.getContent());
            int voteId = scanner.nextInt();
            Vote vote = VoteStore.getInstance().getVote(voteId);
            int option = scanner.nextInt();
            if (option > vote.getOptions().size())
                throw new Exception("");
            vote.setVote(req.getSender().id, option);
            editMessage(voteId, getVoteString(vote));
            VoteStore.getInstance().save();
        }
        catch (Exception ignored) {}
    }

    public void createVote(Message req) {
        Message message = new Message();
        message.setSender(MyProfileStore.getInstance().getUser());
        if (req.getReceiverGroup() == null)
            message.setReceiver(req.getSender());
        else
            message.setReceiverGroup(req.getReceiverGroup());

        Vote vote = new Vote();
        try {
            Scanner scanner = new Scanner(req.getContent());
            int cntOptions = Integer.parseInt(scanner.nextLine());
            if (cntOptions == 0)
                throw new Exception("");
            for (int i = 0; i < cntOptions; i++)
                vote.addOption(scanner.nextLine());
        }
        catch (Exception e) {
            message.setContent("Invalid request");
            Packet res = sendMessage(message);
            VoteStore.getInstance().unAnswer(req.id);
            VoteStore.getInstance().save();
            return;
        }

        Packet res = sendMessage(message);
        if (res.getStatus() != StatusCode.CREATED) {
            VoteStore.getInstance().unAnswer(req.id);
            VoteStore.getInstance().save();
            return;
        }
        int messageId = res.getInt("id");
        message.id = messageId;
        vote.setVoteId(messageId);
        VoteStore.getInstance().addVote(messageId, vote);
        VoteStore.getInstance().save();

        message.setContent(getVoteString(vote));
        editMessage(messageId, message.getContent());
    }

    public String getVoteString(Vote vote) {
        StringBuilder builder = new StringBuilder();
        builder.append("Vote ID: ").append(vote.getVoteId()).append(" | ");
        int i = 1;
        for (String option: vote.getOptions())
            builder.append(i++).append("- ").append(option).append(" (" + vote.getVoteCount(i - 1) + ")").append(" | ");
        return builder.toString();
    }

    private Packet sendMessage(Message message) {
        Packet packet = new Packet("send-message");
        packet.putObject("message", message);
        return SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
    }

    private void editMessage(int messageId, String content) {
        Packet packet = new Packet("message-action");
        packet.put("type", "edit");
        packet.put("message-id", messageId);
        packet.putObject("content", content);
        SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
    }
}
