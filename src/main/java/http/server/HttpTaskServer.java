package http.server;

import com.sun.net.httpserver.HttpServer;
import handlers.EpicHandler;
import handlers.SubtaskHandler;
import handlers.TaskHandler;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    public TaskManager manager = Managers.getHttpTaskManager();

    public HttpTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks/task", new TaskHandler(manager));
        server.createContext("/tasks/epic", new EpicHandler(manager));
        server.createContext("/tasks/subtask", new SubtaskHandler(manager));
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
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