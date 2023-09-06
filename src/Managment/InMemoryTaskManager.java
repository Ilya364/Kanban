package Managment;

import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager{
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int nextId = 0;

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.clear();
        for (Epic epic: epics.values()) {
            epic.removeAllSubtasksIds();
            determineStatus(epic);
        }
    }

    @Override
    public void addTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtaskId(subtask.getId());
        determineStatus(epic);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            determineStatus(epics.get(subtask.getEpicId()));
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void removeTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void removeSubtask(int id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        epic.deleteSubtaskId(id);
        determineStatus(epic);
        subtasks.remove(id);
    }

    @Override
    public void removeEpic(int id) {
        Epic epic = epics.get(id);
        ArrayList<Integer> subtaskIds = new ArrayList<>(epic.getSubtasksIds());
        for (Integer subtaskId : subtaskIds) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int id) {
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        for (Integer subtasksId : epics.get(id).getSubtasksIds()) {
            subtasksByEpic.add(subtasks.get(subtasksId));
        }
        return subtasksByEpic;
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void determineStatus(Epic epic) {
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
