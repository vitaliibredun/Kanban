import Serializers.DurationAdapter;
import Serializers.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import constants.Status;
import http.server.server.HttpTaskServer;
import http.server.server.KVServer;
import manager.HttpTaskManager;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();

        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        HttpTaskManager httpTaskManager = new HttpTaskManager();

        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Gson gson = new GsonBuilder().serializeNulls()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        Task newTask = new Task("task 1", "some description", Status.NEW);
        String json = gson.toJson(newTask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());



//
//        HttpTaskManager httpTaskManager = new HttpTaskManager();
//        httpTaskManager.load();


//        KVTaskClient kvTaskClient = new KVTaskClient();
//        kvTaskClient.save("key", "value");
//
//        kvTaskClient.load("key");
//
//
//        kvTaskClient.save("key", "new value");
//        kvTaskClient.load("key");
    }
}