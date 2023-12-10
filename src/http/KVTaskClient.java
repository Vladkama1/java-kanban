package http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;
    private final String apiKey;
    private HttpClient client;

    public KVTaskClient(String url) {
        this.url = url;
        client = HttpClient.newHttpClient();
        apiKey = register();
    }

    private String register() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "/register"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    public void put(String key, String json) {
        try {
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
                       HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/save" + key + "?API_TOKEN=" + apiKey))
                    .POST(body)

                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public String load(String key) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "/load" + key + "?API_TOKEN=" + apiKey))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}