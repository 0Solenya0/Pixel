package db.queryBuilder;

import model.Model;

import java.util.function.Predicate;

public class QueryBuilder<T extends Model> {
    private Predicate<T> query = (T model) -> true;

    public QueryBuilder<T> addCustomFilter(Predicate<T> p) {
        query = query.and(p);
        return this;
    }

    public Predicate<T> getQuery() {
        return query;
    }
}
