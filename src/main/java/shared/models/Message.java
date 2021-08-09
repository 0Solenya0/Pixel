package shared.models;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Message {

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
}
