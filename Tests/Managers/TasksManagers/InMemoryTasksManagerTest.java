package Managers.TasksManagers;

import org.junit.jupiter.api.BeforeEach;

public class InMemoryTasksManagerTest extends TasksManagerTest<InMemoryTasksManager> {
    @BeforeEach
    void createManager() {
        manager = Managers.getDefault();
    }
}