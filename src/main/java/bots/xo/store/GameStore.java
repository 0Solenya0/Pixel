package bots.xo.store;

import bots.bot.store.BotStore;
import bots.xo.models.Game;

import java.util.HashMap;

public class GameStore extends BotStore {
    private static GameStore instance;
    private HashMap<Integer, Game> games = new HashMap<>();

    public synchronized static GameStore getInstance() {
        if (instance == null)
            instance = new GameStore();
        return instance;
    }

    public synchronized static void setInstance(GameStore instance1) {
        instance = instance1;
    }

    public synchronized void addGame(int mId, Game game) {
        games.put(mId, game);
    }

    public synchronized Game getGame(int mId) {
        return games.get(mId);
    }

    @Override
    public String getDataSource() {
        return "./db/bots/xo-game/data.json";
    }
}
