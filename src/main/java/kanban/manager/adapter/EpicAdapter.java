package kanban.manager.adapter;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import kanban.task.Epic;
import kanban.task.Status;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EpicAdapter extends TypeAdapter<Epic> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");
    
    @Override
    public void write(JsonWriter jsonWriter, Epic epic) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("id").value(epic.getId());
        jsonWriter.name("name").value(epic.getName());
        jsonWriter.name("description").value(epic.getDescription());
        jsonWriter.name("status").value(epic.getStatus().toString());
        jsonWriter.name("subtasksIds").value(epic.getSubtasksIds().toString());
        if (epic.getStartTime() != null) {
            jsonWriter.name("startTime").value(epic.getStartTime().format(formatter));
            jsonWriter.name("duration").value(epic.getDuration().toMinutes());
        }
        jsonWriter.endObject();
    }
    
    @Override
    public Epic read(JsonReader jsonReader) throws IOException {
        final Epic epic = new Epic();
        epic.setId(-1);
        
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            switch (jsonReader.nextName()) {
                case "id":
                    epic.setId(jsonReader.nextInt());
                    break;
                case "name":
                    epic.setName(jsonReader.nextString());
                    break;
                case "status":
                    epic.setStatus(Status.valueOf(jsonReader.nextString()));
                    break;
                case "description":
                    epic.setDescription(jsonReader.nextString());
                    break;
                case "subtasksIds":
                    String subtaskIds = jsonReader.nextString();
                    subtaskIds = subtaskIds.substring(1, subtaskIds.length() - 1);
                    if (subtaskIds.isEmpty()) {
                        break;
                    } else if (subtaskIds.length() == 1) {
                        int subId = Integer.parseInt(subtaskIds);
                        epic.setId(subId);
                    } else {
                        String[] subIds = subtaskIds.split(",");
                        for (String id: subIds) {
                            id = id.trim();
                            epic.addSubtaskId(Integer.parseInt(id));
                        }
                    }
                    break;
                case "startTime":
                    LocalDateTime startTime = LocalDateTime.parse(jsonReader.nextString(), formatter);
                    epic.setStartTime(startTime);
                    break;
                case "duration":
                    int duration = jsonReader.nextInt();
                    epic.setDuration(duration);
                    break;
                default:
                    throw new JsonSyntaxException(jsonReader.nextString());
            }
        }
        jsonReader.endObject();
        return epic;
    }
}
