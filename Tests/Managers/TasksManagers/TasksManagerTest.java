package Managers.TasksManagers;

import Tasks.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract public class TasksManagerTest <T extends TasksManager> {
    protected T manager;

    @Test
    void shouldAddAndReturnListOfTasks() {
        Task task1 = new Task("name", "description", Status.NEW);
        Task task2 = new Task("name", "description", Status.NEW);
        Task task3 = new Task("name", "description", Status.NEW);
        List<Task> tasks = List.of(task1, task2, task3);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        assertIterableEquals(tasks, manager.getTasks());
    }

    @Test
    void shouldReturnEmptyListOfTasks() {
        assertIterableEquals(new ArrayList<>(), manager.getTasks());
    }

    @Test
    void shouldAddAndReturnListOfEpics() {
        Epic epic1 = new Epic("name", "description");
        Epic epic2 = new Epic("name", "description");
        Epic epic3 = new Epic("name", "description");
        List<Epic> epics = List.of(epic1, epic2, epic3);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);

        assertIterableEquals(epics, manager.getEpics());
    }

    @Test
    void shouldReturnEmptyListOfEpics() {
        assertIterableEquals(new ArrayList<>(), manager.getEpics());
    }

    @Test
    void shouldAddAndReturnListOfSubtasks() {
        Epic epic = new Epic("name", "subtask");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("name", "description", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("name", "description", Status.NEW, epic.getId());
        Subtask subtask3 = new Subtask("name", "description", Status.NEW, epic.getId());
        List<Subtask> subtasks = List.of(subtask1, subtask2, subtask3);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        assertIterableEquals(subtasks, manager.getSubtasks());
    }

    @Test
    void shouldReturnEmptyListOfSubtasks() {
        assertIterableEquals(new ArrayList<>(), manager.getSubtasks());
    }

    @Test
    void shouldAddAndReturnListOfPrioritizedTasks() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        Task task1 = new Task("name", "description", Status.NEW);
        Task task2 = new Task("name", "description", Status.NEW,
                LocalDateTime.of(2023, Month.DECEMBER, 30, 15, 20), 40);
        Task task3 = new Task("name", "description", Status.NEW);
        Subtask subtask1 = new Subtask("name", "description", Status.NEW, epic.getId(),
                LocalDateTime.of(2023, Month.OCTOBER, 30, 15, 20), 40);
        Subtask subtask2 = new Subtask("name", "description", Status.NEW, epic.getId(),
                LocalDateTime.of(2023, Month.DECEMBER, 30, 15, 20), 40);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        List<Task> prioritizedTasks = List.of(subtask1, task2, task1, task3);

        assertIterableEquals(prioritizedTasks, manager.getPrioritizedTasks());
    }

    @Test
    void shouldReturnEmptyListOfPrioritizedTasks() {
        assertIterableEquals(new ArrayList<Task>(), manager.getPrioritizedTasks());
    }

    @Test
    void shouldRemoveAllTasksFromTasksHistoryPrioritized() {
        Task task1 = new Task("name", "description", Status.NEW);
        Task task2 = new Task("name", "description", Status.NEW,
                LocalDateTime.of(2023, Month.DECEMBER, 30, 15, 20), 40);
        Task task3 = new Task("name", "description", Status.NEW);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        manager.getTask(task3.getId());
        manager.removeAllTasks();

        assertIterableEquals(new ArrayList<Task>(), manager.getTasks());
        assertIterableEquals(new ArrayList<Task>(), manager.getHistory());
        assertIterableEquals(new ArrayList<>(), manager.getPrioritizedTasks());
    }

    @Test
    void shouldRemoveAllEpicsFromEpicsHistory() {
        Epic epic1 = new Epic("name", "description");
        Epic epic2 = new Epic("name", "description");
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.getEpic(epic1.getId());
        manager.getEpic(epic2.getId());
        manager.removeAllEpics();

        assertIterableEquals(new ArrayList<>(), manager.getEpics());
        assertIterableEquals(new ArrayList<>(), manager.getHistory());
    }

    @Test
    void shouldRemoveAllSubtasksFromSubtasksHistoryPrioritized() {
        Epic epic = new Epic("name", "description");
        Subtask subtask1 = new Subtask("name", "description", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("name", "description", Status.NEW, epic.getId());
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());
        manager.removeAllSubtasks();

        assertIterableEquals(new ArrayList<>(), manager.getSubtasks());
        assertIterableEquals(new ArrayList<>(), manager.getHistory());
        assertIterableEquals(new ArrayList<>(), manager.getPrioritizedTasks());
    }

    @Test
    void shouldAddAndReturnTaskWithoutStartTime() {
        Task task = new Task("name", "description", Status.NEW);
        manager.addTask(task);

        assertEquals(task, manager.getTask(task.getId()));
    }

    @Test
    void shouldAddAndReturnTaskWithStartTime() {
        Task task = new Task("name", "description", Status.NEW,
                LocalDateTime.of(2023, Month.OCTOBER, 30, 20, 20), 50);
        manager.addTask(task);

        assertEquals(task, manager.getTask(task.getId()));
    }

    @Test
    void shouldAddAndReturnSubtaskWithoutStartTime() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("name", "description", Status.NEW, epic.getId());
        manager.addSubtask(subtask);

        assertEquals(subtask, manager.getSubtask(subtask.getId()));
    }

    @Test
    void shouldAddAndReturnSubtaskWithStartTime() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("name", "description", Status.NEW, epic.getId(),
                LocalDateTime.of(2023, Month.OCTOBER, 30, 20, 20), 50);
        manager.addSubtask(subtask);

        assertEquals(subtask, manager.getSubtask(subtask.getId()));
    }

    @Test
    void shouldNotAddTaskWithInvalidStartEndTime() {
        Task task1 = new Task("name", "description", Status.NEW,
                LocalDateTime.of(2023, Month.OCTOBER, 30, 20, 20), 50);
        Task task2 = new Task("name", "description", Status.NEW,
                LocalDateTime.of(2023, Month.OCTOBER, 30, 20, 20), 50);
        Task task3 = new Task("name", "description", Status.NEW,
                LocalDateTime.of(2020, Month.OCTOBER, 30, 20, 20), 50);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        assertIterableEquals(new ArrayList<>(List.of(task1, task3)), manager.getTasks());
    }

    @Test
    void shouldNotAddSubtaskWithInvalidStartEndTime() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("name", "description", Status.NEW, epic.getId(),
                LocalDateTime.of(2023, Month.OCTOBER, 30, 20, 20), 50);
        Subtask subtask2 = new Subtask("name", "description", Status.NEW, epic.getId(),
                LocalDateTime.of(2023, Month.OCTOBER, 30, 20, 20), 50);
        Subtask subtask3 = new Subtask("name", "description", Status.NEW, epic.getId(),
                LocalDateTime.of(2020, Month.OCTOBER, 30, 20, 20), 50);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        assertIterableEquals(new ArrayList<>(List.of(subtask1, subtask3)), manager.getSubtasks());
    }

    @Test
    void shouldAddAndReturnEpic() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);

        assertEquals(epic, manager.getEpic(epic.getId()));
    }

    @Test
    void shouldReturnNullTaskWhenEmptyTasks() {
        assertNull(manager.getTask(0));
    }

    @Test
    void shouldReturnNullTaskWhenInvalidId() {
        Task task = new Task("name", "description", Status.NEW);
        manager.addTask(task);

        assertNull(manager.getTask(task.getId() + 1));
    }

    @Test
    void shouldReturnNullSubtaskWhenEmptySubtasks() {
        assertNull(manager.getSubtask(0));
    }

    @Test
    void shouldReturnNullSubtaskWhenInvalidId() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("name", "description", Status.NEW, epic.getId());

        assertNull(manager.getSubtask(subtask.getId() + 1));
    }

    @Test
    void shouldReturnNullEpicWhenEmptyEpics() {
        assertNull(manager.getEpic(0));
    }

    @Test
    void shouldReturnNullEpicWhenInvalidId() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);

        assertNull(manager.getEpic(epic.getId() + 1));
    }

    @Test
    void shouldUpdateTaskWhenExist() {
        Task task1 = new Task("name", "description", Status.NEW);
        manager.addTask(task1);
        Task task2 = new Task("name", "description", Status.NEW);
        task2.setId(task1.getId());
        manager.updateTask(task2);

        assertEquals(task2, manager.getTask(task1.getId()));
    }

    @Test
    void shouldNotUpdateTaskWhenNotExist() {
        Task task = new Task("name", "description", Status.NEW);
        manager.updateTask(task);

        assertNull(manager.getTask(task.getId()));
    }

    @Test
    void shouldUpdateSubtaskWhenExist() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("name", "description", Status.NEW, epic.getId());
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("name", "description", Status.NEW, epic.getId());
        subtask2.setId(subtask1.getId());
        manager.updateSubtask(subtask2);

        assertEquals(subtask2, manager.getSubtask(subtask1.getId()));
    }

    @Test
    void shouldNotUpdateSubtaskWhenNotExist() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("name", "description", Status.NEW, epic.getId());
        manager.updateSubtask(subtask);

        assertNull(manager.getSubtask(subtask.getId()));
    }

    @Test
    void shouldUpdateEpicWhenExist() {
        Epic epic1 = new Epic("name", "description");
        manager.addEpic(epic1);
        Epic epic2 = new Epic("name", "description");
        epic2.setId(epic1.getId());
        manager.updateTask(epic2);

        assertEquals(epic2, manager.getEpic(epic1.getId()));
    }

    @Test
    void shouldNotUpdateEpicWhenNotExist() {
        Epic epic = new Epic("name", "description");
        manager.updateEpic(epic);

        assertNull(manager.getSubtask(epic.getId()));
    }

    @Test
    void shouldRemoveTaskFromTasksHistoryPrioritized() {
        Task task1 = new Task("name", "description", Status.NEW);
        Task task2 = new Task("name", "description", Status.NEW);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        manager.removeTask(task1.getId());

        assertIterableEquals(new ArrayList<>(List.of(task2)), manager.getTasks());
        assertIterableEquals(new ArrayList<>(List.of(task2)), manager.getHistory());
        assertIterableEquals(new ArrayList<>(List.of(task2)), manager.getPrioritizedTasks());
    }

    @Test
    void shouldNotRemoveTaskWhenInvalidId() {
        Task task = new Task("name", "description", Status.NEW);
        manager.addTask(task);
        manager.removeTask(task.getId() + 1);

        assertEquals(task, manager.getTask(task.getId()));
    }

    @Test
    void shouldRemoveSubtaskFromSubtasksHistoryPrioritized() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("name", "description", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("name", "description", Status.NEW, epic.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());
        manager.removeSubtask(subtask1.getId());

        assertIterableEquals(new ArrayList<>(List.of(subtask2)), manager.getSubtasks());
        assertIterableEquals(new ArrayList<>(List.of(subtask2)), manager.getHistory());
        assertIterableEquals(new ArrayList<>(List.of(subtask2)), manager.getPrioritizedTasks());
    }

    @Test
    void shouldNotRemoveSubtaskWhenInvalidId() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("name", "description", Status.NEW, epic.getId());
        manager.addSubtask(subtask);
        manager.removeSubtask(subtask.getId() + 1);

        assertEquals(subtask, manager.getSubtask(subtask.getId()));
    }

    @Test
    void shouldRemoveEpicFromEpicsHistory() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        manager.getEpic(epic.getId());
        manager.removeEpic(epic.getId());

        assertNull(manager.getEpic(epic.getId()));
        assertIterableEquals(new ArrayList<>(), manager.getHistory());
    }

    @Test
    void shouldNotRemoveEpicWhenInvalidId() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        manager.removeEpic(epic.getId() + 1);

        assertEquals(epic, manager.getEpic(epic.getId()));
    }

    @Test
    void shouldReturnListOfEpicSubtasks() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("name", "description", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("name", "description", Status.NEW, epic.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertIterableEquals(new ArrayList<>(List.of(subtask1, subtask2)), manager.getEpicSubtasks(epic.getId()));
    }

    @Test
    void shouldReturnEmptyListWhenInvalidId() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("name", "description", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("name", "description", Status.NEW, epic.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertIterableEquals(new ArrayList<>(), manager.getEpicSubtasks(epic.getId() + 1));
    }

    @Test
    void shouldReturnEmptyListWhenEpicIsEmpty() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);

        assertIterableEquals(new ArrayList<>(), manager.getEpicSubtasks(epic.getId()));
    }

    @Test
    void shouldReturnTasksInHistory() {
        Task task = new Task("name", "description", Status.NEW);
        Epic epic = new Epic("name", "description");
        manager.addTask(task);
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("name", "description", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("name", "description", Status.NEW, epic.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.getTask(task.getId());
        manager.getEpic(epic.getId());
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());
        manager.getEpic(epic.getId());

        List<Task> expectedHistory = List.of(task, subtask1, subtask2, epic);
        assertIterableEquals(expectedHistory, manager.getHistory());
    }

    @Test
    void shouldReturnEmptyHistory() {
        assertIterableEquals(new ArrayList<>(), manager.getHistory());
    }

    @Test
    public void shouldNotAddSubtaskWhenInvalidEpicId() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("name", "description", Status.NEW, epic.getId() + 1);
        manager.addSubtask(subtask);

        assertNull(manager.getSubtask(1));
    }
}