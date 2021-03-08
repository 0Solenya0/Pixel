package Server.models.Filters;

import Server.models.Tweet;
import Server.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TweetFilter {
    private ArrayList<Tweet> list;
    public TweetFilter() throws IOException {
        for (int i = 1; i <= Tweet.getLastId(Tweet.datasrc); i++)
            list.add(Tweet.get(i));
    }
    public TweetFilter userCustomFilter(Predicate<Tweet> p) {
        list.stream().filter(p).collect(Collectors.toList());
        return this;
    }

}
