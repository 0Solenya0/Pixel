package shared.models;

import com.google.gson.annotations.Expose;
import shared.models.fields.NotificationType;

import javax.persistence.*;

@Entity
public class Notification extends Model {

    @ManyToOne
    @Expose
    private User sender;

    @ManyToOne(optional = false)
    @Expose
    private User receiver;

    @Column(nullable = false)
    @Expose
    private String message;

    @Enumerated(EnumType.STRING)
    @Expose
    private NotificationType type;

    @Expose
    private boolean visible = true;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
