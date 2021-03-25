package Server.models;

import Server.models.Exceptions.ConnectionException;
import Server.models.Exceptions.ValidationException;
import Server.models.GsonAdapters.LocalDateAdapter;
import Server.models.GsonAdapters.LocalDateTimeAdapter;
import com.google.gson.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TreeSet;

public abstract class Model {
    private static final Logger logger = LogManager.getLogger(Model.class);
    public static String getDataSourceByClass(Class<? extends Model> x) {
        return "./db/" + x.getName();
    }

    public int id;
    public boolean isDeleted;
    public LocalDateTime createdAt;
    public LocalDateTime lastModified;

    public String getDataSource() {
        return getDataSourceByClass(this.getClass());
    }
    public static int getLastId(Class<? extends Model> x) {
        File path = new File(getDataSourceByClass(x) + "/");
        path.mkdirs();
        return path.list().length;
    }
    public Model() {
        isDeleted = false;
    }

    public void save() throws ValidationException, ConnectionException {
        if (!isDeleted)
            isValid();
        if (id == 0) {
            id = getLastId(this.getClass()) + 1;
            createdAt = LocalDateTime.now();
        }
        lastModified = LocalDateTime.now();

        File path = new File(getDataSource() + "/" + id + ".json");
        if (path.exists())
            path.delete();

        try {
            FileWriter writer = new FileWriter(getDataSource() + "/" + id + ".json");
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            gson.toJson(this, writer);
            writer.flush();
        }
        catch (IOException e) {
            logger.error("Database Record File Was Not Created - " + e.getMessage());
            throw new ConnectionException();
        }

        logger.info(String.format("An instance of %s with id %s got saved.", this.getClass(), this.id));
    }
    public void delete() throws ConnectionException {
        if (this.id == 0)
            return;
        isDeleted = true;
        try {
            save();
            logger.info(String.format("An instance of %s with id %s got deleted", this.getClass(), this.id));
        }
        catch (ValidationException e) {
            logger.warn("Delete failed - " + e.getMessage());
        }
    }
    public void isValid() throws ValidationException, ConnectionException { }

    public static Model loadObj(int id, Class<? extends Model> x) throws ConnectionException {
        try {
            FileReader file = new FileReader(getDataSourceByClass(x) + "/" + id + ".json");
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            return gson.fromJson(file, x);
        }
        catch (IOException e) {
            logger.error("Can't Read Database Record File - " + e.getMessage());
            throw new ConnectionException();
        }
    }
}