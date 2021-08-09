package shared.models;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Message extends Model {

    @ManyToOne(optional = false)
    @Expose
    private User sender;

    @ManyToOne
    @Expose
    private User receiver;

    @ManyToOne
    @Expose
    private Group receiverGroup;

    @OneToMany
    @JoinTable(
            name = "message_viewer",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name="user_id")
    )
    @Expose
    public List<User> viewers = new ArrayList<>();

    @OneToMany
    @JoinTable(
            name = "message_deliver",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name="user_id")
    )
    public List<User> delivers = new ArrayList<>();

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

    public List<User> getViewers() {
        return viewers;
    }
}
