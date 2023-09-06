package Managment;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;

public interface TaskManager {
    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Epic> getEpics();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    void addTask(Task task);

    void addSubtask(Subtask subtask);

    void addEpic(Epic epic);

    Task getTask(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void removeTask(int id);

    void removeSubtask(int id);

    void removeEpic(int id);

    ArrayList<Subtask> getEpicSubtasks(int id);

    ArrayList<Task> getHistory();
}
