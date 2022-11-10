package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import constants.RequestMethod;
import constants.ResponseCode;
import manager.Managers;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler implements HttpHandler {
    private TaskManager manager;

    private Gson gson = Managers.getGsonBuilder();

    public TaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        RequestMethod method = RequestMethod.valueOf(requestMethod);

        switch (method) {
            case GET:
                String path = httpExchange.getRequestURI().getPath();
                String query = httpExchange.getRequestURI().getQuery();
                if (query != null) {
                    URI requestURI = httpExchange.getRequestURI();
                    String substring = requestURI.getQuery().substring(3);
                    int id = Integer.parseInt(substring);
                    Task task = manager.getTask(id);
                    String body = gson.toJson(task);
                    httpExchange.sendResponseHeaders(ResponseCode.OK.hashCode(), 0);
                    try (OutputStream responseBody = httpExchange.getResponseBody()) {
                        responseBody.write(body.getBytes(StandardCharsets.UTF_8));
                    }
                    httpExchange.close();
                    break;
                }
                if (path.contains("history")) {
                    List prioritizedTasks = manager.getPrioritizedTasks();
                    String body = gson.toJson(prioritizedTasks);
                    httpExchange.sendResponseHeaders(ResponseCode.OK.hashCode(), 0);
                    try (OutputStream responseBody = httpExchange.getResponseBody()) {
                        responseBody.write(body.getBytes(StandardCharsets.UTF_8));
                    }
                    httpExchange.close();
                    break;
                }
                List<Task> allTasks = manager.getAllTasks();
                String body = gson.toJson(allTasks);
                httpExchange.sendResponseHeaders(ResponseCode.OK.hashCode(), 0);
                try (OutputStream responseBody = httpExchange.getResponseBody()) {
                    responseBody.write(body.getBytes(StandardCharsets.UTF_8));
                }
                httpExchange.close();
                break;

            case POST:
                InputStream inputBody = httpExchange.getRequestBody();
                String stringBody = new String(inputBody.readAllBytes());
                Task task = gson.fromJson(stringBody, Task.class);
                manager.addTask(task);
                httpExchange.sendResponseHeaders(ResponseCode.CREATED.hashCode(), 0);
                httpExchange.close();
                break;

            case PUT:
                InputStream inputBody1 = httpExchange.getRequestBody();
                String stringBody2 = new String(inputBody1.readAllBytes());
                Task task1 = gson.fromJson(stringBody2, Task.class);
                manager.updateTask(task1);
                httpExchange.sendResponseHeaders(ResponseCode.CREATED.hashCode(), 0);
                httpExchange.close();
                break;

            case DELETE:
                String query1 = httpExchange.getRequestURI().getQuery();
                if (query1 != null) {
                    URI requestURI = httpExchange.getRequestURI();
                    String substring = requestURI.getQuery().substring(3);
                    int id = Integer.parseInt(substring);
                    manager.removeTask(id);
                    httpExchange.sendResponseHeaders(ResponseCode.CREATED.hashCode(), 0);
                    httpExchange.close();
                    break;
                }
                manager.deleteAllTasks();
                httpExchange.sendResponseHeaders(ResponseCode.CREATED.hashCode(), 0);
                httpExchange.close();
                break;

            default:
                httpExchange.sendResponseHeaders(ResponseCode.NOT_FOUND.hashCode(), 0);
                httpExchange.close();
        }
    }
}
