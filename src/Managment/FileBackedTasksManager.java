package Managment;

import Tasks.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTasksManager{
    private final Path path;
    private static final String FILE_HEADER = "id,type,name,status,description,epic" + System.lineSeparator();

    private static String historyToString(HistoryManager historyManager) {
        StringBuilder historyToString = new StringBuilder();
        final int size = historyManager.getHistory().size();
        final List<Task> history = historyManager.getHistory();
        if (size != 0) {
            int index;
            for (index = 0; index < size - 1; index++) {
                historyToString.append(history.get(index).getId()).append(",");
            }
            historyToString.append(history.get(index).getId());
        }
        return historyToString.toString();
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        for (String elementId : value.split(",")) {
            history.add(Integer.parseInt(elementId));
        }
        return history;
    }

    private void save() {
        try (Writer fileWriter = new FileWriter(path.toFile())) {
            fileWriter.write(FILE_HEADER);
            for (Task task: super.getTasks()) {
                fileWriter.write(task.toString() + System.lineSeparator());
            }
            for (Epic epic: super.getEpics()) {
                fileWriter.write(epic.toString() + System.lineSeparator());
            }
            for (Subtask subtask: super.getSubtasks()) {
                fileWriter.write(subtask.toString() + System.lineSeparator());
            }
            fileWriter.write(System.lineSeparator() + historyToString(this.historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при сохранении данных в файл.");
        }
    }

    private Task fromString(String value) {
        Task task = null;
        if (!value.isBlank()) {
            String[] taskProperties = value.split(",");
            String name = taskProperties[2];
            Status status = Status.valueOf(taskProperties[3]);
            String description = taskProperties[4];
            switch (TaskType.valueOf(taskProperties[1])) {
                case SUBTASK:
                    int epicId = Integer.parseInt(taskProperties[5]);
                    task = new Subtask(name, description, status, epicId);
                    break;
                case TASK:
                    task = new Task(name, description, status);
                    break;
                case EPIC:
                    task = new Epic(name, description);
                    break;
            }
            if (task != null) {
                task.setId(Integer.parseInt(taskProperties[0]));
            }
        }
        return task;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(Paths.get(file.toURI()));
        try (BufferedReader reader = new BufferedReader(new FileReader(manager.path.toFile()))){
            int i = 0;
            while (reader.ready()) {
                if (i == 0) {
                    reader.readLine();
                    i++;
                    continue;
                }
                String strTask = reader.readLine();
                if (!strTask.isEmpty()) {
                    TaskType taskType = TaskType.valueOf(strTask.split(",")[1]);
                    int id = Integer.parseInt(strTask.split(",")[0]);
                    switch (taskType) {
                        case TASK:
                            Task task = manager.fromString(strTask);
                            manager.tasks.put(task.getId(), task);
                            break;
                        case EPIC:
                            Epic epic = (Epic) manager.fromString(strTask);
                            manager.epics.put(epic.getId(), epic);
                            break;
                        case SUBTASK:
                            Subtask subtask = (Subtask) manager.fromString(strTask);
                            manager.subtasks.put(subtask.getId(), subtask);
                            manager.epics.get(subtask.getEpicId()).addSubtaskId(subtask.getId());
                            break;
                    }
                    if (manager.nextId <= id) {
                        manager.nextId = id + 1;
                    }
                } else {
                    break;
                }
            }
            String history = reader.readLine();
            if (history != null) {
                for (Integer id : historyFromString(history)) {
                    if (manager.tasks.get(id) != null) {  //Обращаюсь к мапам напрямую, т.к. методы получения
                        Task task = manager.getTask(id);  //заполняют историю
                        manager.historyManager.add(task);
                    } else if (manager.epics.get(id) != null) {
                        Epic epic = manager.getEpic(id);
                        manager.historyManager.add(epic);
                    } else {
                        Subtask subtask = manager.getSubtask(id);
                        manager.historyManager.add(subtask);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при сохранении данных из файла.");
        }

        return manager;
    }

    public FileBackedTasksManager(Path path) {
        this.path = path;
        if (Files.notExists(this.path)) {
            try {
                Files.createFile(this.path);
            } catch (IOException e) {
                throw new ManagerSaveException("Произошла ошибка при создании файла.");
            }
        }
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int id) {
        return super.getEpicSubtasks(id);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    public static void main(String[] args) {
        final String filename = "kanban.csv";
        TasksManager manager = loadFromFile(new File(System.getProperty("user.dir") + "\\resources\\" + filename));
        manager.removeAllEpics();
        manager.removeAllTasks();
        manager.removeAllSubtasks();

        Task task1 = new Task("task1", "descriptionTask1", Status.DONE);
        Task task2 = new Task("task2", "descriptionTask2", Status.NEW);
        Task task3 = new Task("task3", "descriptionTask2", Status.IN_PROGRESS);

        Epic epic1 = new Epic("epic1", "descriptionEpic1");
        Epic epic2 = new Epic("epic2", "descriptionEpic2");

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        Subtask subtask1 = new Subtask("subtask1", "descriptionSubtask1", Status.DONE, epic2.getId());
        manager.addSubtask(subtask1);

        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        manager.getTask(task3.getId());
        manager.getEpic(epic1.getId());
        manager.getTask(task2.getId());
        manager.getTask(task3.getId());
        manager.getEpic(epic2.getId());
        manager.getSubtask(subtask1.getId());

        TasksManager manager1 = loadFromFile(new File(System.getProperty("user.dir") + "\\resources\\" + filename));
    }
}
