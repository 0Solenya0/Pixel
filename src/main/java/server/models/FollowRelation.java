package server.models;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "target_id"}) })
public class FollowRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private User target;

    public User getUser() {
        return user;
    }

    public User getTarget() {
        return target;
    }
}
