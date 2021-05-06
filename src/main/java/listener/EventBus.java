package listener;

import java.util.ArrayList;

public interface EventBus {
    ArrayList<StringListener> listeners = new ArrayList<>();
    default void broadcast(String s) {
        for (StringListener listener: listeners)
            listener.listen(s);
    }

    default void addListenerToBus(StringListener stringListener)  {
        listeners.add(stringListener);
    }
}
