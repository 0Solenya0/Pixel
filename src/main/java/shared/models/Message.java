package shared.models;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Message extends Model {

    @ManyToOne(optional = false, cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    @Expose
    private User sender;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    @Expose
    private User receiver;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    @Expose
    private Group receiverGroup;

    @Expose
    private boolean seen = false;

    @Expose
    private boolean delivered = false;

    @Expose
    private LocalDateTime schedule;

    @Expose
    private String content;
    @Expose
    private byte[] photo;

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

    public Group getReceiverGroup() {
        return receiverGroup;
    }

    public void setReceiverGroup(Group receiverGroup) {
        this.receiverGroup = receiverGroup;
    }

    public LocalDateTime getSchedule() {
        if (schedule == null)
            return LocalDateTime.now();
        return schedule;
    }

    public void setSchedule(LocalDateTime schedule) {
        this.schedule = schedule;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }
}
