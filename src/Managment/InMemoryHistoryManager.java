package Managment;

import Tasks.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private static final int TASKS_HISTORY_SIZE = 10;
    private final List<Task> history = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        return history;
    }

    @Override
    public void add(Task task) {
        if (history.size() < TASKS_HISTORY_SIZE) {
            history.add(task);
        } else {
            history.remove(0);
            history.add(task);
        }
    }
}
