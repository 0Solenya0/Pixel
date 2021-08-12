package client.store;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.controllers.message.GroupActionController;
import shared.gson.gsonAdapter.LocalDateAdapter;
import shared.gson.gsonAdapter.LocalDateTimeAdapter;
import shared.util.Config;

import java.io.*;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Store {
    private static final Logger logger = LogManager.getLogger(Store.class);
    protected transient Config config = Config.getConfig("mainConfig");

    public synchronized void save() {
        File path = new File(getDataSource());
        if (path.exists())
            path.delete();
        File parent = new File(path.getParent());
        parent.mkdirs();
        try {
            FileWriter writer = new FileWriter(getDataSource());
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            gson.toJson(this, writer);
            writer.flush();
            logger.info("Store data saved successfully");
        }
        catch (IOException e) {
            logger.error("Store wasn't saved - " + e.getMessage());
        }
    }

    public Store load() {
        FileReader file = null;
        try {
            file = new FileReader(getDataSource());
        } catch (FileNotFoundException e) {
            logger.error("Store data couldn't be loaded");
            return null;
        }
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        return gson.fromJson(file, this.getClass());
    }

    protected abstract String getDataSource();
}
