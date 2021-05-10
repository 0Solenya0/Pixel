package model.field;

public class LockedField<E> {
    public AccessLevel accessLevel;
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
