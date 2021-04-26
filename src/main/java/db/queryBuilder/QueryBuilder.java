package db.queryBuilder;

import db.Context;
import model.Model;

import java.util.function.Predicate;

public class QueryBuilder<T extends Model> {
    protected Context context = new Context();
    private Predicate<T> query = (T model) -> true;

    public QueryBuilder<T> addCustomFilter(Predicate<T> p) {
        query = query.and(p);
        return this;
    }

    public Predicate<T> getQuery() {
        return query;
    }
}
