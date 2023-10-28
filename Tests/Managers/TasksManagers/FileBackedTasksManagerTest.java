package Managers.TasksManagers;

import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TasksManagerTest<FileBackedTasksManager> {
    private static final File filename = new File(System.getProperty("user.dir")
            + "\\resources\\" + "kanban.csv");

    @BeforeEach
    public void createManager() {
        manager = FileBackedTasksManager.loadFromFile(filename);
        manager.removeAllTasks();
        manager.removeAllSubtasks();
        manager.removeAllEpics();
    }

    @Test
    public void shouldWriteAndReadTasksEpicsSubtasksToFromFile() {
        Task task1 = new Task("name", "description", Status.NEW,
                LocalDateTime.of(2023, Month.OCTOBER, 30, 15, 0), 50);
        Task task2 = new Task("name", "description", Status.DONE);
        Epic epic = new Epic("name", "description");
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("name", "description", Status.DONE, epic.getId());
        Subtask subtask2 = new Subtask("name", "description", Status.DONE, epic.getId(),
                LocalDateTime.of(2023, Month.OCTOBER, 29, 15, 0), 50);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        TasksManager newManager = FileBackedTasksManager.loadFromFile(filename);

        assertIterableEquals(manager.getTasks(), newManager.getTasks());
        assertIterableEquals(manager.getSubtasks(), newManager.getSubtasks());
        assertIterableEquals(manager.getEpics(), newManager.getEpics());
        assertIterableEquals(manager.getPrioritizedTasks(), newManager.getPrioritizedTasks());
    }

    @Test
    public void shouldWriteHistoryToFromFile() {
        Task task1 = new Task("name", "description", Status.NEW,
                LocalDateTime.of(2023, Month.OCTOBER, 30, 15, 0), 50);
        Task task2 = new Task("name", "description", Status.DONE);
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        manager.getTask(task1.getId());
        TasksManager newManager = FileBackedTasksManager.loadFromFile(filename);

        assertIterableEquals(manager.getHistory(), newManager.getHistory());
    }

    @Test
    public void shouldNotWriteHistoryToFileWhenEmpty() {
        Task task = new Task("name", "description", Status.DONE);
        manager.addTask(task);
        TasksManager newManager = FileBackedTasksManager.loadFromFile(filename);
        assertIterableEquals(new ArrayList<>(), newManager.getHistory());
    }

    @Test
    public void shouldWriteEpicToFileWhenEmpty() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        TasksManager newManager = FileBackedTasksManager.loadFromFile(filename);
        assertEquals(manager.getEpic(epic.getId()), newManager.getEpic(epic.getId()));
    }
}