package manager;

import com.google.gson.Gson;
import http.server.KVTaskClient;

public class HttpTaskManager extends FileBackedTasksManager{
    KVTaskClient kvTaskClient;
    private final String url;
    private Gson gson = Managers.getGsonBuilder();

    public HttpTaskManager() {
        super(null);
        url = "http://localhost:8078/";
        kvTaskClient = new KVTaskClient();
    }

    @Override
    protected void save() {
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
