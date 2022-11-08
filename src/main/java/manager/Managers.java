package manager;

import Serializers.DurationAdapter;
import Serializers.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {

    public static HistoryManager getInMemoryHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getHttpTaskManager() {
        return new HttpTaskManager();
    }

    public static Gson getGsonBuilder() {
        Gson gson = new GsonBuilder().serializeNulls()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        return gson;
    }
}
