package bots.bot.store;

import client.store.Store;
import shared.models.User;

import java.time.LocalDateTime;
import java.util.HashSet;

public abstract class BotStore extends Store {
    private LocalDateTime lastFetch = null;
    private HashSet<Integer> answeredMessages = new HashSet<>();
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
