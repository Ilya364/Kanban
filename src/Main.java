import Managment.Managers;
import Managment.TaskManager;
import Tasks.*;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        Task firstTask = new Task("задача", "задача 1", Status.NEW);
        Task secondTask = new Task("задача", "задача 2", Status.NEW);
        manager.addTask(firstTask);
        manager.addTask(secondTask);
        Epic firstEpic = new Epic("эпик", "эпик 1");
        Epic secondEpic = new Epic("эпик", "эпик 2");
        manager.addEpic(firstEpic);
        manager.addEpic(secondEpic);
        Subtask firstSubtask = new Subtask("подзадача", "подзадача 1", Status.NEW, firstEpic.getId());
        Subtask secondSubtask = new Subtask("подзадача", "подзадача 2", Status.NEW, firstEpic.getId());
        Subtask thirdSubtask = new Subtask("подзадача", "подзадача 3", Status.NEW, firstEpic.getId());
        manager.addSubtask(firstSubtask);
        manager.addSubtask(secondSubtask);
        manager.addSubtask(thirdSubtask);

        manager.getTask(0);
        manager.getTask(0);
        manager.getTask(1);
        manager.getEpic(2);
        manager.getSubtask(6);
        manager.getEpic(3);
        manager.getSubtask(4);
        manager.getEpic(3);
        manager.getSubtask(5);
        manager.getSubtask(6);
        manager.getSubtask(5);
        for (Task task: manager.getHistory()) {
            System.out.println(task);
        }
        System.out.println("--------------------------");
        manager.removeTask(1);
        for (Task task: manager.getHistory()) {
            System.out.println(task);
        }
        System.out.println("--------------------------");
        manager.removeEpic(2);
        for (Task task: manager.getHistory()) {
            System.out.println(task);
        }
    }
}
