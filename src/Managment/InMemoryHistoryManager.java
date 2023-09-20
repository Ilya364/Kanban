package Managment;

import Tasks.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{
    private final Map<Integer, Node<Task>> history = new HashMap<>();
    private final CustomLinkedList tasksInHistory = new CustomLinkedList();

    public static class CustomLinkedList {
        private Node<Task> head;
        private Node<Task> tail;
        private int size = 0;

        public void linkLast(Task task) {
            final Node<Task> oldTail = tail;
            final Node<Task> newNode = new Node<>(task, oldTail, null);
            tail = newNode;
            if (oldTail == null)
                head = newNode;
            else
                oldTail.setNext(newNode);
            size++;
        }

        public ArrayList<Task> getTasks() {
            ArrayList<Task> tasks = new ArrayList<>();
            Node<Task> curNode = head;
            while (curNode.getNext() != null) {
                tasks.add(curNode.getData());
                curNode = curNode.getNext();
            }
            tasks.add(curNode.getData());
            return tasks;
        }

        public void removeNode(Node<Task> node) {
            final Node<Task> next = node.getNext();
            final Node<Task> prev = node.getPrev();

            if (prev == null) {
                head = next;
            } else {
                prev.setNext(next);
                node.setPrev(null);
            }

            if (next == null) {
                tail = prev;
            } else {
                next.setPrev(prev);
                node.setNext(null);
            }

            node.setData(null);
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
            Node<Task> node = history.get(task.getId());
            tasksInHistory.removeNode(node);
            history.remove(task.getId());
        }
        tasksInHistory.linkLast(task);
        history.put(task.getId(), tasksInHistory.tail);
    }

    @Override
    public void remove(int id) {
        if (history.containsKey(id)) {
            tasksInHistory.removeNode(history.get(id));
            history.remove(id);
        }
    }
}
