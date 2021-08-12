package bots.calculator.store;

import client.store.Store;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

public class BotStore extends Store {
    private static BotStore instance;
    private LocalDateTime lastFetch = null;
    private HashSet<Integer> answeredMessages = new HashSet<>();

    public synchronized static BotStore getInstance() {
        if (instance == null)
            instance = new BotStore();
        return instance;
    }

    public synchronized static void setInstance(BotStore instance1) {
        instance = instance1;
    }

    public synchronized LocalDateTime getLastFetch() {
        return lastFetch;
    }

    public synchronized void setLastFetch(LocalDateTime lastFetch) {
        this.lastFetch = lastFetch;
    }

    public synchronized boolean hasAnswered(int message) {
        return answeredMessages.contains(message);
    }

    public synchronized void answer(int message) {
        this.answeredMessages.add(message);
    }

    public synchronized void unAnswer(int message) {
        this.answeredMessages.remove(message);
    }

    @Override
    public String getDataSource() {
        return "./db/bots/calculator/data.json";
    }
}
