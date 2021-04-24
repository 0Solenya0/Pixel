package db;

import db.gsonAdapter.LocalDateAdapter;
import db.gsonAdapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import db.exception.ConnectionException;
import model.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class DBSet<T extends Model> {
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

    public void delete(T model) throws ConnectionException {
        model.isDeleted = true;
        save(model);
        logger.info("Model successfully deleted - " + modelClass.getName() + " - id: " + model.id);
    }

    public T save(T model) throws ConnectionException {
        logger.info(String.format("Start saving model to database - An instance of %s with id %s is getting saved.", model.getClass(), model.id));

        if (!model.isDeleted)
            model.validate();

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
            gson.toJson(this, writer);
            writer.flush();
        }
        catch (IOException e) {
            logger.error("Database Record File Was Not Saved - " + e.getMessage());
            throw new ConnectionException();
        }

        logger.info(String.format("An instance of %s with id %s got saved.", modelClass.getClass(), model.id));
        return model;
    }
}
