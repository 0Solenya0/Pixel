package shared.models;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserList extends Model {

    @ManyToMany
    @JoinTable(
            name = "list_users",
            joinColumns = @JoinColumn(name = "list_id"),
            inverseJoinColumns = @JoinColumn(name="user_id")
    )
    @Expose
    private List<User> users = new ArrayList<>();

    @Expose
    private String name;

    @ManyToOne
    @Expose
    private User owner;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
