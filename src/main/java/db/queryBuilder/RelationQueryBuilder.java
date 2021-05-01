package db.queryBuilder;

import apps.auth.model.User;
import apps.relation.model.Relation;
import apps.relation.model.field.RelStatus;

public class RelationQueryBuilder extends QueryBuilder<Relation> {

    public RelationQueryBuilder() {
        super();
    }

    public RelationQueryBuilder getByTwoUser(int user1, int user2) {
        addCustomFilter(relation -> relation.getSender() == user1 && relation.getReceiver() == user2);
        return this;
    }

    public RelationQueryBuilder getByUser1(User user1) {
        addCustomFilter(relation -> relation.getSender() == user1.id);
        return this;
    }

    public RelationQueryBuilder getByUser2(User user2) {
        addCustomFilter(relation -> relation.getReceiver() == user2.id);
        return this;
    }

    public RelationQueryBuilder getByType(RelStatus t) {
        addCustomFilter(relation -> relation.getType() == t);
        return this;
    }

    public RelationQueryBuilder getEnabled() {
        addCustomFilter(relation -> {
            try {
                return context.users.get(relation.getSender()).isEnabled()
                        && context.users.get(relation.getReceiver()).isEnabled();
            }
            catch (Exception e) {
                return false;
            }
        });
        return this;
    }
}
