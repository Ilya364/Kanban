package Tasks;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, Status status, int epicId, LocalDateTime startTime,
                   int durationInMinutes) {
        super(name, description, status, startTime, durationInMinutes);
        this.epicId = epicId;
    }

    public Subtask(String name ,String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        if (getStartTime() != null) {
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s", getId(), TaskType.SUBTASK, getName(), getStatus(),
                    getDescription(), getStartTime(), getDuration().toMinutes(), epicId);
        }
        return String.format("%s,%s,%s,%s,%s,%s", getId(), TaskType.SUBTASK, getName(), getStatus(), getDescription(), epicId);
    }
}
