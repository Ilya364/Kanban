import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int nextId = 0;

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
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
            epic.getSubtasksIds().clear();
            determineStatus(epic);
        }
    }

    public void addTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    public void addTask(Subtask subtask) {
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
    }

    public void addTask(Epic epic) {
        epic.setId(nextId++);
        for (Integer id: epic.getSubtasksIds()) {
            subtasks.get(id).setEpicId(epic.getId());
        }
        determineStatus(epic);
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
        tasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        determineStatus(epics.get(subtask.getEpicId()));
    }

    public void updateEpic(Epic epic) {
        for (Integer subtasksId : epic.getSubtasksIds()) {
            subtasks.get(subtasksId).setEpicId(epic.getId());
        }
        determineStatus(epic);
        epics.put(epic.getId(), epic);
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeSubtask(int id) {
        epics.get(subtasks.get(id).getEpicId()).getSubtasksIds().remove(id);
        determineStatus(epics.get(subtasks.get(id).getEpicId()));
        subtasks.remove(id);
    }

    public void removeEpic(int id) {
        for (Integer subtaskId: epics.get(id).getSubtasksIds()) {
            if (subtasks.get(subtaskId).getEpicId() == id) {
                subtasks.remove(subtaskId);
            }
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
            epic.setStatus(Task.Status.NEW);
            return;
        }
        int doneCounter = 0;
        int newCounter = 0;
        for (Integer id: epic.getSubtasksIds()) {
            if (subtasks.get(id).getStatus() == Task.Status.DONE) {
                doneCounter++;
            } else if (subtasks.get(id).getStatus() == Task.Status.NEW) {
                newCounter++;
            }

            if (doneCounter == epic.getSubtasksIds().size()) {
                epic.setStatus(Task.Status.DONE);
            } else if (newCounter == epic.getSubtasksIds().size()) {
                epic.setStatus(Task.Status.NEW);
            } else {
                epic.setStatus(Task.Status.IN_PROGRESS);
            }
        }
    }
}
