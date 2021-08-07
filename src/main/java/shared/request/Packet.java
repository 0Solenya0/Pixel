package shared.request;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import shared.gson.annotations.Exclude;
import shared.gson.gsonAdapter.HibernateProxyTypeAdapter;
import shared.gson.gsonAdapter.LocalDateAdapter;
import shared.gson.gsonAdapter.LocalDateTimeAdapter;
import shared.gson.gsonAdapter.LocalTimeAdapter;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;

public class Packet implements Serializable {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
            .excludeFieldsWithoutExposeAnnotation()
            .create();
    private final HashMap<String, String> data = new HashMap<>();
    public String target = "";
    public StatusCode status;
    public long identifier;

    public Packet(String target) {
        this.target = target;
        SecureRandom random = new SecureRandom();
        identifier = random.nextLong();
    }

    public Packet(StatusCode statusCode) {
        status = statusCode;
        SecureRandom random = new SecureRandom();
        identifier = random.nextLong();
    }

    public Packet put(String key, String value) {
        data.put(key, value);
        return this;
    }

    public Packet put(String key, int value) {
        data.put(key, String.valueOf(value));
        return this;
    }

    public Packet put(String key, Boolean value) {
        data.put(key, String.valueOf(value));
        return this;
    }

    public <T> Packet putObject(String key, T obj) {
        data.put(key, gson.toJson(obj));
        return this;
    }

    public boolean hasKey(String key) {
        return data.containsKey(key);
    }

    public String get(String key, String def) {
        if (data.containsKey(key))
            return data.get(key);
        return def;
    }

    public String get(String key) {
        if (data.containsKey(key))
            return data.get(key);
        return null;
    }

    public int getInt(String key) {
        return Integer.parseInt(data.get(key));
    }

    public int getInt(String key, int def) {
        if (data.containsKey(key))
            return getInt(key);
        return def;
    }

    public boolean getBool(String key) {
        return Boolean.parseBoolean(data.get(key));
    }

    public boolean getBool(String key, boolean def) {
        if (data.containsKey(key))
            return getBool(key);
        return def;
    }

    public <T> T getObject(String key, Class<T> objClass) {
        return gson.fromJson(data.get(key), objClass);
    }

    public <T> T getObject(String key, Class<T> objClass, T def) {
        if (!data.containsKey(key))
            return def;
        try {
            return gson.fromJson(data.get(key), objClass);
        }
        catch (Exception e) {
            return null;
        }
    }

    public <T> T getObject(String key, Type type) {
        return gson.fromJson(data.get(key), type);
    }

    public String getJson() {
        return gson.toJson(this);
    }

    public StatusCode getStatus() {
        return status;
    }
}
