package Managers.HistoryManagers;

import Managers.TasksManagers.Managers;
import Tasks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    public void createManager() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void shouldAddTaskToHistory() {
        Task task = new Task("name", "description", Status.DONE);
        historyManager.add(task);

        assertIterableEquals(new ArrayList<>(List.of(task)), historyManager.getHistory());
    }

    @Test
    void shouldSaveOnlyLastTaskGettingToHistory() {
        Task task1 = new Task("name", "description", Status.DONE);
        Task task2 = new Task("name", "description", Status.DONE);
        Task task3 = new Task("name", "description", Status.DONE);
        task1.setId(0);
        task2.setId(1);
        task3.setId(2);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task2);
        historyManager.add(task1);

        assertIterableEquals(new ArrayList<>(List.of(task3, task2, task1)), historyManager.getHistory());
    }

    @Test
    void shouldAddEpicToHistory() {
        Epic epic = new Epic("name", "description");
        historyManager.add(epic);

        assertIterableEquals(new ArrayList<>(List.of(epic)), historyManager.getHistory());
    }

    @Test
    void shouldSaveOnlyLastEpicGettingToHistory() {
        Epic epic1 = new Epic("name", "description");
        Epic epic2 = new Epic("name", "description");
        Epic epic3 = new Epic("name", "description");
        epic1.setId(0);
        epic2.setId(1);
        epic3.setId(2);
        historyManager.add(epic1);
        historyManager.add(epic2);
        historyManager.add(epic3);
        historyManager.add(epic1);
        historyManager.add(epic2);

        assertIterableEquals(new ArrayList<>(List.of(epic3, epic1, epic2)), historyManager.getHistory());
    }

    @Test
    void shouldAddSubtaskToHistory() {
        Subtask subtask = new Subtask("name", "description", Status.DONE, 0);
        historyManager.add(subtask);

        assertIterableEquals(new ArrayList<>(List.of(subtask)), historyManager.getHistory());
    }

    @Test
    void shouldSaveOnlyLastSubtaskGettingToHistory() {
        Subtask subtask1 = new Subtask("name", "description", Status.DONE, 0);
        Subtask subtask2 = new Subtask("name", "description", Status.DONE, 0);
        Subtask subtask3 = new Subtask("name", "description", Status.DONE, 0);
        subtask1.setId(0);
        subtask2.setId(1);
        subtask3.setId(2);
        historyManager.add(subtask1);
        historyManager.add(subtask3);
        historyManager.add(subtask2);
        historyManager.add(subtask1);
        historyManager.add(subtask3);

        assertIterableEquals(new ArrayList<>(List.of(subtask2, subtask1, subtask3)), historyManager.getHistory());
    }

    @Test
    void shouldRemoveElementFromStartOfHistory() {
        Task task = new Task("name", "description", Status.IN_PROGRESS);
        Epic epic = new Epic("name", "description");
        Subtask subtask1 = new Subtask("name", "description", Status.IN_PROGRESS, 1);
        Subtask subtask2 = new Subtask("name", "description", Status.IN_PROGRESS, 1);
        task.setId(0);
        epic.setId(1);
        subtask1.setId(2);
        subtask2.setId(3);

        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask1);
        historyManager.add(subtask2);

        historyManager.remove(task.getId());
        assertIterableEquals(new ArrayList<>(List.of(epic, subtask1, subtask2)), historyManager.getHistory());
    }

    @Test
    void shouldRemoveElementFromMiddleOfHistory() {
        Task task = new Task("name", "description", Status.IN_PROGRESS);
        Epic epic = new Epic("name", "description");
        Subtask subtask1 = new Subtask("name", "description", Status.IN_PROGRESS, 1);
        Subtask subtask2 = new Subtask("name", "description", Status.IN_PROGRESS, 1);
        task.setId(0);
        epic.setId(1);
        subtask1.setId(2);
        subtask2.setId(3);

        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask1);
        historyManager.add(subtask2);

        historyManager.remove(epic.getId());
        assertIterableEquals(new ArrayList<>(List.of(task, subtask1, subtask2)), historyManager.getHistory());
        historyManager.remove(subtask1.getId());
        assertIterableEquals(new ArrayList<>(List.of(task, subtask2)), historyManager.getHistory());
    }

    @Test
    void shouldRemoveElementFromEndOfHistory() {
        Task task = new Task("name", "description", Status.IN_PROGRESS);
        Epic epic = new Epic("name", "description");
        Subtask subtask1 = new Subtask("name", "description", Status.IN_PROGRESS, 1);
        Subtask subtask2 = new Subtask("name", "description", Status.IN_PROGRESS, 1);
        task.setId(0);
        epic.setId(1);
        subtask1.setId(2);
        subtask2.setId(3);

        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask1);
        historyManager.add(subtask2);

        historyManager.remove(subtask2.getId());
        assertIterableEquals(new ArrayList<>(List.of(task, epic, subtask1)), historyManager.getHistory());
    }
}