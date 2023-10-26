package Tasks;

import Managers.TasksManagers.Managers;
import Managers.TasksManagers.TasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private TasksManager manager;

    @BeforeEach
    public void createManager() {
        manager = Managers.getDefault();
    }

    @Test
    public void shouldDetermineStatusNewWhenEmpty() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        assertEquals(manager.getEpic(epic.getId()).getStatus(), Status.NEW);
    }

    @Test
    public void shouldDetermineStatusNewWhenAllSubsNew() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("name", "description", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("name", "description", Status.NEW, epic.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        assertEquals(manager.getEpic(epic.getId()).getStatus(), Status.NEW);
    }

    @Test
    public void shouldDetermineStatusDoneWhenAllSubsDone() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("name", "description", Status.DONE, epic.getId());
        Subtask subtask2 = new Subtask("name", "description", Status.DONE, epic.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        assertEquals(manager.getEpic(epic.getId()).getStatus(), Status.DONE);
    }

    @Test
    public void shouldDetermineStatusInProgressWhenVariousSubs() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("name", "description", Status.DONE, epic.getId());
        Subtask subtask2 = new Subtask("name", "description", Status.NEW, epic.getId());
        Subtask subtask3 = new Subtask("name", "description", Status.IN_PROGRESS, epic.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        assertEquals(manager.getEpic(epic.getId()).getStatus(), Status.IN_PROGRESS);
    }

    @Test
    public void shouldDetermineStatusInProgressWhenAllSubsInProgress() {
        Epic epic = new Epic("name", "description");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("name", "description", Status.IN_PROGRESS, epic.getId());
        Subtask subtask2 = new Subtask("name", "description", Status.IN_PROGRESS, epic.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        assertEquals(manager.getEpic(epic.getId()).getStatus(), Status.IN_PROGRESS);
    }
}
