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

class FileBackedTasksManagerTest extends TasksManagerTest {
    static final String filename = "kanban.csv";

    @BeforeEach @Test
    public void createManager() {
        TasksManagerTest.manager = FileBackedTasksManager.loadFromFile(new File(System.getProperty("user.dir")
                + "\\resources\\" + filename));
        manager.removeAllTasks();
        manager.removeAllSubtasks();
        manager.removeAllEpics();
    }

    @Override @Test
    void shouldAddAndReturnListOfTasks() {
        super.shouldAddAndReturnListOfTasks();
    }

    @Override @Test
    void shouldReturnEmptyListOfTasks() {
        super.shouldReturnEmptyListOfTasks();
    }

    @Override @Test
    void shouldAddAndReturnListOfEpics() {
        super.shouldAddAndReturnListOfEpics();
    }

    @Override @Test
    void shouldReturnEmptyListOfEpics() {
        super.shouldReturnEmptyListOfEpics();
    }

    @Override @Test
    void shouldAddAndReturnListOfSubtasks() {
        super.shouldAddAndReturnListOfSubtasks();
    }

    @Override @Test
    void shouldReturnEmptyListOfSubtasks() {
        super.shouldReturnEmptyListOfSubtasks();
    }

    @Override @Test
    void shouldAddAndReturnListOfPrioritizedTasks() {
        super.shouldAddAndReturnListOfPrioritizedTasks();
    }

    @Override @Test
    void shouldReturnEmptyListOfPrioritizedTasks() {
        super.shouldReturnEmptyListOfPrioritizedTasks();
    }

    @Override @Test
    void shouldRemoveAllTasksFromTasksHistoryPrioritized() {
        super.shouldRemoveAllTasksFromTasksHistoryPrioritized();
    }

    @Override @Test
    void shouldRemoveAllEpicsFromEpicsHistory() {
        super.shouldRemoveAllEpicsFromEpicsHistory();
    }

    @Override @Test
    void shouldRemoveAllSubtasksFromSubtasksHistoryPrioritized() {
        super.shouldRemoveAllSubtasksFromSubtasksHistoryPrioritized();
    }

    @Override @Test
    void shouldAddAndReturnTaskWithoutStartTime() {
        super.shouldAddAndReturnTaskWithoutStartTime();
    }

    @Override @Test
    void shouldAddAndReturnTaskWithStartTime() {
        super.shouldAddAndReturnTaskWithStartTime();
    }

    @Override @Test
    void shouldAddAndReturnSubtaskWithoutStartTime() {
        super.shouldAddAndReturnSubtaskWithoutStartTime();
    }

    @Override @Test
    void shouldAddAndReturnSubtaskWithStartTime() {
        super.shouldAddAndReturnSubtaskWithStartTime();
    }

    @Override @Test
    void shouldNotAddTaskWithInvalidStartEndTime() {
        super.shouldNotAddTaskWithInvalidStartEndTime();
    }

    @Override @Test
    void shouldNotAddSubtaskWithInvalidStartEndTime() {
        super.shouldNotAddSubtaskWithInvalidStartEndTime();
    }

    @Override @Test
    void shouldAddAndReturnEpic() {
        super.shouldAddAndReturnEpic();
    }

    @Override @Test
    void shouldReturnNullTaskWhenEmptyTasks() {
        super.shouldReturnNullTaskWhenEmptyTasks();
    }

    @Override @Test
    void shouldReturnNullTaskWhenInvalidId() {
        super.shouldReturnNullTaskWhenInvalidId();
    }

    @Override @Test
    void shouldReturnNullSubtaskWhenEmptySubtasks() {
        super.shouldReturnNullSubtaskWhenEmptySubtasks();
    }

    @Override @Test
    void shouldReturnNullSubtaskWhenInvalidId() {
        super.shouldReturnNullSubtaskWhenInvalidId();
    }

    @Override @Test
    void shouldReturnNullEpicWhenEmptyEpics() {
        super.shouldReturnNullEpicWhenEmptyEpics();
    }

    @Override @Test
    void shouldReturnNullEpicWhenInvalidId() {
        super.shouldReturnNullEpicWhenInvalidId();
    }

    @Override @Test
    void shouldUpdateTaskWhenExist() {
        super.shouldUpdateTaskWhenExist();
    }

    @Override @Test
    void shouldNotUpdateTaskWhenNotExist() {
        super.shouldNotUpdateTaskWhenNotExist();
    }

    @Override @Test
    void shouldUpdateSubtaskWhenExist() {
        super.shouldUpdateSubtaskWhenExist();
    }

    @Override @Test
    void shouldNotUpdateSubtaskWhenNotExist() {
        super.shouldNotUpdateSubtaskWhenNotExist();
    }

    @Override @Test
    void shouldUpdateEpicWhenExist() {
        super.shouldUpdateEpicWhenExist();
    }

    @Override @Test
    void shouldNotUpdateEpicWhenNotExist() {
        super.shouldNotUpdateEpicWhenNotExist();
    }

    @Override @Test
    void shouldRemoveTaskFromTasksHistoryPrioritized() {
        super.shouldRemoveTaskFromTasksHistoryPrioritized();
    }

    @Override @Test
    void shouldNotRemoveTaskWhenInvalidId() {
        super.shouldNotRemoveTaskWhenInvalidId();
    }

    @Override @Test
    void shouldRemoveSubtaskFromSubtasksHistoryPrioritized() {
        super.shouldRemoveSubtaskFromSubtasksHistoryPrioritized();
    }

    @Override @Test
    void shouldNotRemoveSubtaskWhenInvalidId() {
        super.shouldNotRemoveSubtaskWhenInvalidId();
    }

    @Override @Test
    void shouldRemoveEpicFromEpicsHistory() {
        super.shouldRemoveEpicFromEpicsHistory();
    }

    @Override @Test
    void shouldNotRemoveEpicWhenInvalidId() {
        super.shouldNotRemoveEpicWhenInvalidId();
    }

    @Override @Test
    void shouldReturnListOfEpicSubtasks() {
        super.shouldReturnListOfEpicSubtasks();
    }

    @Override @Test
    void shouldReturnEmptyListWhenInvalidId() {
        super.shouldReturnEmptyListWhenInvalidId();
    }

    @Override @Test
    void shouldReturnEmptyListWhenEpicIsEmpty() {
        super.shouldReturnEmptyListWhenEpicIsEmpty();
    }

    @Override @Test
    void shouldReturnTasksInHistory() {
        super.shouldReturnTasksInHistory();
    }

    @Override @Test
    void shouldReturnEmptyHistory() {
        super.shouldReturnEmptyHistory();
    }

    @Override @Test
    public void shouldNotAddSubtaskWhenInvalidEpicId() {
        super.shouldNotAddSubtaskWhenInvalidEpicId();
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
        TasksManager newManager = FileBackedTasksManager.loadFromFile(new File(System.getProperty("user.dir")
                + "\\resources\\" + filename));

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
        TasksManager newManager = FileBackedTasksManager.loadFromFile(new File(System.getProperty("user.dir")
                + "\\resources\\" + filename));

        assertIterableEquals(manager.getHistory(), newManager.getHistory());
    }

    @Test
    public void shouldNotWriteHistoryToFileWhenEmpty() {
        Task task = new Task("name", "description", Status.DONE);
        manager.addTask(task);
        TasksManager newManager = FileBackedTasksManager.loadFromFile(new File(System.getProperty("user.dir")
                + "\\resources\\" + filename));
        assertIterableEquals(new ArrayList<>(), newManager.getHistory());
    }

    @Test
    public void shouldWriteEpicToFileWhenEmpty() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        TasksManager newManager = FileBackedTasksManager.loadFromFile(new File(System.getProperty("user.dir")
                + "\\resources\\" + filename));
        assertEquals(manager.getEpic(epic.getId()), newManager.getEpic(epic.getId()));
    }
}