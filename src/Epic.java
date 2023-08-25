import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task{
    private ArrayList<Integer> subtasksIds;

    public Epic(String name, String description, ArrayList<Integer> subtasks) {
        super(name, description, Status.DEFAULT);
        this.subtasksIds = subtasks;
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasksIds, epic.subtasksIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksIds);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name=" + '\'' + getName() + '\'' +
                ", description=" + '\'' + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", subtasks=" + subtasksIds +
                '}';
    }
}
