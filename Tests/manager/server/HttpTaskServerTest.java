package manager.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kanban.manager.adapter.EpicAdapter;
import kanban.manager.adapter.SubtaskAdapter;
import kanban.manager.adapter.TaskAdapter;
import kanban.server.HttpTasksServer;
import kanban.server.KVServer;
import kanban.task.Epic;
import kanban.task.Status;
import kanban.task.Subtask;
import kanban.task.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskServerTest {
    private static KVServer dataServer;
    private static HttpTasksServer handleServer;
    
    private static HttpClient client = HttpClient.newBuilder().build();
    
    
    @BeforeAll
    public static void createAll() throws IOException, InterruptedException {
        dataServer = new KVServer();
        dataServer.start();
        handleServer = new HttpTasksServer();
        handleServer.start();
    }
    @AfterEach
    public void stopStartDataServer() throws IOException, InterruptedException {
        dataServer.stop();
        handleServer.stop();
        dataServer = new KVServer();
        dataServer.start();
        handleServer = new HttpTasksServer();
        handleServer.start();
    }
    
    @AfterAll
    public static void stopAll() {
        dataServer.stop();
        handleServer.stop();
    }
    
    @Test
    public void shouldAddReturnTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .setPrettyPrinting()
                .create();
        Task task = new Task("name1", "desc1", Status.IN_PROGRESS);
        String json = gson.toJson(task);
        
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(response.statusCode(), 204);
        
        uri = URI.create(uri + "/?id=" + task.getId());
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(response.statusCode(), 200);
        assertEquals(response.body(), json);
    }
    
    @Test
    public void shouldAddReturnTaskWithStartTime() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .setPrettyPrinting()
                .create();
        Task task = new Task("name1", "desc1", Status.IN_PROGRESS,
                LocalDateTime.of(2023, 11, 11, 15, 20), 60);
        String json = gson.toJson(task);
        
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(response.statusCode(), 204);
        
        uri = URI.create(uri + "/?id=" + task.getId());
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(response.statusCode(), 200);
        assertEquals(response.body(), json);
    }
    
    @Test
    public void shouldAddReturnEpic() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .setPrettyPrinting()
                .create();
        Epic epic = new Epic("name1", "desc1");
        String json = gson.toJson(epic);
        
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(response.statusCode(), 204);
        
        uri = URI.create(uri + "/?id=" + epic.getId());
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(response.statusCode(), 200);
        assertEquals(response.body(), json);
    }
    
    @Test
    public void shouldAddReturnSubtask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .registerTypeAdapter(Subtask.class, new SubtaskAdapter())
                .setPrettyPrinting()
                .create();
        Epic epic = new Epic("name1", "desc1");
        String json = gson.toJson(epic);
        
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        Subtask subtask = new Subtask("name2", "desc2", Status.NEW, epic.getId());
        json = gson.toJson(subtask);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/subtask")).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(response.statusCode(), 204);
        
        uri = URI.create("http://localhost:8080/tasks/subtask/?id=1");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        subtask.setId(1);
        json = gson.toJson(subtask);
        
        assertEquals(response.statusCode(), 200);
        assertEquals(response.body(), json);
    }
    
    @Test
    public void shouldAddReturnSubtaskWithStartTime() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .registerTypeAdapter(Subtask.class, new SubtaskAdapter())
                .setPrettyPrinting()
                .create();
        Epic epic = new Epic("name1", "desc1");
        String json = gson.toJson(epic);
        
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        Subtask subtask = new Subtask("name2", "desc2", Status.NEW, epic.getId(),
                LocalDateTime.of(2023, 11, 11, 15, 20), 40);
        json = gson.toJson(subtask);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/subtask")).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(response.statusCode(), 204);
        
        uri = URI.create("http://localhost:8080/tasks/subtask/?id=1");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        subtask.setId(1);
        json = gson.toJson(subtask);
        
        assertEquals(response.statusCode(), 200);
        assertEquals(response.body(), json);
    }
    
    @Test
    public void shouldUpdateTask() throws IOException, InterruptedException {
        URI u = URI.create("http://localhost:8080/tasks/epic/?id=0");
        HttpRequest r = HttpRequest.newBuilder().uri(u).GET().build();
        System.out.println(client.send(r, HttpResponse.BodyHandlers.ofString()).body());
        
        URI uri = URI.create("http://localhost:8080/tasks/task");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .setPrettyPrinting()
                .create();
        Task task = new Task("name1", "desc1", Status.IN_PROGRESS);
        Task task2 = new Task("name2", "desc2", Status.DONE);
        
        String json = gson.toJson(task);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        task2.setId(task.getId());
        json = gson.toJson(task2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(response.statusCode(), 204);
        
        uri = URI.create(uri + "/?id=" + 0);
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        
        assertEquals(response.statusCode(), 200);
        assertEquals(response.body(), json);
    }
    
    @Test
    public void shouldUpdateSubtask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .setPrettyPrinting()
                .registerTypeAdapter(Subtask.class, new SubtaskAdapter())
                .create();
        Epic epic = new Epic("name1", "desc1");
        String json = gson.toJson(epic);
        
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        Subtask subtask = new Subtask("name2", "desc2", Status.NEW, epic.getId());
        json = gson.toJson(subtask);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/subtask")).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        Subtask subtask2 = new Subtask("name3", "desc3", Status.IN_PROGRESS, epic.getId());
        subtask2.setId(1);
        json = gson.toJson(subtask2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/subtask")).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(response.statusCode(), 204);
        
        uri = URI.create("http://localhost:8080/tasks/subtask/?id=" + subtask2.getId());
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(response.statusCode(), 200);
        assertEquals(response.body(), json);
    }
    
    @Test
    public void shouldUpdateEpic() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .setPrettyPrinting()
                .create();
        Epic epic = new Epic("name1", "desc1");
        Epic epic2 = new Epic("name2", "desc2");
        
        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        epic2.setId(epic.getId());
        json = gson.toJson(epic2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(response.statusCode(), 204);
        
        uri = URI.create(uri + "/?id=" + epic.getId());
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(response.statusCode(), 200);
        assertEquals(response.body(), json);
    }
    
    @Test
    public void shouldRemoveTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task");
        Gson gson = new GsonBuilder().registerTypeAdapter(Task.class, new TaskAdapter()).create();
        Task task = new Task("name1", "desc1", Status.IN_PROGRESS);
        String json = gson.toJson(task);
        
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        uri = URI.create(uri + "/?id=0");
        request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(response.statusCode(), 204);
        
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(response.statusCode(), 404);
    }
    
    @Test
    public void shouldRemoveSubtask() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .registerTypeAdapter(Subtask.class, new SubtaskAdapter())
                .create();
        
        Epic epic = new Epic("name1", "desc1");
        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        Subtask subtask = new Subtask("name2", "desc2", Status.NEW, epic.getId());
        json = gson.toJson(subtask);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/subtask")).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        uri = URI.create(uri + "/?id=0");
        request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(response.statusCode(), 204);
        
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(response.statusCode(), 404);
    }
    
    @Test
    public void shouldRemoveEpic() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .registerTypeAdapter(Subtask.class, new SubtaskAdapter())
                .create();
        
        Epic epic = new Epic("name1", "desc1");
        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        uri = URI.create(uri + "/?id=0");
        request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(response.statusCode(), 204);
        
        uri = URI.create(uri + "/?id=" + epic.getId());
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(response.statusCode(), 404);
    }
    
    @Test
    public void shouldReturnEpicSubtasks() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .registerTypeAdapter(Subtask.class, new SubtaskAdapter())
                .setPrettyPrinting()
                .create();
        Epic epic = new Epic("name1", "desc1");
        String json = gson.toJson(epic);
        
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        Subtask subtask = new Subtask("name2", "desc2", Status.NEW, epic.getId());
        json = gson.toJson(subtask);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/subtask")).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(response.statusCode(), 204);
        
        uri = URI.create("http://localhost:8080/tasks/subtask/epic/?id=0");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        subtask.setId(1);
        List<Task> subtasks = List.of(subtask);
        json = gson.toJson(subtasks);
        
        assertEquals(response.statusCode(), 200);
        assertEquals(response.body(), json);
    }
    
    @Test
    public void shouldReturnPrioritizedTasks() throws IOException, InterruptedException {
        Task task = new Task("name", "desc", Status.NEW,
                LocalDateTime.of(2023, 11, 11, 15, 20), 60);
        Epic epic = new Epic("name", "desc");
        Subtask subtask1 = new Subtask("name", "desc", Status.NEW, 1);
        Subtask subtask2 = new Subtask("name", "desc", Status.NEW, 1,
                LocalDateTime.of(2024, 11, 11, 15, 20), 60);
        
        URI addTask = URI.create("http://localhost:8080/tasks/task");
        URI addEpic = URI.create("http://localhost:8080/tasks/epic");
        URI addSubtask = URI.create("http://localhost:8080/tasks/subtask");
        URI returnPrioritized = URI.create("http://localhost:8080/tasks");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .registerTypeAdapter(Subtask.class, new SubtaskAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .setPrettyPrinting()
                .create();
        String json;
        HttpRequest.BodyPublisher body;
        
        json = gson.toJson(task);
        body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(addTask).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        epic.setId(-1);
        json = gson.toJson(epic);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(addEpic).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        subtask1.setId(-1);
        json = gson.toJson(subtask1);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(addSubtask).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        subtask2.setId(-1);
        json = gson.toJson(subtask2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(addSubtask).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        request = HttpRequest.newBuilder().uri(returnPrioritized).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        epic.setId(1);
        subtask1.setId(2);
        subtask2.setId(3);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        tasks.add(subtask2);
        tasks.add(subtask1);
        json = gson.toJson(tasks, ArrayList.class);
        
        assertEquals(response.statusCode(), 200);
        assertEquals(json, response.body());
    }
    
    @Test
    public void shouldReturnHistory() throws IOException, InterruptedException {
        Task task1 = new Task("name", "desc", Status.NEW);
        Task task2 = new Task("name", "desc", Status.NEW);
        Task task3 = new Task("name", "desc", Status.NEW);
        URI addTask = URI.create("http://localhost:8080/tasks/task");
        URI getTask = URI.create("http://localhost:8080/tasks/task/?id=");
        URI getHistory = URI.create("http://localhost:8080/tasks/history");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .setPrettyPrinting()
                .create();
        String json;
        HttpRequest.BodyPublisher body;
        
        json = gson.toJson(task1);
        body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(addTask).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        task2.setId(-1);
        json = gson.toJson(task2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(addTask).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        task3.setId(-1);
        json = gson.toJson(task3);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(addTask).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        request = HttpRequest.newBuilder().uri(URI.create(getTask.toString() + 1)).GET().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder().uri(URI.create(getTask.toString() + 2)).GET().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder().uri(URI.create(getTask.toString() + 0)).GET().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        request = HttpRequest.newBuilder().uri(getHistory).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        task2.setId(1);
        task3.setId(2);
        List<Task> history = new ArrayList<>();
        history.add(task2);
        history.add(task3);
        history.add(task1);
        json = gson.toJson(history, ArrayList.class);
        
        assertEquals(response.statusCode(), 200);
        assertEquals(response.body(), json);
    }
    
    @Test
    public void shouldRemoveAllTasks() throws IOException, InterruptedException {
        Task task1 = new Task("name1", "desc", Status.NEW);
        Task task2 = new Task("name2", "desc", Status.NEW);
        Task task3 = new Task("name3", "desc", Status.NEW);
        URI addTask = URI.create("http://localhost:8080/tasks/task");
        URI getTask = URI.create("http://localhost:8080/tasks/task/?id=");
        URI removeAll = URI.create("http://localhost:8080/tasks");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .create();
        String json;
        HttpRequest.BodyPublisher body;
        
        json = gson.toJson(task1);
        body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(addTask).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        task2.setId(-1);
        json = gson.toJson(task2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(addTask).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        task3.setId(-1);
        json = gson.toJson(task3);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(addTask).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        request = HttpRequest.newBuilder().uri(removeAll).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        
        request = HttpRequest.newBuilder().uri(URI.create(getTask.toString() + 0)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 404);
        
        request = HttpRequest.newBuilder().uri(URI.create(getTask.toString() + 1)).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 404);
        
        request = HttpRequest.newBuilder().uri(URI.create(getTask.toString() + 2)).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 404);
    }
    
    @Test
    public void shouldReturn404WhenInvalidResource() throws IOException, InterruptedException {
        URI badUri1 = URI.create("http://localhost:8080/tasks/tasks");
        URI badUri2 = URI.create("http://localhost:8080/tasks/subtask/epiccc/?id=0");
        URI badUri3 = URI.create("http://localhost:8080/tasks/subtosk/?id=0");
        
        HttpRequest request = HttpRequest.newBuilder().GET().uri(badUri1).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(404, response.statusCode());
        
        request = HttpRequest.newBuilder().GET().uri(badUri2).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(404, response.statusCode());
        
        request = HttpRequest.newBuilder().GET().uri(badUri3).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(404, response.statusCode());
    }
    
    @Test
    public void shouldReturn404WhenInvalidIdInQuery() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=i");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(404, response.statusCode());
    }
    
    @Test
    public void shouldReturn405WhenInvalidMethod() throws IOException, InterruptedException {
        URI getOrRemoveAll = URI.create("http://localhost:8080/tasks");
        URI getHistory = URI.create("http://localhost:8080/tasks/history");
        URI getOrDeleteTask = URI.create("http://localhost:8080/tasks/task/?id=0");
        URI getOrDeleteSubtask = URI.create("http://localhost:8080/tasks/subtask/?id=0");
        URI getOrDeleteEpic = URI.create("http://localhost:8080/tasks/epic/?id=0");
        URI addTask = URI.create("http://localhost:8080/tasks/task");
        URI addEpic = URI.create("http://localhost:8080/tasks/epic");
        URI addSubtask = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request;
        HttpResponse<String> response;
        
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString("");
        request = HttpRequest.newBuilder().POST(bodyPublisher).uri(getOrRemoveAll).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode());
        
        request = HttpRequest.newBuilder().POST(bodyPublisher).uri(getOrDeleteTask).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode());
        
        request = HttpRequest.newBuilder().POST(bodyPublisher).uri(getOrDeleteSubtask).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode());
        
        request = HttpRequest.newBuilder().POST(bodyPublisher).uri(getOrDeleteEpic).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode());
        
        request = HttpRequest.newBuilder().DELETE().uri(getHistory).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode());
        
        request = HttpRequest.newBuilder().DELETE().uri(addTask).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode());
        
        request = HttpRequest.newBuilder().DELETE().uri(addEpic).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode());
        
        request = HttpRequest.newBuilder().DELETE().uri(addSubtask).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode());
    }
}
