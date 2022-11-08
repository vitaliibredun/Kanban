package manager;

import Serializers.DurationAdapter;
import Serializers.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.server.KVTaskClient;

import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskManager extends FileBackedTasksManager{
    KVTaskClient kvTaskClient;
    private final String url;

    public HttpTaskManager() {
        super(null);
        url = "http://localhost:8078/";
        kvTaskClient = new KVTaskClient();
    }

    @Override
    protected void save() {
        Gson gson = new GsonBuilder().serializeNulls()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        String jsonTasks = gson.toJson(tasks);
        String jsonEpics = gson.toJson(epics);
        String jsonSubtasks = gson.toJson(subtasks);
        String jsonHistory = gson.toJson(historyManager);
        kvTaskClient.save("task", jsonTasks);
        kvTaskClient.save("epic", jsonEpics);
        kvTaskClient.save("subtask", jsonSubtasks);
        kvTaskClient.save("history", jsonHistory);
    }

    public void load() {
        kvTaskClient.load("task");
        kvTaskClient.load("epic");
        kvTaskClient.load("subtask");
        kvTaskClient.load("history");
    }
}
