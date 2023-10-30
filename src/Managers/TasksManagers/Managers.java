package Managers.TasksManagers;

import Managers.HistoryManagers.HistoryManager;
import Managers.HistoryManagers.InMemoryHistoryManager;

public class Managers {
    public static TasksManager getDefault() {
        return new InMemoryTasksManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
