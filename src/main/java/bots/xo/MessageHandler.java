package bots.xo;

import bots.xo.models.Game;
import bots.xo.store.GameStore;
import com.google.gson.reflect.TypeToken;
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

    public void getNewMessages() {
        Packet packet = new Packet("message-list");
        LocalDateTime lastFetch = GameStore.getInstance().getLastFetch();
        if (lastFetch != null)
            packet.putObject("after", lastFetch.minusMinutes(1));
        Packet res = PacketManager.getInstance().sendAndGetResponse(packet);
        if (res.getStatus() != StatusCode.OK)
            return;

        GameStore.getInstance().setLastFetch(LocalDateTime.now());
        Type listType = new TypeToken<ArrayList<Message>>(){}.getType();
        answerMessages(res.getObject("list", listType));
    }

    public synchronized void answerMessages(ArrayList<Message> messages) {
        for (Message message: messages) {
            if (message.getSender().id == GameStore.getInstance().getUser().id)
                continue;
            if (!message.getContent().startsWith("/xo") || message.getContent().length() <= 4)
                continue;
            if (GameStore.getInstance().hasAnswered(message.id))
                continue;
            message.setContent(message.getContent().substring(4));
            GameStore.getInstance().answer(message.id);
            pool.execute(() -> answerMessage(message));
        }
    }

    public void answerMessage(Message req) {
        if (req.getContent().startsWith("create")) {
            req.setContent("");
            createGame(req);
        }
        else if (req.getContent().startsWith("play")) {
            req.setContent(req.getContent().substring(5));
            gameMove(req);
        }
        else
            gameJoin(req);
    }

    public void gameJoin(Message req) {
        Message message = new Message();
        message.setSender(GameStore.getInstance().getUser());
        if (req.getReceiverGroup() == null)
            message.setReceiver(req.getSender());
        else
            message.setReceiverGroup(req.getReceiverGroup());

        try {
            Scanner scanner = new Scanner(req.getContent());
            int gameId = scanner.nextInt();
            Game game = GameStore.getInstance().getGame(gameId);
            if (game.isStarted())
                return;
            game.setPlayer(2, req.getSender().id);

            Packet res = sendMessage(message);
            if (res.getStatus() != StatusCode.CREATED) {
                GameStore.getInstance().unAnswer(req.id);
                GameStore.getInstance().save();
                return;
            }
            int messageId = res.getInt("id");
            game.getMessageIds().add(messageId);

            for (int mId: game.getMessageIds())
                editMessage(mId, getGameString(game));
            GameStore.getInstance().save();
        }
        catch (Exception ignored) {}
    }

    public void gameMove(Message req) {
        Message message = new Message();
        message.setSender(GameStore.getInstance().getUser());
        if (req.getReceiverGroup() == null)
            message.setReceiver(req.getSender());
        else
            message.setReceiverGroup(req.getReceiverGroup());

        try {
            Scanner scanner = new Scanner(req.getContent());
            int gameId = scanner.nextInt();
            Game game = GameStore.getInstance().getGame(gameId);

            if (game.isFinished())
                return;

            if (game.getCurrentPlayerId() != req.getSender().id) {
                message.setContent("It's not your turn yet!");
                sendMessage(message);
                return;
            }

            int x = scanner.nextInt(), y = scanner.nextInt();
            if (!(x <= 3 && y <= 3 && x >= 1 && y >= 1))
                return;
            if (!game.playTurn(x - 1, y - 1)) {
                message.setContent("Cell " + x + " " + y + " is not empty");
                sendMessage(message);
                return;
            }

            for (int mId: game.getMessageIds())
                editMessage(mId, getGameString(game));
            GameStore.getInstance().save();
        }
        catch (Exception ignored) {}
    }

    public void createGame(Message req) {
        Message message = new Message();
        message.setSender(GameStore.getInstance().getUser());
        if (req.getReceiverGroup() == null)
            message.setReceiver(req.getSender());
        else
            message.setReceiverGroup(req.getReceiverGroup());

        Game game = new Game();
        Packet res = sendMessage(message);
        if (res.getStatus() != StatusCode.CREATED) {
            GameStore.getInstance().unAnswer(req.id);
            GameStore.getInstance().save();
            return;
        }
        int messageId = res.getInt("id");
        message.id = messageId;
        game.setGameId(messageId);
        game.setPlayer(1, req.getSender().id);
        game.getMessageIds().add(messageId);
        GameStore.getInstance().addGame(messageId, game);
        GameStore.getInstance().save();

        message.setContent(getGameString(game));
        editMessage(messageId, message.getContent());
    }

    public String getGameString(Game game) {
        if (game.isFinished() && game.getWinnerNumber() == 0)
            return "Game is finished, Tie";
        else if (game.isFinished())
            return "Game is finished, winner is player " + game.getWinnerNumber();
        if (!game.isStarted())
            return "Game ID: " + game.getGameId() + "\nWaiting for players";
        StringBuilder builder = new StringBuilder();
        builder.append("Game ID: ").append(game.getGameId()).append('\n');
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (game.getCell(i, j) == 0)
                    builder.append('-');
                else
                    builder.append(game.getCell(i, j) == 1 ? 'o' : 'x');
                builder.append(' ');
            }
            builder.append('\n');
        }
        builder.append("Player ").append(game.getCurrentPlayerNumber()).append(" turn");
        return builder.toString();
    }

    private Packet sendMessage(Message message) {
        Packet packet = new Packet("send-message");
        packet.putObject("message", message);
        return PacketManager.getInstance().sendAndGetResponse(packet);
    }

    private void editMessage(int messageId, String content) {
        Packet packet = new Packet("message-action");
        packet.put("type", "edit");
        packet.put("message-id", messageId);
        packet.put("content", content);
        PacketManager.getInstance().sendAndGetResponse(packet);
    }
}
