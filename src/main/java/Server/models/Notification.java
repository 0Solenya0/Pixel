package Server.models;

import Server.models.Fields.NotificationType;
import Server.models.Fields.RelType;
import Server.models.Filters.NotificationFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class Notification extends Model {
    private static final Logger logger = LogManager.getLogger(Relation.class);

    public static final String datasrc = "./db/Notifications";
    public String getdatasrc() {
        return datasrc;
    }
    public int user1, user2;
    protected String message;
    public NotificationType type;

    public Notification(int user1, int user2, String content) {
        this.user1 = user1;
        this.user2 = user2;
        this.type = NotificationType.INFO;
        this.message = content;
    }
    public Notification(int user1, int user2, NotificationType type) {
        this.user1 = user1;
        this.user2 = user2;
        this.type = type;
        this.message = "";
    }
    public Notification(int id) throws Exception {
        JSONObject notif = loadJSON(id, datasrc);
        this.id = id;
        this.user1 = Integer.parseInt(notif.get("user1").toString());
        this.user1 = Integer.parseInt(notif.get("user2").toString());
        this.type = NotificationType.valueOf(notif.getString("type"));
        this.isActive = Boolean.parseBoolean(notif.get("isActive").toString());
        this.message = notif.getString("message");
    }

    /** Must be in every model section **/
    public static Notification get(int id) throws Exception {
        return new Notification(id);
    }
    public static NotificationFilter getFilter() throws Exception {
        return new NotificationFilter();
    }

    public boolean isValid() throws Exception {
        if (user1 > User.getLastId(User.datasrc) && user2 > User.getLastId(User.datasrc))
            throw new Exception("User does not exist");
        return true;
    }
    public JSONObject getJSON() {
        JSONObject rel = new JSONObject();
        rel.put("id", id);
        rel.put("user1", user1);
        rel.put("user2", user2);
        rel.put("message", message);
        rel.put("type", type);
        return rel;
    }
}
