package shared.event;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ObservableField<T> {
    private final ArrayList<Observer<T>> observers = new ArrayList<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private T value;

    public void addObserver(Observer<T> observer) {
        observers.add(observer);
        lock.readLock().lock();
        if (value != null)
            observer.update(value);
        lock.readLock().unlock();;
    }

    public void notifyObservers() {
        lock.readLock().lock();
        for (Observer<T> observer: observers)
            observer.update(value);
        lock.readLock().unlock();
    }

    public void set(T value) {
        lock.writeLock().lock();
        this.value = value;
        lock.writeLock().unlock();
        if (value != null)
            notifyObservers();
    }

    public T get() {
        lock.readLock().lock();
        T val = value;
        lock.readLock().unlock();
        return val;
    }
}
