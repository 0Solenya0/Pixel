package Server.models;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public abstract class Model {
    private static final Logger logger = LogManager.getLogger(User.class);

    public int id;
    public void save() throws Exception {
        if (!isValid())
            throw new Exception("Validation Failed.");

        JSONObject data = getJSON();
        int id = getLastId(getdatasrc()) + 1;
        if (!data.get("id").toString().equals("0"))
            id = Integer.parseInt(getJSON().get("id").toString());
        data.put("id", id);

        File path = new File(getdatasrc() + "/" + id + ".json");
        if (path.exists())
            path.delete();
        try {
            path.getParentFile().mkdirs();
            path.createNewFile();
        }
        catch (Exception e) { }
        PrintStream printStream = new PrintStream(path);
        printStream.print(data.toString());
        printStream.flush();
        printStream.close();
        this.id = id;
        logger.info(String.format("%s got saved.", this.getClass()));
    }
    public boolean isValid() throws Exception {
        return true;
    }

    public abstract String getdatasrc();
    public abstract JSONObject getJSON();

    public static JSONObject loadJSON(int id, String datasrc) throws IOException {
        File file = new File(datasrc + "/" + id + ".json");
        String content = FileUtils.readFileToString(file, "utf-8");

        return new JSONObject(content);
    }
    public static int getLastId(String datasrc) {
        File path = new File(datasrc + "/");
        path.mkdirs();
        return path.list().length;
    }
}