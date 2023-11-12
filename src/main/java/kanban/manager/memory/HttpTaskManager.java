package kanban.manager.memory;

import java.io.IOException;
import java.net.URI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kanban.manager.exception.ManagerSaveException;
import kanban.server.KVTaskClient;
import kanban.manager.adapter.*;
import kanban.manager.file.FileBackedTasksManager;
import kanban.task.*;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
    public HttpTaskManager (URI uri) throws IOException, InterruptedException {
        client = new KVTaskClient(uri);
        load();
    }
    
    public void load() {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Task.class, new TaskAdapter())
                    .registerTypeAdapter(Epic.class, new EpicAdapter())
                    .registerTypeAdapter(Subtask.class, new SubtaskAdapter())
                    .create();
            final String response = client.load("all");
            final String[] jsonTasks = response.split(";");
            for (String jsonTask: jsonTasks) {
                int id = -1;
                if (jsonTask.contains("subtasksIds")) {
                    Epic epic = gson.fromJson(jsonTask, Epic.class);
                    id = epic.getId();
                    epics.put(id, epic);
                    System.out.println(epic.subtasksIds);
                } else if (jsonTask.contains("epicId")) {
                    Subtask subtask = gson.fromJson(jsonTask, Subtask.class);
                    id = subtask.getId();
                    subtasks.put(id, subtask);
                    prioritizedTasks.add(subtask);
                } else if (!jsonTask.isEmpty()) {
                    Task task = gson.fromJson(jsonTask, Task.class);
                    id = task.getId();
                    tasks.put(id, task);
                    prioritizedTasks.add(task);
                }
                if (nextId <= id) {
                    nextId = id + 1;
                }
            }
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
                client.put("TASK" + task.getId(), json);
            }
            for (Subtask subtask : super.getSubtasks()) {
                json = gson.toJson(subtask, Subtask.class);
                client.put("SUBTASK" + subtask.getId(), json);
            }
            for (Epic epic: super.getEpics()) {
                json = gson.toJson(epic, Epic.class);
                client.put("EPIC" + epic.getId(), json);
            }
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }
}
