package kanban.manager.memory;

import java.io.IOException;
import java.net.URI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kanban.manager.exception.ManagerSaveException;
import kanban.manager.exception.NotFoundException;
import kanban.server.KVTaskClient;
import kanban.manager.adapter.*;
import kanban.manager.file.FileBackedTasksManager;
import kanban.task.*;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
    
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Task.class, new TaskAdapter())
            .registerTypeAdapter(Epic.class, new EpicAdapter())
            .registerTypeAdapter(Subtask.class, new SubtaskAdapter())
            .create();
    
    private void addEpicWithSubtasksFromJson(String json) throws IOException, InterruptedException {
        Subtask subtask;
        Epic epic = gson.fromJson(json, Epic.class);
        int id = epic.getId();
        nextId = Math.max(nextId, id);
        epics.put(id, epic);
        for (int subtaskId: epic.getSubtasksIds()) {
            json = client.load(String.valueOf(subtaskId));
            subtask = gson.fromJson(json, Subtask.class);
            nextId = Math.max(nextId, subtaskId);
            subtasks.put(subtaskId, subtask);
            prioritizedTasks.add(subtask);
        }
    }
    
    private void addTaskFromJson(String json) {
        Task task = gson.fromJson(json, Task.class);
        int id = task.getId();
        nextId = Math.max(nextId, id);
        tasks.put(id, task);
        prioritizedTasks.add(task);
    }
    
    public HttpTaskManager (URI uri) throws IOException, InterruptedException {
        client = new KVTaskClient(uri);
        load("all");
    }
    
    public void load(String key) {
        try {
            String response = client.load(key);
            String[] json;
            switch (key) {
                case "all":
                    json = response.split(";");
                    for (String element : json) {
                        if (element.contains("subtasksIds")) {
                            addEpicWithSubtasksFromJson(element);
                        }
                        if (!(element.contains("subtasksIds") || element.contains("epicId") || element.isEmpty())) {
                            addTaskFromJson(element);
                        }
                    }
                    break;
                case "tasks":
                    json = response.split(";");
                    for (String element : json) {
                        addTaskFromJson(element);
                    }
                    break;
                case "epics":
                    json = response.split(";");
                    for (String element : json) {
                        addEpicWithSubtasksFromJson(element);
                    }
                    break;
                default:
                    if (response.contains("epicId")) {
                        Subtask subtask = gson.fromJson(response, Subtask.class);
                        response = client.load(String.valueOf(subtask.getEpicId()));
                        addEpicWithSubtasksFromJson(response);
                    }
                    if (response.contains("subtasksIds")) {
                        addEpicWithSubtasksFromJson(response);
                    }
                    if (!(response.contains("subtasksIds") || response.contains("epicId"))) {
                        addTaskFromJson(response);
                    }
                    break;
            }
        } catch (NumberFormatException e) {
            throw new NotFoundException("Id has incorrect format.");
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Произошла ошибка при загрузке данных с сервера.");
        }
    }
    
    @Override
    protected void save() {
        try {
            final Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Task.class, new TaskAdapter())
                    .registerTypeAdapter(Epic.class, new EpicAdapter())
                    .registerTypeAdapter(Subtask.class, new SubtaskAdapter())
                    .create();
            String json;
            for (Task task : super.getTasks()) {
                json = gson.toJson(task, Task.class);
                client.put(String.valueOf(task.getId()), json);
            }
            for (Subtask subtask : super.getSubtasks()) {
                json = gson.toJson(subtask, Subtask.class);
                client.put(String.valueOf(subtask.getId()), json);
            }
            for (Epic epic: super.getEpics()) {
                json = gson.toJson(epic, Epic.class);
                client.put(String.valueOf(epic.getId()), json);
            }
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }
}
