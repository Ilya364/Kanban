package Managment;

import Tasks.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{
    private final Map<Integer, Node> history = new HashMap<>();
    private final CustomLinkedList tasksInHistory = new CustomLinkedList();

    public static class CustomLinkedList {
        private Node head;
        private Node tail;
        private int size = 0;

        public void linkLast(Task task) {
            final Node oldTail = tail;
            final Node newNode = new Node(task, oldTail, null);
            tail = newNode;
            if (oldTail == null)
                head = newNode;
            else
                oldTail.next = newNode;
            size++;
        }

        public ArrayList<Task> getTasks() {
            ArrayList<Task> tasks = new ArrayList<>();
            Node curNode = head;
            while (curNode.next != null) {
                tasks.add(curNode.task);
                curNode = curNode.next;
            }
            tasks.add(curNode.task);
            return tasks;
        }

        public void removeNode(Node node) {
            final Node next = node.next;
            final Node prev = node.prev;

            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
                node.prev = null;
            }

            if (next == null) {
                tail = prev;
            } else {
                next.prev = prev;
                node.next = null;
            }

            node.task = null;
            size--;
        }
    }

    @Override
    public List<Task> getHistory() {
        return tasksInHistory.getTasks();
    }

    @Override
    public void add(Task task) {
        if (history.containsKey(task.getId())) {
            Node node = history.get(task.getId());
            tasksInHistory.removeNode(node);
            history.remove(task.getId());
        }
        tasksInHistory.linkLast(task);
        history.put(task.getId(), tasksInHistory.tail);
    }

    @Override
    public void remove(int id) {
        tasksInHistory.removeNode(history.get(id));
        history.remove(id);
    }
}
