package Server.models.Filters;

import Server.models.Exceptions.ConnectionException;
import Server.models.Tweet;
import Server.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TweetFilter {
    private ArrayList<Tweet> list;
    public TweetFilter() throws ConnectionException {
        list = new ArrayList<>();
        for (int i = 1; i <= Tweet.getLastId(Tweet.datasrc); i++)
            if (!Tweet.get(i).isDeleted)
                list.add(Tweet.get(i));
    }
    public TweetFilter userCustomFilter(Predicate<Tweet> p) {
        list = (ArrayList<Tweet>) list.stream().filter(p).collect(Collectors.toList());
        return this;
    }
    public TweetFilter getByUser(String username) {
        userCustomFilter(tweet -> {
            try {
                return tweet.getAuthor().username.equals(username);
            } catch (Exception e) {
                return false;
            }
        });
        return this;
    }

    public ArrayList<Tweet> getList() {
        return list;
    }
}
