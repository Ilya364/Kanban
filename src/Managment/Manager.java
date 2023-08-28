package Managment;

import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int nextId = 0;

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    public void removeAllSubtasks() {
        subtasks.clear();
        for (Epic epic: epics.values()) {
            epic.removeAllSubtasksIds();
            determineStatus(epic);
        }
    }

    public void addTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    public void addSubtask(Subtask subtask) {
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtaskId(subtask.getId());
        determineStatus(epic);
    }

    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            determineStatus(epics.get(subtask.getEpicId()));
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeSubtask(int id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        epic.deleteSubtaskId(id);
        determineStatus(epic);
        subtasks.remove(id);
    }

    public void removeEpic(int id) {
        Epic epic = epics.get(id);
        ArrayList<Integer> subtaskIds = new ArrayList<>(epic.getSubtasksIds());
        for (Integer subtaskId : subtaskIds) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    public ArrayList<Subtask> getEpicSubtasks(int id) {
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        for (Integer subtasksId : epics.get(id).getSubtasksIds()) {
            subtasksByEpic.add(subtasks.get(subtasksId));
        }
        return subtasksByEpic;
    }

    public void determineStatus(Epic epic) {
        if (epic.getSubtasksIds().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        int doneCounter = 0;
        int newCounter = 0;
        for (Integer id: epic.getSubtasksIds()) {
            if (subtasks.get(id).getStatus() == Status.DONE) {
                doneCounter++;
            } else if (subtasks.get(id).getStatus() == Status.NEW) {
                newCounter++;
            }

            if (doneCounter == epic.getSubtasksIds().size()) {
                epic.setStatus(Status.DONE);
            } else if (newCounter == epic.getSubtasksIds().size()) {
                epic.setStatus(Status.NEW);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }
}
