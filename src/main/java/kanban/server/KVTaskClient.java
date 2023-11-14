package kanban.server;

import kanban.manager.exception.GetTokenException;
import kanban.manager.exception.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String token;
    private final URI url;
    public KVTaskClient(URI url) throws IOException, InterruptedException {
        this.url = url;
        URI urlForRegister = URI.create(this.url + "register");
        HttpRequest request = HttpRequest.newBuilder().uri(urlForRegister).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            token = response.body();
        } else {
            throw new GetTokenException("The token was not received. Error code: " + response.statusCode());
        }
    }
    
    public void put(String key, String json) throws IOException, InterruptedException {
        URI urlForSave = URI.create(url + "save/" + key + "/?API_TOKEN=" + token);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(urlForSave).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new ManagerSaveException(
                    "Json was not saved by key " + "\"" + key + "\"" + ". Error code: " + response.statusCode());
        }
    }
    
    public String load(String key) throws IOException, InterruptedException {
        URI urlForLoad = URI.create(url + "load/" + key + "/?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder().uri(urlForLoad).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new ManagerSaveException(
                    "An error occurred when uploading by key " + "\"" + key + "\"" + ". Error code: "
                            + response.statusCode());
        }
    }
}
