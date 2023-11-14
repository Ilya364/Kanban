package kanban.manager.memory;

import kanban.manager.memory.history.HistoryManager;
import kanban.manager.memory.history.InMemoryHistoryManager;
import java.io.IOException;
import java.net.URI;

public class Managers {
    private static final URI localhost = URI.create("http://localhost:");
    private static final int PORT = 8079;
    
    public static TasksManager getDefault() throws IOException, InterruptedException {
        URI uri = URI.create(localhost.toString() + PORT);
        return new HttpTaskManager(uri);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
