package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import constants.RequestMethod;
import constants.ResponseCode;
import manager.Managers;
import manager.TaskManager;
import tasks.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler implements HttpHandler {
    private TaskManager manager;

    private Gson gson = Managers.getGsonBuilder();

    public SubtaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        RequestMethod method = RequestMethod.valueOf(requestMethod);

        switch (method) {
            case GET:
                String query = httpExchange.getRequestURI().getQuery();
                if (query != null && !query.contains("idEpic")) {
                    URI requestURI = httpExchange.getRequestURI();
                    String substring = requestURI.getQuery().substring(3);
                    int id = Integer.parseInt(substring);
                    SubTask subTask = manager.getSubtask(id);
                    String body = gson.toJson(subTask);
                    httpExchange.sendResponseHeaders(ResponseCode.OK.hashCode(), 0);
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
                    httpExchange.sendResponseHeaders(ResponseCode.OK.hashCode(), 0);
                    try (OutputStream responseBody = httpExchange.getResponseBody()) {
                        responseBody.write(body.getBytes(StandardCharsets.UTF_8));
                    }
                    httpExchange.close();
                    break;
                }
                List<SubTask> allSubtasks = manager.getALLSubtasks();
                String body = gson.toJson(allSubtasks);
                httpExchange.sendResponseHeaders(ResponseCode.OK.hashCode(), 0);
                try (OutputStream responseBody = httpExchange.getResponseBody()) {
                    responseBody.write(body.getBytes(StandardCharsets.UTF_8));
                }
                httpExchange.close();
                break;

            case POST:
                InputStream inputBody = httpExchange.getRequestBody();
                String stringBody = new String(inputBody.readAllBytes());
                SubTask subTask = gson.fromJson(stringBody, SubTask.class);
                manager.addSubtask(subTask);
                httpExchange.sendResponseHeaders(ResponseCode.CREATED.hashCode(), 0);
                httpExchange.close();
                break;

            case PUT:
                InputStream inputBody1 = httpExchange.getRequestBody();
                String stringBody1 = new String(inputBody1.readAllBytes());
                SubTask subTask1 = gson.fromJson(stringBody1, SubTask.class);
                manager.updateSubtask(subTask1);
                httpExchange.sendResponseHeaders(ResponseCode.CREATED.hashCode(), 0);
                httpExchange.close();
                break;

            case DELETE:
                String query1 = httpExchange.getRequestURI().getQuery();
                if (query1 != null) {
                    URI requestURI = httpExchange.getRequestURI();
                    String substring = requestURI.getQuery().substring(3);
                    int id = Integer.parseInt(substring);
                    manager.removeSubtask(id);
                    httpExchange.sendResponseHeaders(ResponseCode.CREATED.hashCode(), 0);
                    httpExchange.close();
                    break;
                }
                manager.deleteAllSubtasks();
                httpExchange.sendResponseHeaders(ResponseCode.CREATED.hashCode(), 0);
                httpExchange.close();
                break;

            default:
                httpExchange.sendResponseHeaders(ResponseCode.NOT_FOUND.hashCode(), 0);
                httpExchange.close();
        }
    }
}
