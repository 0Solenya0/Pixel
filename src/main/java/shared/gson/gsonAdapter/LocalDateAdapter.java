package shared.gson.gsonAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;

public class LocalDateAdapter extends TypeAdapter<LocalDate> {
    @Override
    public void write(final JsonWriter jsonWriter, final LocalDate localDate ) throws IOException {
        if (localDate == null)
            jsonWriter.value("null");
        else
            jsonWriter.value(localDate.toString());
    }

    @Override
    public LocalDate read( final JsonReader jsonReader ) throws IOException {
        String s = jsonReader.nextString();
        if (s.equals("null"))
            return null;
        else
            return LocalDate.parse(s);
    }
}