package Server.models.Filters;

import Server.models.Relation;
import Server.models.Tweet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RelationFilter {
    private ArrayList<Relation> list;
    public RelationFilter() throws IOException {
        list = new ArrayList<>();
        for (int i = 1; i <= Tweet.getLastId(Tweet.datasrc); i++)
            list.add(Relation.get(i));
    }
    public RelationFilter userCustomFilter(Predicate<Relation> p) {
        list.stream().filter(p).collect(Collectors.toList());
        return this;
    }
    
}
