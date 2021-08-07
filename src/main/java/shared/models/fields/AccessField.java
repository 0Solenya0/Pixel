package shared.models.fields;

import com.google.gson.annotations.Expose;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class AccessField<E> {
    @Enumerated(EnumType.STRING)
    @Expose
    private AccessLevel accessLevel = AccessLevel.PRIVATE;
    @Expose
    private E value;

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public E get() {
        return value;
    }

    public void set(E value) {
        this.value = value;
    }
}