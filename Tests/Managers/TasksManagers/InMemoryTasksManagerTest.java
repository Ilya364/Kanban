package Managers.TasksManagers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InMemoryTasksManagerTest extends TasksManagerTest {
    @Test @BeforeEach
    public void createManager() {
        TasksManagerTest.manager = Managers.getDefault();
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
}