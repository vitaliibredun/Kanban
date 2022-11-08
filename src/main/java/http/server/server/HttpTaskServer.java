package http.server.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    public TaskManager manager = Managers.getHttpTaskManager();
    private Gson gson = Managers.getGsonBuilder();

    public HttpTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks/task", new TaskHandler());
        server.createContext("/tasks/epic", new EpicHandler());
        server.createContext("/tasks/subtask", new SubtaskHandler());
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }

    class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String requestMethod = httpExchange.getRequestMethod();

            switch (requestMethod) {
                case "GET":
                    String path = httpExchange.getRequestURI().getPath();
                    String query = httpExchange.getRequestURI().getQuery();
                    if (query != null) {
                        URI requestURI = httpExchange.getRequestURI();
                        String substring = requestURI.getQuery().substring(3);
                        int id = Integer.parseInt(substring);
                        Task task = manager.getTask(id);
                        String body = gson.toJson(task);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream responseBody = httpExchange.getResponseBody()) {
                            responseBody.write(body.getBytes(StandardCharsets.UTF_8));
                        }
                        httpExchange.close();
                        break;
                    }
                    if (path.contains("history")) {
                        List prioritizedTasks = manager.getPrioritizedTasks();
                        String body = gson.toJson(prioritizedTasks);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream responseBody = httpExchange.getResponseBody()) {
                            responseBody.write(body.getBytes(StandardCharsets.UTF_8));
                        }
                        httpExchange.close();
                        break;
                    }
                    List<Task> allTasks = manager.getAllTasks();
                    String body = gson.toJson(allTasks);
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream responseBody = httpExchange.getResponseBody()) {
                        responseBody.write(body.getBytes(StandardCharsets.UTF_8));
                    }
                    httpExchange.close();
                    break;

                case "POST":
                    InputStream inputBody = httpExchange.getRequestBody();
                    String stringBody = new String(inputBody.readAllBytes());
                    Task task = gson.fromJson(stringBody, Task.class);
                    manager.addTask(task);
                    httpExchange.sendResponseHeaders(201, 0);
                    httpExchange.close();
                    break;

                case "PUT":
                    InputStream inputBody1 = httpExchange.getRequestBody();
                    String stringBody2 = new String(inputBody1.readAllBytes());
                    Task task1 = gson.fromJson(stringBody2, Task.class);
                    manager.updateTask(task1);
                    httpExchange.sendResponseHeaders(201, 0);
                    httpExchange.close();
                    break;

                case "DELETE":
                    String query1 = httpExchange.getRequestURI().getQuery();
                    if (query1 != null) {
                        URI requestURI = httpExchange.getRequestURI();
                        String substring = requestURI.getQuery().substring(3);
                        int id = Integer.parseInt(substring);
                        manager.removeTask(id);
                        httpExchange.sendResponseHeaders(201, 0);
                        httpExchange.close();
                        break;
                    }
                    manager.deleteAllTasks();
                    httpExchange.sendResponseHeaders(201, 0);
                    httpExchange.close();
                    break;

                default:
                    httpExchange.sendResponseHeaders(404, 0);
                    httpExchange.close();
            }
        }
    }

    class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String requestMethod = httpExchange.getRequestMethod();

            switch (requestMethod) {
                case "GET":
                    String query = httpExchange.getRequestURI().getQuery();
                    if (query != null) {
                        URI requestURI = httpExchange.getRequestURI();
                        String substring = requestURI.getQuery().substring(3);
                        int id = Integer.parseInt(substring);
                        Epic epic = manager.getEpic(id);
                        String body = gson.toJson(epic);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream responseBody = httpExchange.getResponseBody()) {
                            responseBody.write(body.getBytes(StandardCharsets.UTF_8));
                        }
                        httpExchange.close();
                        break;
                    }
                    List<Epic> allEpics = manager.getALLEpics();
                    String body = gson.toJson(allEpics);
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream responseBody = httpExchange.getResponseBody()) {
                        responseBody.write(body.getBytes(StandardCharsets.UTF_8));
                    }
                    httpExchange.close();
                    break;

                case "POST":
                    InputStream inputBody = httpExchange.getRequestBody();
                    String stringBody = new String(inputBody.readAllBytes());
                    Epic epic = gson.fromJson(stringBody, Epic.class);
                    manager.addEpic(epic);
                    httpExchange.sendResponseHeaders(201, 0);
                    httpExchange.close();
                    break;

                case "PUT":
                    InputStream inputBody1 = httpExchange.getRequestBody();
                    String stringBody1 = new String(inputBody1.readAllBytes());
                    Epic epic1 = gson.fromJson(stringBody1, Epic.class);
                    manager.updateEpic(epic1);
                    httpExchange.sendResponseHeaders(201, 0);
                    httpExchange.close();
                    break;

                case "DELETE":
                    String query1 = httpExchange.getRequestURI().getQuery();
                    if (query1 != null) {
                        URI requestURI = httpExchange.getRequestURI();
                        String substring = requestURI.getQuery().substring(3);
                        int id = Integer.parseInt(substring);
                        manager.removeEpic(id);
                        httpExchange.sendResponseHeaders(201, 0);
                        httpExchange.close();
                        break;
                    }
                    manager.deleteAllEpics();
                    httpExchange.sendResponseHeaders(201, 0);
                    httpExchange.close();
                    break;

                default:
                    httpExchange.sendResponseHeaders(404, 0);
                    httpExchange.close();
            }
        }
    }

    class SubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String requestMethod = httpExchange.getRequestMethod();

            switch (requestMethod) {
                case "GET":
                    String query = httpExchange.getRequestURI().getQuery();
                    if (query != null && !query.contains("idEpic")) {
                        URI requestURI = httpExchange.getRequestURI();
                        String substring = requestURI.getQuery().substring(3);
                        int id = Integer.parseInt(substring);
                        SubTask subTask = manager.getSubtask(id);
                        String body = gson.toJson(subTask);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream responseBody = httpExchange.getResponseBody()) {
                            responseBody.write(body.getBytes(StandardCharsets.UTF_8));
                        }
                        httpExchange.close();
                        break;
                    }
                    if (query != null && query.contains("idEpic")) {
                        URI requestURI = httpExchange.getRequestURI();
                        String substring = requestURI.getQuery().substring(7);
                        int id = Integer.parseInt(substring);
                        List<SubTask> subtasksByEpic = manager.getSubtasksByEpic(id);
                        String body = gson.toJson(subtasksByEpic);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream responseBody = httpExchange.getResponseBody()) {
                            responseBody.write(body.getBytes(StandardCharsets.UTF_8));
                        }
                        httpExchange.close();
                        break;
                    }
                    List<SubTask> allSubtasks = manager.getALLSubtasks();
                    String body = gson.toJson(allSubtasks);
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream responseBody = httpExchange.getResponseBody()) {
                        responseBody.write(body.getBytes(StandardCharsets.UTF_8));
                    }
                    httpExchange.close();
                    break;

                case "POST":
                    InputStream inputBody = httpExchange.getRequestBody();
                    String stringBody = new String(inputBody.readAllBytes());
                    SubTask subTask = gson.fromJson(stringBody, SubTask.class);
                    manager.addSubtask(subTask);
                    httpExchange.sendResponseHeaders(201, 0);
                    httpExchange.close();
                    break;

                case "PUT":
                    InputStream inputBody1 = httpExchange.getRequestBody();
                    String stringBody1 = new String(inputBody1.readAllBytes());
                    SubTask subTask1 = gson.fromJson(stringBody1, SubTask.class);
                    manager.updateSubtask(subTask1);
                    httpExchange.sendResponseHeaders(201, 0);
                    httpExchange.close();
                    break;

                case "DELETE":
                    String query1 = httpExchange.getRequestURI().getQuery();
                    if (query1 != null) {
                        URI requestURI = httpExchange.getRequestURI();
                        String substring = requestURI.getQuery().substring(3);
                        int id = Integer.parseInt(substring);
                        manager.removeSubtask(id);
                        httpExchange.sendResponseHeaders(201, 0);
                        httpExchange.close();
                        break;
                    }
                    manager.deleteAllSubtasks();
                    httpExchange.sendResponseHeaders(201, 0);
                    httpExchange.close();
                    break;

                default:
                    httpExchange.sendResponseHeaders(404, 0);
                    httpExchange.close();
            }
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        System.out.println("Остановка сервера на порту " + PORT);
        server.stop(0);
    }
}