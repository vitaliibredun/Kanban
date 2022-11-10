package manager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import constants.Status;
import http.server.KVTaskClient;
import http.server.HttpTaskServer;
import http.server.KVServer;
import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class HttpTaskManagerTest {
    private KVServer kvServer;
    private HttpTaskServer httpTaskServer;
    private KVTaskClient kvTaskClient;
    private Gson gson;

    @BeforeEach
    void setUp() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        kvTaskClient = new KVTaskClient();
        gson = Managers.getGsonBuilder();
    }

    @AfterEach
    void stopServers() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    @DisplayName("Проверка добавления всех tasks на KVServer")
    void saveTest() throws IOException, InterruptedException {
        Assertions.assertAll(
                () -> Assertions.assertEquals(0, kvTaskClient.load("task").length(), "Tasks не равны"),
                () -> Assertions.assertEquals(0, kvTaskClient.load("epic").length(), "Tasks не равны"),
                () -> Assertions.assertEquals(0, kvTaskClient.load("subtask").length(), "Tasks не равны"),
                () -> Assertions.assertEquals(0, kvTaskClient.load("history").length(), "Tasks не равны")
        );

        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Task newTask = new Task("task", "some description", Status.NEW);
        String json = gson.toJson(newTask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8080/tasks/epic/");
        Epic newEpic = new Epic("epic", "some description");
        String json1 = gson.toJson(newEpic);
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:8080/tasks/subtask/?idEpic=0");
        String substring = url2.getQuery().substring(7);
        int epicId = Integer.parseInt(substring);
        SubTask newSubtask = new SubTask("subtask", "some description", Status.NEW, epicId);
        String json2 = gson.toJson(newSubtask);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

        Assertions.assertAll(
                () -> Assertions.assertNotEquals(0, kvTaskClient.load("task").length(), "Tasks не равны"),
                () -> Assertions.assertNotEquals(0, kvTaskClient.load("epic").length(), "Tasks не равны"),
                () -> Assertions.assertNotEquals(0, kvTaskClient.load("subtask").length(), "Tasks не равны"),
                () -> Assertions.assertNotEquals(0, kvTaskClient.load("history").length(), "Tasks не равны")
        );
    }

    @Test
    @DisplayName("Проверка запроса всех tasks с KVServer")
    void loadTest() throws IOException, InterruptedException {
        Assertions.assertAll(
                () -> Assertions.assertEquals(0, kvTaskClient.load("task").length(), "Tasks не равны"),
                () -> Assertions.assertEquals(0, kvTaskClient.load("epic").length(), "Tasks не равны"),
                () -> Assertions.assertEquals(0, kvTaskClient.load("subtask").length(), "Tasks не равны"),
                () -> Assertions.assertEquals(0, kvTaskClient.load("history").length(), "Tasks не равны")
        );

        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Task newTask = new Task("task", "some description", Status.NEW);
        String json = gson.toJson(newTask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8080/tasks/epic/");
        Epic newEpic = new Epic("epic", "some description");
        String json1 = gson.toJson(newEpic);
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:8080/tasks/subtask/?idEpic=1");
        String substring = url2.getQuery().substring(7);
        int epicId = Integer.parseInt(substring);
        SubTask newSubtask = new SubTask("subtask", "some description", Status.NEW, epicId);
        String json2 = gson.toJson(newSubtask);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

        String bodyFromServer1 = kvTaskClient.load("task");
        JsonElement jsonElement1 = JsonParser.parseString(bodyFromServer1);
        JsonElement task = jsonElement1.getAsJsonObject().get("0");
        Task taskFromKVServer = gson.fromJson(task, Task.class);

        String bodyFromServer2 = kvTaskClient.load("epic");
        JsonElement jsonElement2 = JsonParser.parseString(bodyFromServer2);
        JsonElement epic = jsonElement2.getAsJsonObject().get("1");
        Epic epicFromKVServer = gson.fromJson(epic, Epic.class);

        String bodyFromServer3 = kvTaskClient.load("subtask");
        JsonElement jsonElement3 = JsonParser.parseString(bodyFromServer3);
        JsonElement subtask = jsonElement3.getAsJsonObject().get("2");
        SubTask subtaskFromKVServer = gson.fromJson(subtask, SubTask.class);

        Assertions.assertAll(
                () -> Assertions.assertEquals(newTask.getName(), taskFromKVServer.getName(), "Tasks не равны"),
                () -> Assertions.assertEquals(newTask.getDescription(), taskFromKVServer.getDescription(), "Tasks не равны"),
                () -> Assertions.assertEquals(newTask.getStatus(), taskFromKVServer.getStatus(), "Tasks не равны"),
                () -> Assertions.assertEquals(newEpic.getName(), epicFromKVServer.getName(), "Epics не равны"),
                () -> Assertions.assertEquals(newEpic.getDescription(), epicFromKVServer.getDescription(), "Epics не равны"),
                () -> Assertions.assertEquals(newEpic.getStatus(), epicFromKVServer.getStatus(), "Epics не равны"),
                () -> Assertions.assertEquals(newSubtask.getName(), subtaskFromKVServer.getName(), "Epics не равны"),
                () -> Assertions.assertEquals(newSubtask.getDescription(), subtaskFromKVServer.getDescription(), "Epics не равны"),
                () -> Assertions.assertEquals(newSubtask.getStatus(), subtaskFromKVServer.getStatus(), "Epics не равны")
        );
    }
}