package shared.gsonAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class LocalTimeAdapter extends TypeAdapter<LocalTime> {
    @Override
    public void write(final JsonWriter jsonWriter, final LocalTime localTime ) throws IOException {
        if (localTime == null)
            jsonWriter.value("null");
        else
            jsonWriter.value(localTime.toString());
    }

    @Override
    public LocalTime read( final JsonReader jsonReader ) throws IOException {
        String s = jsonReader.nextString();
        if (s.equals("null"))
            return null;
        else
            return LocalTime.parse(s);
    }
}