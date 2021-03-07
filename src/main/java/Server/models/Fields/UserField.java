package Server.models.Fields;

import org.json.JSONObject;

public class UserField<E> {
    public AccessLevel accessLevel;
    private E value;

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public E get() {
        return value;
    }

    public void set(E value) {
        this.value = value;
    }

    public JSONObject getJSON() {
        JSONObject obj = new JSONObject();
        obj.put("value", value);
        obj.put("access", accessLevel.toString());
        return obj;
    }
}
