package kanban.manager.adapter;

import kanban.task.Subtask;
import kanban.task.Status;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubtaskAdapter extends TypeAdapter<Subtask> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");
    
    @Override
    public void write(JsonWriter jsonWriter, Subtask subtask) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("id").value(subtask.getId());
        jsonWriter.name("name").value(subtask.getName());
        jsonWriter.name("description").value(subtask.getDescription());
        jsonWriter.name("status").value(subtask.getStatus().toString());
        jsonWriter.name("epicId").value(subtask.getEpicId());
        if (subtask.getStartTime() != null) {
            jsonWriter.name("startTime").value(subtask.getStartTime().format(formatter));
            jsonWriter.name("duration").value(subtask.getDuration().toMinutes());
        }
        jsonWriter.endObject();
    }
    
    @Override
    public Subtask read(JsonReader jsonReader) throws IOException {
        final Subtask subtask = new Subtask();
        subtask.setId(-1);
        
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            switch (jsonReader.nextName()) {
                case "id":
                    subtask.setId(jsonReader.nextInt());
                    break;
                case "name":
                    subtask.setName(jsonReader.nextString());
                    break;
                case "description":
                    subtask.setDescription(jsonReader.nextString());
                    break;
                case "status":
                    subtask.setStatus(Status.valueOf(jsonReader.nextString()));
                    break;
                case "epicId":
                    int epicId = jsonReader.nextInt();
                    subtask.setEpicId(epicId);
                    break;
                case "startTime":
                    LocalDateTime startTime = LocalDateTime.parse(jsonReader.nextString(), formatter);
                    subtask.setStartTime(startTime);
                    break;
                case "duration":
                    int duration = jsonReader.nextInt();
                    subtask.setDuration(duration);
                    break;
                default:
                    throw new JsonSyntaxException("");
            }
        }
        jsonReader.endObject();
        return subtask;
    }
}
