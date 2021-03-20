package Server.models;

import Server.models.Exceptions.ConnectionException;
import Server.models.Exceptions.ValidationException;
import Server.models.Fields.RelType;
import Server.models.Filters.RelationFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Relation extends Model {
    private static final Logger logger = LogManager.getLogger(Relation.class);

    public int user1, user2;
    public RelType type;


    public Relation(int user1, int user2, RelType type) {
        super();
        this.user1 = user1;
        this.user2 = user2;
        this.type = type;
    }

    /** Must be in every model section **/
    public static Relation get(int id) throws ConnectionException {
        return (Relation) loadObj(id, Relation.class);
    }
    public static RelationFilter getFilter() throws ConnectionException {
        return new RelationFilter();
    }

    public void isValid() throws ValidationException, ConnectionException {
        if (user1 == user2)
            throw new ValidationException("user", "Relation", "A user can't have relation with itself");
        if (user1 > User.getLastId(User.class) || user2 > User.getLastId(User.class))
            throw new ValidationException("user", "Relation", "User does not exist");
        if (getFilter().getByTwoUser(user1, user2) != null && this.id != getFilter().getByTwoUser(user1, user2).id)
            throw new ValidationException("Relation", "Relation", "Relationship already exists");
    }
}
