package shared.event;

public interface Observer<T> {
    void update(T value);
}
