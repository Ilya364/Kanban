package manager.server;

import kanban.manager.memory.HttpTaskManager;
import kanban.manager.memory.Managers;
import kanban.server.KVServer;
import kanban.task.*;
import manager.memory.TasksManagerTest;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class HttpTaskManagerTest extends TasksManagerTest<HttpTaskManager> {
    private KVServer server;
    @BeforeEach
    public void createManagerAndStartServer() throws IOException, InterruptedException {
        server = new KVServer();
        server.start();
        manager = (HttpTaskManager) Managers.getDefault();
    }
    
    @AfterEach
    public void stopServer() {
        server.stop();
    }
    
    @Test
    public void shouldWriteAndReadTasksToFromServer () throws IOException, InterruptedException {
        Task task1 = new Task("name1", "desc1", Status.NEW);
        Task task2 = new Task("name2", "desc2", Status.IN_PROGRESS,
                LocalDateTime.of(2023, 11, 11, 15, 20), 60);
        manager.addTask(task1);
        manager.addTask(task2);
        HttpTaskManager newManager = (HttpTaskManager) Managers.getDefault();
        assertIterableEquals(manager.getTasks(), newManager.getTasks());
    }
}
