package kanban.server;

import static java.nio.charset.StandardCharsets.UTF_8;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class KVServer {
    public static final int PORT = 8079;
    private final String apiToken;
    private final HttpServer server;
    private final Map<String, String> data = new HashMap<>();
    
    public KVServer() throws IOException {
        apiToken = generateApiToken();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 1);
        server.createContext("/register", this::register);
        server.createContext("/save", this::save);
        server.createContext("/load", this::load);
    }
    
    private void load(HttpExchange h) throws IOException {
        try {
            if (!hasAuth(h)) {
                System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
                h.sendResponseHeaders(403, 0);
                return;
            }
            RequestMethod method = RequestMethod.valueOf(h.getRequestMethod());
            if (RequestMethod.GET.equals(method)) {
                String key = h.getRequestURI().getPath().substring("/load/".length());
                if (key.isEmpty()) {
                    System.out.println("Key для получения пустой. key указывается в пути: /load/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                String response;
                switch (key) {
                    case "all/":
                        response = String.join(";", data.values());
                        sendText(h, response);
                        break;
                    case "tasks":
                        response = data.values().stream()
                                .filter(element -> !(element.contains("subtasksIds") || element.contains("epicId")))
                                .collect(Collectors.joining(";"));
                        if (response.isEmpty()) {
                            h.sendResponseHeaders(404, 0);
                        } else {
                            sendText(h, response);
                        }
                        break;
                    case "epics":
                        response = data.values().stream()
                                .filter(element -> element.contains("subtasksIds"))
                                .collect(Collectors.joining(";"));
                        if (response.isEmpty()) {
                            h.sendResponseHeaders(404, 0);
                        } else {
                            sendText(h, response);
                        }
                        break;
                    default:
                        if (data.containsKey(key)) {
                            response = data.get(key);
                            sendText(h, response);
                        } else {
                            h.sendResponseHeaders(404, 0);
                        }
                        break;
                }
            }
        } finally {
            h.close();
        }
    }
    
    private void save(HttpExchange h) throws IOException {
        try {
            if (!hasAuth(h)) {
                System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
                h.sendResponseHeaders(403, 0);
                return;
            }
            RequestMethod method = RequestMethod.valueOf(h.getRequestMethod().toUpperCase(Locale.ROOT));
            if (RequestMethod.POST.equals(method)) {
                String key = h.getRequestURI().getPath().substring("/save/".length());
                if (key.isEmpty()) {
                    System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                String value = readText(h);
                if (value.isEmpty()) {
                    System.out.println("Value для сохранения пустой. value указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                data.put(key, value);
                System.out.println("Значение для ключа " + key + " успешно обновлено!");
                h.sendResponseHeaders(200, 0);
            } else {
                System.out.println("/save ждёт POST-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }
    
    private void register(HttpExchange h) throws IOException {
        try {
            if ("GET".equals(h.getRequestMethod())) {
                sendText(h, apiToken);
            } else {
                System.out.println("/register ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }
    
    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        System.out.println("API_TOKEN: " + apiToken);
        server.start();
    }
    
    public void stop() {
        data.clear();
        server.stop(0);
    }
    
    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    }
    
    protected boolean hasAuth(HttpExchange h) {
        String rawQuery = h.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
    }
    
    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }
    
    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
