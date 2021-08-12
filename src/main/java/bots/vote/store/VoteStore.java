package bots.vote.store;

import bots.BotStore;
import bots.vote.models.Vote;
import client.store.Store;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;

public class VoteStore extends BotStore {
    private static VoteStore instance;
    private HashMap<Integer, Vote> votes = new HashMap<>();

    public synchronized static VoteStore getInstance() {
        if (instance == null)
            instance = new VoteStore();
        return instance;
    }

    public synchronized static void setInstance(VoteStore instance1) {
        instance = instance1;
    }

    public synchronized void addVote(int mId, Vote vote) {
        votes.put(mId, vote);
    }

    public synchronized Vote getVote(int mId) {
        return votes.get(mId);
    }

    @Override
    public String getDataSource() {
        return "./db/bots/vote/data.json";
    }
}
