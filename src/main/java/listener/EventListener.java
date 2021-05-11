package listener;

import db.exception.ConnectionException;
import db.exception.ValidationException;
import event.Event;

public interface EventListener<T extends Event> {
    <E> E eventOccurred(T event) throws ConnectionException, ValidationException;
}
