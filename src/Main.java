import Managment.Manager;
import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

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

        for (Task task: manager.getTasks()) {
            System.out.println(task);
        }
        for (Epic epic: manager.getEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println("-------------------");

        Task newFirstTask = manager.getTask(0);
        newFirstTask.setStatus(Status.IN_PROGRESS);
        manager.updateTask(newFirstTask);

        Task newSecondTask = manager.getTask(1);
        newSecondTask.setStatus(Status.DONE);
        manager.updateTask(newSecondTask);

        Subtask newFirstSubtask = manager.getSubtask(3);
        newFirstSubtask.setStatus(Status.DONE);
        manager.updateSubtask(newFirstSubtask);

        Subtask newSecondSubtask = manager.getSubtask(4);
        newSecondSubtask.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(newSecondSubtask);

        Subtask newThirdSubtask = manager.getSubtask(6);
        newThirdSubtask.setStatus(Status.DONE);
        manager.updateSubtask(newThirdSubtask);

        for (Task task: manager.getTasks()) {
            System.out.println(task);
        }
        for (Epic epic: manager.getEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("------------------------");
        manager.removeEpic(2);
        manager.removeTask(1);
        for (Task task: manager.getTasks()) {
            System.out.println(task);
        }
        for (Epic epic: manager.getEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }
    }
}
