package kanban.task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private LocalDateTime endTime;
    public ArrayList<Integer> subtasksIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void addSubtaskId(int id) {
        if (!subtasksIds.contains(id)) {
            subtasksIds.add(id);
        }
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
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
        return Objects.equals(subtasksIds, epic.subtasksIds) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksIds);
    }

    @Override
    public String toString() {
        if (getStartTime() == null) {
            return String.format("%s,%s,%s,%s,%s", getId(), TaskType.EPIC, getName(), getStatus(), getDescription());
        }
        return String.format("%s,%s,%s,%s,%s,%s,%s", getId(), TaskType.EPIC, getName(), getStatus(), getDescription(),
                getStartTime(), getDuration().toMinutes());
    }
}
