package Server.models;

import Server.models.Exceptions.ConnectionException;
import Server.models.Exceptions.ValidationException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public abstract class Model {
    private static final Logger logger = LogManager.getLogger(User.class);

    public int id;
    public boolean isDeleted;

    public String getDataSource() {
        return "./db/" + this.getClass().getName();
    }
    public static int getLastId(String datasource) {
        File path = new File(datasource + "/");
        path.mkdirs();
        return path.list().length;
    }
    public Model() {
        isDeleted = false;
    }

    public void save() throws ValidationException, ConnectionException {
        if (!isDeleted)
            isValid();

        JSONObject data = getJSON();
        if (id == 0)
            id = getLastId(getDataSource()) + 1;

        data.put("id", id);
        data.put("isDeleted", isDeleted);

        File path = new File(getDataSource() + "/" + id + ".json");
        if (path.exists())
            path.delete();

        try {
            path.getParentFile().mkdirs();
            path.createNewFile();
        }
        catch(IOException e) {
            logger.error("Creating New Database Record File Failed - " + e.getMessage());
            throw new ConnectionException();
        }

        try {
            PrintStream printStream = new PrintStream(path);
            printStream.print(data.toString());
            printStream.flush();
            printStream.close();
        }
        catch (FileNotFoundException e) {
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
        catch (ValidationException e) { }
    }
    public void isValid() throws ValidationException, ConnectionException { }

    public abstract JSONObject getJSON();

    public static JSONObject loadJSON(int id, String datasrc) throws ConnectionException {
        File file = new File(datasrc + "/" + id + ".json");
        try {
            String content = FileUtils.readFileToString(file, "utf-8");
            return new JSONObject(content);
        }
        catch (IOException e) {
            logger.error("Can't Read Database Record File - " + e.getMessage());
            throw new ConnectionException();
        }
    }
}