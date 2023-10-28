import Managers.TasksManagers.InMemoryTasksManager;
import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        InMemoryTasksManager manager = new InMemoryTasksManager();
        Epic epic = new Epic("новый эпик 1", "описание эпика 1");
        manager.addEpic(epic);
        //Данная задача должна быть 4 по счёту
        Subtask subtask1 = new Subtask("новая подзадача 1", "описание подзадачи 1", Status.NEW,
                epic.getId(), LocalDateTime.of(2022, 12, 30, 0, 30).plusDays(2), 15);
        manager.addSubtask(subtask1);
        //Данная задача должна быть 1 по счёту
        Subtask subtask2 = new Subtask("новая подзадача 2", "описание подзадачи 2", Status.NEW,
                epic.getId(), LocalDateTime.of(2022, 12, 30, 0, 30), 30);
        manager.addSubtask(subtask2);
        //Данная задача должна быть 3 по счёту
        Subtask subtask3 = new Subtask( "новая подзадача 3", "описание подзадачи 3", Status.NEW,
                epic.getId(), LocalDateTime.of(2022, 12, 30, 0, 30).plusDays(1),
                45);
        manager.addSubtask(subtask3);
        //Данная задача должна быть 2 по счёту
        Subtask subtask4 = new Subtask( "новая подзадача 4", "описание подзадачи 4", Status.NEW,
                epic.getId(), LocalDateTime.of(2022, 12, 30, 0, 30).plusHours(12),
                60);
        manager.addSubtask(subtask4);
        //Порядок добавления следующий subtask1 -> subtask2 -> subtask3 -> subtask4
        //Порядок следующий subtask2 -> subtask4 -> subtask3 -> subtask1
        System.out.println(epic.getDuration() + " == "
                + (subtask1.getDuration().plus(subtask2.getDuration().plus(subtask3.getDuration()))
                .plus(subtask4.getDuration())
                + " " + (epic.getDuration().equals(subtask1.getDuration().plus((subtask2.getDuration()
                .plus((subtask3.getDuration()).plus(subtask4.getDuration()))))))));
        System.out.println(epic.getStartTime() + " == " + subtask2.getStartTime() + " "
                + epic.getStartTime().equals(subtask2.getStartTime()));
        System.out.println(epic.getEndTime() + " == " + subtask1.getEndTime() + " "
                + epic.getEndTime().equals(subtask1.getEndTime()));
        System.out.println(manager.getPrioritizedTasks().stream().map(Task::getName).collect(Collectors.toList()));
        //Удаление подзадачи subtask3
        manager.removeSubtask(subtask3.getId());
        //Новый порядок subtask2 -> subtask4 -> subtask1
        System.out.println(epic.getDuration() + " == "
                + (subtask1.getDuration().plus(subtask2.getDuration().plus(subtask4.getDuration())) + " "
                + (epic.getDuration().equals(subtask1.getDuration().plus((subtask2.getDuration()))
                .plus((subtask4.getDuration()))))));
        System.out.println(epic.getStartTime() + " == " + subtask2.getStartTime() + " "
                + epic.getStartTime().equals(subtask2.getStartTime()));
        System.out.println(epic.getEndTime() + " == " + subtask1.getEndTime() + " "
                + epic.getEndTime().equals(subtask1.getEndTime()));
        System.out.println(manager.getPrioritizedTasks().stream().map(Task::getName).collect(Collectors.toList()));
        //Удаление подзадачи subtask2
        manager.removeSubtask(subtask2.getId());
        //Новый порядок subtask4 -> subtask1
        System.out.println(epic.getDuration() + " == " + (subtask1.getDuration().plus(subtask4.getDuration())  + " "
                + (epic.getDuration().equals(subtask1.getDuration().plus(subtask4.getDuration())))));
        System.out.println(epic.getStartTime() + " == " + subtask4.getStartTime() + " "
                + epic.getStartTime().equals(subtask4.getStartTime()));
        System.out.println(epic.getEndTime() + " == " + subtask1.getEndTime() + " "
                + epic.getEndTime().equals(subtask1.getEndTime()));
        System.out.println(manager.getPrioritizedTasks().stream().map(Task::getName).collect(Collectors.toList()));
        //Удаление подзадачи subtask1
        manager.removeSubtask(subtask1.getId());
        //Новый порядок subtask4
        System.out.println(epic.getDuration() + " == " + subtask4.getDuration()  + " "
                + (epic.getDuration() .equals (subtask4.getDuration())));
        System.out.println(epic.getStartTime() + " == " + subtask4.getStartTime() + " "
                + epic.getStartTime().equals(subtask4.getStartTime()));
        System.out.println(epic.getEndTime() + " == " + subtask4.getEndTime() + " "
                + epic.getEndTime().equals(subtask4.getEndTime()));
        System.out.println(manager.getPrioritizedTasks().stream().map(Task::getName).collect(Collectors.toList()));
    }
}
