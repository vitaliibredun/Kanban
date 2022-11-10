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
import java.util.List;

public class HttpTaskServerTest {

    private KVServer kvServer;
    private HttpTaskServer httpTaskServer;
    private KVTaskClient kvTaskClient;
    private TaskManager manager;
    private Gson gson;

    @BeforeEach
    void setUp() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        kvTaskClient = new KVTaskClient();
        manager = Managers.getHttpTaskManager();
        gson = Managers.getGsonBuilder();
    }

    @AfterEach
    void cleanUp() {
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();
        manager.getPrioritizedTasks().clear();
        kvServer.getData().clear();
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    @DisplayName("Проверка добавления task на сервер")
    void addTaskTest() throws IOException, InterruptedException {
        Assertions.assertTrue(kvTaskClient.load("task").isEmpty(), "Данные не верны");

        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Task newTask = new Task("task", "some description", Status.NEW);
        String json = gson.toJson(newTask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String bodyFromServer = kvTaskClient.load("task");
        JsonElement jsonElement = JsonParser.parseString(bodyFromServer);
        JsonElement task = jsonElement.getAsJsonObject().get("0");
        Task taskFromServer = gson.fromJson(task, Task.class);

        Assertions.assertAll(
                        () -> Assertions.assertFalse(kvTaskClient.load("task").isEmpty(), "Данные не верны"),
                        () -> Assertions.assertEquals(newTask, taskFromServer, "Tasks не равны")
                );
    }

    @Test
    @DisplayName("Проверка добавления epic на сервер")
    void addEpicTest() throws IOException, InterruptedException {
        Assertions.assertTrue(kvTaskClient.load("epic").isEmpty(), "Данные не верны");

        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Epic newEpic = new Epic("epic", "some description");
        String json = gson.toJson(newEpic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String bodyFromServer = kvTaskClient.load("epic");
        JsonElement jsonElement = JsonParser.parseString(bodyFromServer);
        JsonElement epic = jsonElement.getAsJsonObject().get("0");
        Epic epicFromServer = gson.fromJson(epic, Epic.class);

        Assertions.assertAll(
                () -> Assertions.assertFalse(kvTaskClient.load("epic").isEmpty(), "Данные не верны"),
                () -> Assertions.assertEquals(newEpic, epicFromServer, "Epics не равны")
        );
    }

    @Test
    @DisplayName("Проверка добавления subtask на сервер")
    void addSubtaskTest() throws IOException, InterruptedException {
        Assertions.assertTrue(kvTaskClient.load("subtask").isEmpty(), "Данные не верны");

        URI url1 = URI.create("http://localhost:8080/tasks/epic/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
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

        String bodyFromServer = kvTaskClient.load("subtask");
        JsonElement jsonElement = JsonParser.parseString(bodyFromServer);
        JsonElement subtask = jsonElement.getAsJsonObject().get("1");
        SubTask subtaskFromServer = gson.fromJson(subtask, SubTask.class);

        Assertions.assertAll(
                () -> Assertions.assertFalse(kvTaskClient.load("subtask").isEmpty(), "Данные не верны"),
                () -> Assertions.assertEquals(newSubtask.getName(), subtaskFromServer.getName(), "Subtasks не равны"),
                () -> Assertions.assertEquals(newSubtask.getDescription(), subtaskFromServer.getDescription(), "Subtasks не равны"),
                () -> Assertions.assertEquals(newSubtask.getStatus(), subtaskFromServer.getStatus(), "Subtasks не равны")
        );
    }

    @Test
    @DisplayName("Проверка запроса всех tasks с сервера")
    void getAllTasksTest() throws IOException, InterruptedException {
        Assertions.assertTrue(kvTaskClient.load("task").isEmpty(), "Данные не верны");

        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Task newTask = new Task("task", "some description", Status.NEW);
        String json = gson.toJson(newTask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).POST(body).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept","application/json")
                .build();
        httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

        String bodyFromServer = kvTaskClient.load("task");
        JsonElement jsonElement = JsonParser.parseString(bodyFromServer);
        JsonElement task = jsonElement.getAsJsonObject().get("0");
        Task taskFromServer = gson.fromJson(task, Task.class);

        Assertions.assertAll(
                () -> Assertions.assertFalse(kvTaskClient.load("task").isEmpty(), "Данные не верны"),
                () -> Assertions.assertEquals(newTask, taskFromServer, "Tasks не равны")
        );
    }

    @Test
    @DisplayName("Проверка запроса всех epics с сервера")
    void getAllEpicsTest() throws IOException, InterruptedException {
        Assertions.assertTrue(kvTaskClient.load("epic").isEmpty(), "Данные не верны");

        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Epic newEpic = new Epic("epic", "some description");
        String json = gson.toJson(newEpic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).POST(body).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept","application/json")
                .build();
        httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

        String bodyFromServer = kvTaskClient.load("epic");
        JsonElement jsonElement = JsonParser.parseString(bodyFromServer);
        JsonElement epic = jsonElement.getAsJsonObject().get("0");
        Epic epicFromServer = gson.fromJson(epic, Epic.class);

        Assertions.assertAll(
                () -> Assertions.assertFalse(kvTaskClient.load("epic").isEmpty(), "Данные не верны"),
                () -> Assertions.assertEquals(newEpic, epicFromServer, "Epics не равны")
        );
    }

    @Test
    @DisplayName("Проверка запроса всех subtasks с сервера")
    void getAllSubtasksTest() throws IOException, InterruptedException {
        Assertions.assertTrue(kvTaskClient.load("subtask").isEmpty(), "Данные не верны");

        URI url1 = URI.create("http://localhost:8080/tasks/epic/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
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

        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept","application/json")
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String bodyFromServer = kvTaskClient.load("subtask");
        JsonElement jsonElement = JsonParser.parseString(bodyFromServer);
        JsonElement subtask = jsonElement.getAsJsonObject().get("1");
        SubTask subtaskFromServer = gson.fromJson(subtask, SubTask.class);

        Assertions.assertAll(
                () -> Assertions.assertFalse(kvTaskClient.load("subtask").isEmpty(), "Данные не верны"),
                () -> Assertions.assertEquals(newSubtask.getName(), subtaskFromServer.getName(), "Subtasks не равны"),
                () -> Assertions.assertEquals(newSubtask.getDescription(), subtaskFromServer.getDescription(), "Subtasks не равны"),
                () -> Assertions.assertEquals(newSubtask.getStatus(), subtaskFromServer.getStatus(), "Subtasks не равны")
        );
    }

    @Test
    @DisplayName("Проверка запроса task с сервера")
    void getTaskTest() throws IOException, InterruptedException {
        Assertions.assertTrue(kvTaskClient.load("task").isEmpty(), "Данные не верны");

        URI url1 = URI.create("http://localhost:8080/tasks/task/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Task newTask = new Task("task", "some description", Status.NEW);
        String json = gson.toJson(newTask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:8080/tasks/task/?id=0");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url2)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept","application/json")
                .build();
        httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

        String bodyFromServer = kvTaskClient.load("task");
        JsonElement jsonElement = JsonParser.parseString(bodyFromServer);
        JsonElement task = jsonElement.getAsJsonObject().get("0");
        Task taskFromServer = gson.fromJson(task, Task.class);

        Assertions.assertAll(
                () -> Assertions.assertFalse(kvTaskClient.load("task").isEmpty(), "Данные не верны"),
                () -> Assertions.assertEquals(newTask, taskFromServer, "Tasks не равны")
        );
    }

    @Test
    @DisplayName("Проверка запроса epic с сервера")
    void getEpicTest() throws IOException, InterruptedException {
        Assertions.assertTrue(kvTaskClient.load("epic").isEmpty(), "Данные не верны");

        URI url1 = URI.create("http://localhost:8080/tasks/epic/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Epic newEpic = new Epic("epic", "some description");
        String json = gson.toJson(newEpic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:8080/tasks/epic/?id=0");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url2)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept","application/json")
                .build();
        httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

        String bodyFromServer = kvTaskClient.load("epic");
        JsonElement jsonElement = JsonParser.parseString(bodyFromServer);
        JsonElement epic = jsonElement.getAsJsonObject().get("0");
        Epic epicFromServer = gson.fromJson(epic, Epic.class);

        Assertions.assertAll(
                () -> Assertions.assertFalse(kvTaskClient.load("task").isEmpty(), "Данные не верны"),
                () -> Assertions.assertEquals(newEpic, epicFromServer, "Tasks не равны")
        );
    }

    @Test
    @DisplayName("Проверка запроса subtask с сервера")
    void getSubtaskTest() throws IOException, InterruptedException {
        Assertions.assertTrue(kvTaskClient.load("subtask").isEmpty(), "Данные не верны");

        URI url1 = URI.create("http://localhost:8080/tasks/epic/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
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

        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept","application/json")
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String bodyFromServer = kvTaskClient.load("subtask");
        JsonElement jsonElement = JsonParser.parseString(bodyFromServer);
        JsonElement subtask = jsonElement.getAsJsonObject().get("1");
        SubTask subtaskFromServer = gson.fromJson(subtask, SubTask.class);

        Assertions.assertAll(
                () -> Assertions.assertFalse(kvTaskClient.load("subtask").isEmpty(), "Данные не верны"),
                () -> Assertions.assertEquals(newSubtask.getName(), subtaskFromServer.getName(), "Subtasks не равны"),
                () -> Assertions.assertEquals(newSubtask.getDescription(), subtaskFromServer.getDescription(), "Subtasks не равны"),
                () -> Assertions.assertEquals(newSubtask.getStatus(), subtaskFromServer.getStatus(), "Subtasks не равны")
        );
    }

    @Test
    @DisplayName("Проверка запроса истории всех tasks с сервера")
    void getPrioritizedTasksTest() throws IOException, InterruptedException {
        Assertions.assertAll(
                () -> Assertions.assertTrue(kvTaskClient.load("task").isEmpty(), "Данные не верны"),
                () -> Assertions.assertTrue(kvTaskClient.load("epic").isEmpty(), "Данные не верны"),
                () -> Assertions.assertTrue(kvTaskClient.load("subtask").isEmpty(), "Данные не верны")
        );

        URI url1 = URI.create("http://localhost:8080/tasks/task/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Task task = new Task("task", "some description", Status.NEW, 25, "10/35/05/11/2022");
        String json2 = gson.toJson(task);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url1).POST(body2).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

        URI url3 = URI.create("http://localhost:8080/tasks/epic/");
        Epic epic = new Epic("epic", "some description");
        String json3 = gson.toJson(epic);
        HttpRequest.BodyPublisher body3 = HttpRequest.BodyPublishers.ofString(json3);
        HttpRequest request3 = HttpRequest.newBuilder().uri(url3).POST(body3).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request3, HttpResponse.BodyHandlers.ofString());

        URI url4 = URI.create("http://localhost:8080/tasks/subtask/?idEpic=1");
        String substring = url4.getQuery().substring(7);
        int epicId = Integer.parseInt(substring);
        SubTask subTask = new SubTask("subtask", "some description", Status.NEW, epicId);
        String json4 = gson.toJson(subTask);
        HttpRequest.BodyPublisher body4 = HttpRequest.BodyPublishers.ofString(json4);
        HttpRequest request4 = HttpRequest.newBuilder().uri(url4).POST(body4).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request4, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:8080/tasks/task/history");
        HttpRequest request5 = HttpRequest.newBuilder()
                .uri(url2)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept","application/json")
                .build();
        httpClient.send(request5, HttpResponse.BodyHandlers.ofString());

        String bodyFromServer1 = kvTaskClient.load("task");
        JsonElement jsonElement1 = JsonParser.parseString(bodyFromServer1);
        JsonElement jsonTask = jsonElement1.getAsJsonObject().get("0");
        Task taskFromServer = gson.fromJson(jsonTask, Task.class);

        String bodyFromServer2 = kvTaskClient.load("epic");
        JsonElement jsonElement2 = JsonParser.parseString(bodyFromServer2);
        JsonElement jsonEpic = jsonElement2.getAsJsonObject().get("1");
        Epic epicFromServer = gson.fromJson(jsonEpic, Epic.class);

        String bodyFromServer3 = kvTaskClient.load("subtask");
        JsonElement jsonElement3 = JsonParser.parseString(bodyFromServer3);
        JsonElement jsonSubtask = jsonElement3.getAsJsonObject().get("2");
        SubTask subtaskFromServer = gson.fromJson(jsonSubtask, SubTask.class);

        List prioritizedTasks = httpTaskServer.manager.getPrioritizedTasks();

        Assertions.assertAll(
                () -> Assertions.assertFalse(kvTaskClient.load("task").isEmpty(), "Данные не верны"),
                () -> Assertions.assertEquals(2, prioritizedTasks.size(), "Данные не верны"),
                () -> Assertions.assertEquals(task, prioritizedTasks.get(0), "Данные не верны"),
                () -> Assertions.assertFalse(kvTaskClient.load("epic").isEmpty(), "Данные не верны"),
                () -> Assertions.assertFalse(kvTaskClient.load("subtask").isEmpty(), "Данные не верны"),
                () -> Assertions.assertEquals(task.getName(), taskFromServer.getName(), "Tasks не равны"),
                () -> Assertions.assertEquals(task.getDescription(), taskFromServer.getDescription(), "Tasks не равны"),
                () -> Assertions.assertEquals(task.getStatus(), taskFromServer.getStatus(), "Tasks не равны"),
                () -> Assertions.assertEquals(epic.getName(), epicFromServer.getName(), "Epics не равны"),
                () -> Assertions.assertEquals(epic.getDescription(), epicFromServer.getDescription(), "Epics не равны"),
                () -> Assertions.assertEquals(epic.getStatus(), epicFromServer.getStatus(), "Epics не равны"),
                () -> Assertions.assertEquals(subTask.getName(), subtaskFromServer.getName(), "Subtasks не равны"),
                () -> Assertions.assertEquals(subTask.getDescription(), subtaskFromServer.getDescription(), "Subtasks не равны"),
                () -> Assertions.assertEquals(subTask.getStatus(), subtaskFromServer.getStatus(), "Subtasks не равны")
        );
    }

    @Test
    @DisplayName("Проверка удаления всех tasks")
    void deleteAllTasksTest() throws IOException, InterruptedException {
        Assertions.assertTrue(manager.getAllTasks().isEmpty(), "Данные не верны");

        URI url1 = URI.create("http://localhost:8080/tasks/task/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Task task1 = new Task("task", "some description", Status.NEW);
        String json1 = gson.toJson(task1);
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

        Assertions.assertFalse(manager.getAllTasks().isEmpty(), "Данные не верны");

        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertTrue(manager.getAllTasks().isEmpty(), "Данные не верны");
    }

    @Test
    @DisplayName("Проверка удаления всех epics")
    void deleteAllEpicsTest() throws IOException, InterruptedException {
        Assertions.assertTrue(manager.getALLEpics().isEmpty(), "Данные не верны");

        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Epic epic1 = new Epic("epic", "some description");
        String json = gson.toJson(epic1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Epic epic2 = new Epic("epic", "some description");
        String json1 = gson.toJson(epic2);
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).POST(body1).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

        Assertions.assertFalse(manager.getALLEpics().isEmpty(), "Данные не верны");

        URI url1 = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url1).DELETE().version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

        Assertions.assertTrue(manager.getALLEpics().isEmpty(), "Данные не верны");
    }

    @Test
    @DisplayName("Проверка удаления всех subtasks")
    void deleteAllSubtasksTest() throws IOException, InterruptedException {
        Assertions.assertTrue(manager.getALLSubtasks().isEmpty(), "Данные не верны");

        URI url1 = URI.create("http://localhost:8080/tasks/epic/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Epic newEpic = new Epic("epic", "some description");
        String json1 = gson.toJson(newEpic);
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:8080/tasks/subtask/?idEpic=0");
        String substring = url2.getQuery().substring(7);
        int epicId = Integer.parseInt(substring);
        SubTask subTask1 = new SubTask("subtask", "some description", Status.NEW, epicId);
        String json2 = gson.toJson(subTask1);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

        SubTask subTask2 = new SubTask("subtask", "some description", Status.NEW, epicId);
        String json3 = gson.toJson(subTask2);
        HttpRequest.BodyPublisher body3 = HttpRequest.BodyPublishers.ofString(json3);
        HttpRequest request3 = HttpRequest.newBuilder().uri(url2).POST(body3).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request3, HttpResponse.BodyHandlers.ofString());

        Assertions.assertFalse(manager.getALLSubtasks().isEmpty(), "Данные не верны");

        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertTrue(manager.getALLSubtasks().isEmpty(), "Данные не верны");
    }

    @Test
    @DisplayName("Проверка удаления task по id")
    void removeTaskTest() throws IOException, InterruptedException {
        Assertions.assertTrue(manager.getAllTasks().isEmpty(), "Данные не верны");

        URI url1 = URI.create("http://localhost:8080/tasks/task/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Task task1 = new Task("task", "some description", Status.NEW);
        String json1 = gson.toJson(task1);
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, manager.getAllTasks().size(), "Данные не верны"),
                () -> Assertions.assertEquals(task1, manager.getTask(0), "Tasks не равны")
        );

        URI url = URI.create("http://localhost:8080/tasks/task/?id=0");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertTrue(manager.getAllTasks().isEmpty(), "Данные не верны");
    }

    @Test
    @DisplayName("Проверка удаления epic по id")
    void removeEpicTest() throws IOException, InterruptedException {
        Assertions.assertTrue(manager.getALLEpics().isEmpty(), "Данные не верны");

        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Epic newEpic = new Epic("epic", "some description");
        String json = gson.toJson(newEpic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, manager.getALLEpics().size(), "Данные не верны"),
                () -> Assertions.assertEquals(newEpic, manager.getEpic(0), "Epics не равны")
        );

        URI url1 = URI.create("http://localhost:8080/tasks/epic/?id=0");
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).DELETE().version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

        Assertions.assertTrue(manager.getALLEpics().isEmpty(), "Данные не верны");
    }

    @Test
    @DisplayName("Проверка удаления subtask по id")
    void removeSubtaskTest() throws IOException, InterruptedException {
        Assertions.assertTrue(manager.getALLSubtasks().isEmpty(), "Данные не верны");

        URI url1 = URI.create("http://localhost:8080/tasks/epic/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
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

        Assertions.assertFalse(manager.getALLSubtasks().isEmpty(), "Данные не верны");

        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertTrue(manager.getALLSubtasks().isEmpty(), "Данные не верны");
    }

    @Test
    @DisplayName("Проверка замены task")
    void updateTaskTest() throws IOException, InterruptedException {
        Assertions.assertTrue(manager.getAllTasks().isEmpty(), "Данные не верны");

        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Task task = new Task("task", "some description", Status.NEW);
        String json = gson.toJson(task);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, manager.getAllTasks().size(), "Данные не верны"),
                () -> Assertions.assertEquals(task, manager.getTask(0), "Tasks не равны")
        );

        Task newTask = new Task("new task", "some new description",Status.NEW);
        String json1 = gson.toJson(newTask);
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).PUT(body1).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, manager.getAllTasks().size(), "Данные не верны"),
                () -> Assertions.assertEquals(newTask, manager.getTask(0), "Tasks не равны"),
                () -> Assertions.assertNotEquals(task, newTask, "Tasks не равны")
        );
    }

    @Test
    @DisplayName("Проверка замены epic")
    void updateEpicTest() throws IOException, InterruptedException {
        Assertions.assertTrue(manager.getALLEpics().isEmpty(), "Данные не верны");

        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Epic epic = new Epic("epic", "some description");
        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, manager.getALLEpics().size(), "Данные не верны"),
                () -> Assertions.assertEquals(epic, manager.getEpic(0), "Tasks не равны")
        );

        Epic newEpic = new Epic("new epic", "some new description");
        String json1 = gson.toJson(newEpic);
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).PUT(body1).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, manager.getALLEpics().size(), "Данные не верны"),
                () -> Assertions.assertEquals(newEpic, manager.getEpic(0), "Tasks не равны"),
                () -> Assertions.assertNotEquals(epic, newEpic, "Tasks не равны")
        );
    }

    @Test
    @DisplayName("Проверка замены subtask")
    void updateSubtaskTest() throws IOException, InterruptedException {
        Assertions.assertTrue(manager.getALLSubtasks().isEmpty(), "Данные не верны");

        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Epic epic = new Epic("epic", "some description");
        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8080/tasks/subtask/?idEpic=0");
        String substring = url1.getQuery().substring(7);
        int epicId = Integer.parseInt(substring);
        SubTask subTask = new SubTask("subtask", "some description", Status.NEW, epicId);
        String json1 = gson.toJson(subTask);
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(1, manager.getALLSubtasks().size(), "Данные не верны");

        SubTask newSubTask = new SubTask(1,"new subtask", "some new description", Status.NEW, epicId);
        String json2 = gson.toJson(newSubTask);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url1).PUT(body2).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, manager.getALLSubtasks().size(), "Данные не верны"),
                () -> Assertions.assertEquals(newSubTask, manager.getSubtask(1), "Subtasks не равны"),
                () -> Assertions.assertNotEquals(subTask, newSubTask, "Subtasks не равны")
        );
    }

    @Test
    @DisplayName("Проверка запроса всех subtasks по epic id")
    void getSubtasksByEpicTest() throws IOException, InterruptedException {
        Assertions.assertTrue(manager.getALLSubtasks().isEmpty(), "Данные не верны");

        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Epic epic = new Epic("epic", "some description");
        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8080/tasks/subtask/?idEpic=0");
        String substring = url1.getQuery().substring(7);
        int epicId = Integer.parseInt(substring);
        SubTask subTask = new SubTask("subtask", "some description", Status.NEW, epicId);
        String json1 = gson.toJson(subTask);
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

        Assertions.assertFalse(manager.getALLSubtasks().isEmpty(), "Данные не верны");

        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept","application/json")
                .build();
        httpClient.send(request3, HttpResponse.BodyHandlers.ofString());

        List<SubTask> subtasksByEpic = manager.getSubtasksByEpic(0);

        Assertions.assertAll(
                () -> Assertions.assertFalse(manager.getALLSubtasks().isEmpty(), "Данные не верны"),
                () -> Assertions.assertEquals(subTask.getName(), subtasksByEpic.get(0).getName(), "Subtasks не равны"),
                () -> Assertions.assertEquals(subTask.getDescription(), subtasksByEpic.get(0).getDescription(), "Subtasks не равны"),
                () -> Assertions.assertEquals(subTask.getStatus(), subtasksByEpic.get(0).getStatus(), "Subtasks не равны")
        );
    }

    @Test
    @DisplayName("Проверка обновления статуса epic")
    void updateEpicStatusTest() throws IOException, InterruptedException {
        URI url1 = URI.create("http://localhost:8080/tasks/epic/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Epic epic1 = new Epic("epic 1", "some description");
        String json1 = gson.toJson(epic1);
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(Status.NEW, manager.getEpic(0).getStatus(), "Данные не верны");

        URI url2 = URI.create("http://localhost:8080/tasks/subtask/?idEpic=0");
        String substring1 = url2.getQuery().substring(7);
        int epicId1 = Integer.parseInt(substring1);
        SubTask subTask1 = new SubTask("subtask 1", "some description", Status.IN_PROGRESS, epicId1);
        String json2 = gson.toJson(subTask1);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(Status.IN_PROGRESS, manager.getEpic(0).getStatus(), "Данные не верны");
    }

    @Test
    @DisplayName("Проверка обновления полей duration и startTime у epic")
    void updateEpicDurationAndStartTimeTest() throws IOException, InterruptedException {
        URI url1 = URI.create("http://localhost:8080/tasks/epic/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Epic epic1 = new Epic("epic 1", "some description");
        String json1 = gson.toJson(epic1);
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

        Assertions.assertAll(
                () -> Assertions.assertNull(manager.getEpic(0).getDuration(), "Данные не верны"),
                () -> Assertions.assertNull(manager.getEpic(0).getStartTime(), "Данные не верны")
        );

        URI url2 = URI.create("http://localhost:8080/tasks/subtask/?idEpic=0");
        String substring1 = url2.getQuery().substring(7);
        int epicId1 = Integer.parseInt(substring1);
        SubTask subTask1 = new SubTask("subtask 1", "some description", Status.DONE, 25, "12/40/06/11/2022", epicId1);
        String json2 = gson.toJson(subTask1);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

        Assertions.assertAll(
                () -> Assertions.assertEquals(subTask1.getDuration(), manager.getEpic(0).getDuration(), "Данные не верны"),
                () -> Assertions.assertEquals(subTask1.getStartTime(), manager.getEpic(0).getStartTime(), "Данные не верны")
        );
    }

    @Test
    @DisplayName("Проверка пересечения задач по времени")
    void checkCrossDateAndTimeTasksTest() throws IOException, InterruptedException {
        Assertions.assertTrue(httpTaskServer.manager.getPrioritizedTasks().isEmpty(), "Данные не верны");

        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        Task task1 = new Task("task 1", "some description", Status.NEW, 15, "10/45/06/11/2022");
        String json1 = gson.toJson(task1);
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).POST(body1).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request1, HttpResponse.BodyHandlers.ofString());

        Assertions.assertFalse(httpTaskServer.manager.getPrioritizedTasks().isEmpty(), "Данные не верны");

        Task task2 = new Task("task 1", "some description", Status.NEW, 15, "10/45/06/11/2022");
        String json2 = gson.toJson(task2);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).version(HttpClient.Version.HTTP_1_1).build();
        httpClient.send(request2, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(1, httpTaskServer.manager.getPrioritizedTasks().size(), "Данные не верны");
    }
}


