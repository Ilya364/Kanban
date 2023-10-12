package Tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<Integer> subtasksIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void addSubtaskId(int id) {
        if (!subtasksIds.contains(id)) {
            subtasksIds.add(id);
        }
    }

    public void removeSubtaskId(int id) {
        subtasksIds.remove((Integer) id);
    }

    public void removeAllSubtasksIds() {
        subtasksIds.clear();
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
        return String.format("%s,%s,%s,%s,%s", getId(), TaskType.EPIC, getName(),
                getStatus(), getDescription());
    }
}
