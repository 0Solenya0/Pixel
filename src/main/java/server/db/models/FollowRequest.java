package server.db.models;

import com.google.gson.annotations.Expose;
import server.db.fields.RequestState;
import shared.models.Model;
import shared.models.User;

import javax.persistence.*;

@Entity
public class FollowRequest extends Model {

    @ManyToOne(optional = false)
    @Expose
    private User sender, receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Expose
    private RequestState state;

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public RequestState getState() {
        return state;
    }

    public void setState(RequestState state) {
        this.state = state;
    }
}
