package model;

import java.time.LocalDateTime;

public abstract class Model {
    public int id;
    public boolean isDeleted;
    public LocalDateTime createdAt;
    public LocalDateTime lastModified;

    public Model() {
        isDeleted = false;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}