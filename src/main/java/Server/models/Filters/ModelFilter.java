package Server.models.Filters;

import Server.models.Exceptions.ConnectionException;
import Server.models.Model;
import Server.models.User;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class ModelFilter<T extends Model> {
    private ArrayList<T> list;
    private Class<T> cls;

    public ModelFilter(Class<T> x) throws ConnectionException {
        this.cls = x;
        list = new ArrayList<>();
        for (int i = 1; i <= Model.getLastId(x); i++)
            if (!Model.loadObj(i, x).isDeleted)
                list.add(cls.cast(Model.loadObj(i, x)));
    }
    /** filters the list **/
    public ModelFilter<T> customFilter(Predicate<T> p) {
        list = (ArrayList<T>) list.stream().filter(p).collect(Collectors.toList());
        return this;
    }
    /** gets a  single element **/
    public T get(Predicate<T> p) {
        customFilter(p);
        if (!list.isEmpty())
            return list.get(0);
        return null;
    }

    public ArrayList<T> getList() {
        return list;
    }
}