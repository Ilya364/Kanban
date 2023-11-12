package kanban.manager.memory;

import kanban.manager.memory.history.HistoryManager;
import kanban.manager.memory.history.InMemoryHistoryManager;
import java.io.IOException;
import java.net.URI;

public class Managers {
    public static TasksManager getDefault() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8079/");
        return new HttpTaskManager(uri);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
