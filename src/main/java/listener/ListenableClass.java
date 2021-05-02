package listener;

import java.util.ArrayList;

public interface ListenableClass {
    ArrayList<StringListener> listeners = new ArrayList<>();
    default void broadcast(String s) {
        for (StringListener listener: listeners)
            listener.listen(s);
    }

    default void addListener(StringListener stringListener)  {
        listeners.add(stringListener);
    }
}
