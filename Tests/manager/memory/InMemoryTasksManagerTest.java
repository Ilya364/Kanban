package manager.memory;

import org.junit.jupiter.api.BeforeEach;
import kanban.manager.memory.InMemoryTasksManager;

public class InMemoryTasksManagerTest extends TasksManagerTest<InMemoryTasksManager> {
    @BeforeEach
    void createManager() {
        manager = new InMemoryTasksManager();
    }
}