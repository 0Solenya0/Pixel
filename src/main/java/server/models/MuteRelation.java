package server.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "target_id"}) })
public class MuteRelation {

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
