package Server.models;

import Server.models.Fields.RelType;
import Server.models.Filters.RelationFilter;
import Server.models.Filters.TweetFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;

public class Relation extends Model {
    private static final Logger logger = LogManager.getLogger(Relation.class);

    public int user1, user2;
    public RelType type;

    public static final String datasrc = "./db/Relations";
    public String getdatasrc() {
        return datasrc;
    }

    public Relation(int user1, int user2, RelType type) {
        this.user1 = user1;
        this.user2 = user2;
        this.type = type;
        this.isActive = true;
    }
    public Relation(int id) throws Exception {
        JSONObject rel = loadJSON(id, datasrc);
        this.id = id;
        this.user1 = Integer.parseInt(rel.get("user1").toString());
        this.user2 = Integer.parseInt(rel.get("user2").toString());
        this.type = RelType.valueOf(rel.getString("type"));
        this.isActive = Boolean.parseBoolean(rel.get("isActive").toString());
    }

    /** Must be in every model section **/
    public static Relation get(int id) throws Exception {
        return new Relation(id);
    }
    public static RelationFilter getFilter() throws Exception {
        return new RelationFilter();
    }

    public boolean isValid() throws Exception {
        if (user1 == user2)
            throw new Exception("A user can't have relation with itself");
        if (user1 > User.getLastId(User.datasrc) || user2 > User.getLastId(User.datasrc))
            throw new Exception("User does not exist");
        if (getFilter().getByTwoUser(user1, user2) != null && this.id != getFilter().getByTwoUser(user1, user2).id)
            throw new Exception("Relationship exists");
        return true;
    }
    public JSONObject getJSON() {
        JSONObject rel = new JSONObject();
        rel.put("id", id);
        rel.put("user1", user1);
        rel.put("user2", user2);
        rel.put("type", type);
        return rel;
    }
}
