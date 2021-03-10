package Server.models.Filters;

import Server.models.Relation;
import Server.models.Tweet;
import Server.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RelationFilter {
    private ArrayList<Relation> list;
    public RelationFilter() throws Exception {
        list = new ArrayList<>();
        for (int i = 1; i <= Relation.getLastId(Relation.datasrc); i++)
            if (Relation.get(i).isActive)
                list.add(Relation.get(i));
    }
    public RelationFilter userCustomFilter(Predicate<Relation> p) {
        list = (ArrayList<Relation>) list.stream().filter(p).collect(Collectors.toList());
        return this;
    }
    public Relation getByTwoUser(int user1, int user2) {
        userCustomFilter(relation -> relation.user1 == user1 && relation.user2 == user2);
        if (list.isEmpty())
            return null;
        return list.get(0);
    }
    public RelationFilter getByUser(int user1) {
        userCustomFilter(relation -> relation.user1 == user1);
        return this;
    }

    public ArrayList<Relation> getList() {
        return list;
    }
}
