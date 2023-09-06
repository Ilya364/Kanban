import Managment.InMemoryTaskManager;
import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task firstTask = new Task("Посмотреть кино", "Которое посоветовали", Status.NEW);
        Task secondTask = new Task("Тренировка", "Тренировка спины и ног", Status.NEW);
        manager.addTask(firstTask);
        manager.addTask(secondTask);

        Epic firstEpic = new Epic("Выполнить дз", "По всем тех. предметам");
        manager.addEpic(firstEpic);

        Subtask firstSubtask = new Subtask("Инж. граф.", "Решить второе и третье задание", Status.NEW, firstEpic.getId());
        Subtask secondSubtask = new Subtask("Мат анализ", "Решить типовой расчет", Status.NEW, firstEpic.getId());
        manager.addSubtask(firstSubtask);
        manager.addSubtask(secondSubtask);

        Epic secondEpic = new Epic("Почитать", "Сегодня худ литература");
        manager.addEpic(secondEpic);

        Subtask thirdSubtask = new Subtask("Выбрать книгу", "Дома есть пару хороших", Status.NEW, secondEpic.getId());
        manager.addSubtask(thirdSubtask);

        Task newFirstTask = manager.getTask(0);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        Task newSecondTask = manager.getTask(1);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        Subtask newFirstSubtask = manager.getSubtask(3);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        Subtask newSecondSubtask = manager.getSubtask(4);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        Subtask newThirdSubtask = manager.getSubtask(3);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
