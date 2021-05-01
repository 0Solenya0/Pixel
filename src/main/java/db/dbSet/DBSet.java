package db.dbSet;

import db.exception.ValidationException;
import db.gsonAdapter.LocalDateAdapter;
import db.gsonAdapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import db.exception.ConnectionException;
import db.queryBuilder.QueryBuilder;
import listener.ListenableClass;
import model.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.function.Predicate;

public abstract class DBSet<T extends Model> implements ListenableClass {
    private static final Logger logger = LogManager.getLogger(DBSet.class);
    Class<T> modelClass;

    public DBSet(Class<T> modelClass) {
        this.modelClass = modelClass;
    }

    private String getDataSource() {
        return "./db/" + modelClass.getName();
    }

    private int getLastId() {
        File path = new File(getDataSource() + "/");
        path.mkdirs();
        return path.list().length;
    }

    public T get(int id) throws ConnectionException {
        try {
            FileReader file = new FileReader(getDataSource() + "/" + id + ".json");
            logger.info("Open File - " + file);
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            return gson.fromJson(file, modelClass);
        }
        catch (IOException e) {
            logger.error("Can't Read Database Record File - " + e.getMessage());
            throw new ConnectionException();
        }
    }

    public ArrayList<T> getAll(Predicate<T> query) throws ConnectionException {
        ArrayList<T> res = new ArrayList<>();
        for (int i = 1; i <= getLastId(); i++)
            if (query.test(get(i)))
                res.add(get(i));
        return res;
    }

    public T getFirst(Predicate<T> query) throws ConnectionException {
        for (int i = 1; i <= getLastId(); i++)
            if (query.test(get(i)))
                return get(i);
        return null;
    }

    public void delete(T model) throws ConnectionException {
        model.isDeleted = true;
        try {
            save(model);
        }
        catch (ValidationException exception) {
            logger.error("Unexpected validation error while deleting a model");
            return;
        }
        logger.info("Model successfully deleted - " + modelClass.getName() + " - id: " + model.id);
    }

    public T save(T model) throws ConnectionException, ValidationException {
        logger.info(String.format("Start saving model to database - An instance of %s with id %s is getting saved.", model.getClass(), model.id));

        if (!model.isDeleted)
            validate(model);

        if (model.id == 0) {
            model.id = getLastId() + 1;
            model.createdAt = LocalDateTime.now();
        }
        model.lastModified = LocalDateTime.now();

        File path = new File(getDataSource() + "/" + model.id + ".json");
        if (path.exists())
            path.delete();

        try {
            FileWriter writer = new FileWriter(getDataSource() + "/" + model.id + ".json");
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            gson.toJson(model, writer);
            writer.flush();
        }
        catch (IOException e) {
            logger.error("Database Record File Was Not Saved - " + e.getMessage());
            throw new ConnectionException();
        }

        logger.info(String.format("An instance of %s with id %s got saved.", modelClass.getClass(), model.id));
        return model;
    }

    public abstract void validate(T model) throws ConnectionException, ValidationException;

    public QueryBuilder<T> getQueryBuilder() {
        return new QueryBuilder<T>();
    }
}
