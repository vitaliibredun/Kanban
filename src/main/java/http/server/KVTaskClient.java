package http.server;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;
    private final String apiToken;

    public KVTaskClient() {
        url = "http://localhost:8078/";
        apiToken = register(url);
    }

    private String register(String url) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url + "register"))
                    .GET()
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return send.body();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public void save(String key, String value) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url + "save" + key + "?API_TOKEN=" + apiToken))
                    .POST(HttpRequest.BodyPublishers.ofString(value))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public String load(String key) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url + "load" + key + "?API_TOKEN=" + apiToken))
                    .GET()
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return httpResponse.body();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
