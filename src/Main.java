import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Task firstTask = new Task("Посмотреть кино", "Которое посоветовали", Task.Status.NEW);
        Task secondTask = new Task("Тренировка", "Тренировка спины и ног", Task.Status.NEW);
        manager.addTask(firstTask);
        manager.addTask(secondTask);

        Subtask firstSubtask = new Subtask("Инж. граф.", "Решить второе и третье задание", Task.Status.NEW);
        Subtask secondSubtask = new Subtask("Мат анализ", "Решить типовой расчет", Task.Status.NEW);
        manager.addTask(firstSubtask);
        manager.addTask(secondSubtask);

        ArrayList<Integer> subtaskIds = new ArrayList<>();
        subtaskIds.add(firstSubtask.getId());
        subtaskIds.add(secondSubtask.getId());
        Epic firstEpic = new Epic("Выполнить дз", "По всем тех. предметам", subtaskIds);
        manager.addTask(firstEpic);

        Subtask thirdSubtask = new Subtask("Выбрать книгу", "Дома есть пару хороших", Task.Status.NEW);
        manager.addTask(thirdSubtask);
        ArrayList<Integer> secondSubtaskIds = new ArrayList<>();
        secondSubtaskIds.add(thirdSubtask.getId());
        Epic secondEpic = new Epic("Почитать", "Сегодня худ литература", secondSubtaskIds);
        manager.addTask(secondEpic);

        for (Task task: manager.getTasks().values()) {
            System.out.println(task);
        }
        for (Epic epic: manager.getEpics().values()) {
            System.out.println(epic);
        }
        for (Subtask subtask : manager.getSubtasks().values()) {
            System.out.println(subtask);
        }
        System.out.println("-------------------");

        Task newFirstTask = manager.getTask(0);
        newFirstTask.setStatus(Task.Status.IN_PROGRESS);
        manager.updateTask(newFirstTask);

        Task newSecondTask = manager.getTask(1);
        newSecondTask.setStatus(Task.Status.DONE);
        manager.updateTask(newSecondTask);

        Subtask newFirstSubtask = manager.getSubtask(2);
        newFirstSubtask.setStatus(Task.Status.DONE);
        manager.updateSubtask(newFirstSubtask);

        Subtask newSecondSubtask = manager.getSubtask(3);
        newSecondSubtask.setStatus(Task.Status.IN_PROGRESS);
        manager.updateSubtask(newSecondSubtask);

        Subtask newThirdSubtask = manager.getSubtask(5);
        newThirdSubtask.setStatus(Task.Status.DONE);
        manager.updateSubtask(newThirdSubtask);

        for (Task task: manager.getTasks().values()) {
            System.out.println(task);
        }
        for (Epic epic: manager.getEpics().values()) {
            System.out.println(epic);
        }
        for (Subtask subtask : manager.getSubtasks().values()) {
            System.out.println(subtask);
        }

        System.out.println("------------------------");
        manager.removeEpic(4);
        manager.removeTask(1);
        for (Task task: manager.getTasks().values()) {
            System.out.println(task);
        }
        for (Epic epic: manager.getEpics().values()) {
            System.out.println(epic);
        }
        for (Subtask subtask : manager.getSubtasks().values()) {
            System.out.println(subtask);
        }
    }
}
