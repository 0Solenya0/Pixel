package shared.gson.gsonAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime ) throws IOException {
        if (localDateTime == null)
            jsonWriter.value("null");
        else
            jsonWriter.value(localDateTime.toString());
    }

    @Override
    public LocalDateTime read( final JsonReader jsonReader ) throws IOException {
        String s = jsonReader.nextString();
        if (s.equals("null"))
            return null;
        else
            return LocalDateTime.parse(s);
    }
}
