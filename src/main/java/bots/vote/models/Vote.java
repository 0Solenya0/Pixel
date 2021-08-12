package bots.vote.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Vote {
    private int voteId;
    private ArrayList<String> options = new ArrayList<>();
    private HashMap<Integer, Integer> votes = new HashMap<>();

    public ArrayList<String> getOptions() {
        return options;
    }

    public void addOption(String option) {
        options.add(option);
    }

    public int getVoteId() {
        return voteId;
    }

    public void setVote(int userId, int vote) {
        votes.put(userId, vote);
    }

    public long getVoteCount(int option) {
        return votes.values().stream().filter((x) -> x == option).count();
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }
}
