package apps.relation.model;

import apps.auth.model.User;
import apps.relation.model.field.RelStatus;
import model.Model;

public class Relation extends Model {

    private int user1, user2;
    private RelStatus type;

    public Relation(User user1, User user2, RelStatus type) {
        super();
        this.user1 = user1.id;
        this.user2 = user2.id;
        this.type = type;
    }

    public int getSender() {
        return user1;
    }

    public int getReceiver() {
        return user2;
    }

    public RelStatus getType() {
        return type;
    }
}
