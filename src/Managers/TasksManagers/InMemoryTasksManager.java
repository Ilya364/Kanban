package Managers.TasksManagers;

import java.util.*;
import Tasks.*;
import Managers.HistoryManagers.HistoryManager;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class InMemoryTasksManager implements TasksManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected int nextId = 0;
    protected final Set<Task> prioritizedTasks = new TreeSet<>(new TasksByStartTimeComparator());

    private void removeTasksFromHistory(TaskType taskType) {
        switch (taskType) {
            case TASK:
                for (Integer elementId: tasks.keySet()) {
                    historyManager.remove(elementId);
                }
                break;
            case EPIC:
                for (Integer elementId: epics.keySet()) {
                    historyManager.remove(elementId);
                }
                for (Integer elementId: subtasks.keySet()) {
                    historyManager.remove(elementId);
                }
                break;
            case SUBTASK:
                for (Integer elementId: subtasks.keySet()) {
                    historyManager.remove(elementId);
                }
                break;
        }
    }

    private void determineStatus(Epic epic) {
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

    public void determineStartEndDuration(Epic epic) {
        LocalDateTime minTime = LocalDateTime.MAX;
        LocalDateTime maxTime = LocalDateTime.MIN;
        Duration duration = Duration.ofMinutes(0);
        boolean isMinTimeFound = false;
        boolean isMaxTimeFound = false;
        for (Integer subtask: epic.getSubtasksIds()) {
            if (subtasks.get(subtask).getStartTime() == null) {
                continue;
            }
            if (subtasks.get(subtask).getStartTime().isBefore(minTime)) {
                minTime = subtasks.get(subtask).getStartTime();
                isMinTimeFound = true;
            }
            if (subtasks.get(subtask).getStartTime().isAfter(maxTime)) {
                maxTime = subtasks.get(subtask).getEndTime();
                isMaxTimeFound = true;
            }
            duration = duration.plus(subtasks.get(subtask).getDuration());
        }
        if (isMinTimeFound && isMaxTimeFound) {
            epic.setStartTime(minTime);
            epic.setEndTime(maxTime);
            epic.setDuration(duration);
        }
    }

    public boolean isTaskTimeValid(Task task) {
        if (task.getStartTime() == null) {
            return true;
        }
        ArrayList<Task> addedTasks = getPrioritizedTasks();
        int size = addedTasks.size();
        if (size == 0) {
            return true;
        }

        int indexOfLastTaskWithStartTime = -1;
        for (Task element: addedTasks) {
            if (element.getStartTime() != null) {
                indexOfLastTaskWithStartTime++;
            } else {
                break;
            }
        }
        if (indexOfLastTaskWithStartTime == -1) {
            return true;
        }

        final LocalDateTime earliest = addedTasks.get(0).getStartTime();
        final LocalDateTime latest = addedTasks.get(indexOfLastTaskWithStartTime).getEndTime();
        final LocalDateTime taskStartTime = task.getStartTime();
        final LocalDateTime taskEndTime = task.getEndTime();
        if (taskEndTime.isBefore(earliest) || taskStartTime.isAfter(latest)) {
            return true;
        } else if (size == 1) {
            return false;
        }
        for (int i = 0; i < indexOfLastTaskWithStartTime; i++) {
            LocalDateTime prevTaskEndTime = addedTasks.get(i).getEndTime();
            LocalDateTime nextTaskStartTime = addedTasks.get(i + 1).getStartTime();
            if (taskStartTime.isAfter(prevTaskEndTime) && taskEndTime.isBefore(nextTaskStartTime)) {
                return true;
            }
        }
        return false;
    }

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
    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public void removeAllTasks() {
        removeTasksFromHistory(TaskType.TASK); //
        tasks.clear();
        prioritizedTasks.retainAll(prioritizedTasks.stream()
                .filter(element -> element instanceof Subtask)
                .collect(Collectors.toList()));
    }

    @Override
    public void removeAllEpics() {
        removeTasksFromHistory(TaskType.SUBTASK);
        removeTasksFromHistory(TaskType.EPIC);
        subtasks.clear();
        epics.clear();
        prioritizedTasks.retainAll(prioritizedTasks.stream()
                .filter(element -> !(element instanceof Subtask))
                .collect(Collectors.toList()));
    }

    @Override
    public void removeAllSubtasks() {
        removeTasksFromHistory(TaskType.SUBTASK);
        subtasks.clear();
        for (Epic epic: epics.values()) {
            epic.removeAllSubtasksIds();
            determineStatus(epic);
            determineStartEndDuration(epic);
        }
        prioritizedTasks.retainAll(prioritizedTasks.stream()
                .filter(element -> !(element instanceof Subtask))
                .collect(Collectors.toList()));
    }

    @Override
    public void addTask(Task task) {
        task.setId(nextId++);
        if (isTaskTimeValid(task)) {
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            subtask.setId(nextId++);
            if (isTaskTimeValid(subtask)) {
                Epic epic = epics.get(subtask.getEpicId());
                epic.addSubtaskId(subtask.getId());
                subtasks.put(subtask.getId(), subtask);
                determineStatus(epic);
                determineStartEndDuration(epic);
                prioritizedTasks.add(subtask);
            }
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId()) && isTaskTimeValid(task)) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId()) && isTaskTimeValid(subtask)) {
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
        if (tasks.containsKey(id)) {
            prioritizedTasks.remove(tasks.get(id));
            tasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeSubtask(int id) {
        if (subtasks.containsKey(id)) {
            Epic epic = epics.get(subtasks.get(id).getEpicId());
            epic.removeSubtaskId(id);
            determineStatus(epic);
            determineStartEndDuration(epic);
            prioritizedTasks.remove(subtasks.get(id));
            subtasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            ArrayList<Integer> subtaskIds = new ArrayList<>(epic.getSubtasksIds());
            for (Integer subtaskId : subtaskIds) {
                prioritizedTasks.remove(subtasks.get(subtaskId));
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            epics.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int id) {
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        if (epics.containsKey(id)) {
            for (Integer subtasksId : epics.get(id).getSubtasksIds()) {
                subtasksByEpic.add(subtasks.get(subtasksId));
                historyManager.add(subtasks.get(subtasksId));
            }
        }
        return subtasksByEpic;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
