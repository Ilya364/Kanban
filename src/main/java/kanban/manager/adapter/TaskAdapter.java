package kanban.manager.adapter;

import kanban.task.Task;
import kanban.task.Status;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskAdapter extends TypeAdapter<Task> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");
    @Override
    public void write(JsonWriter jsonWriter, Task task) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("id").value(task.getId());
        jsonWriter.name("name").value(task.getName());
        jsonWriter.name("description").value(task.getDescription());
        jsonWriter.name("status").value(task.getStatus().toString());
        if (task.getStartTime() != null) {
            jsonWriter.name("startTime").value(task.getStartTime().format(formatter));
            jsonWriter.name("duration").value(task.getDuration().toMinutes());
        }
        jsonWriter.endObject();
    }
    
    @Override
    public Task read(JsonReader jsonReader) throws IOException {
        final Task task = new Task();
        task.setId(-1);
        
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            switch (jsonReader.nextName()) {
                case "id":
                    task.setId(jsonReader.nextInt());
                    break;
                case "name":
                    task.setName(jsonReader.nextString());
                    break;
                case "description":
                    task.setDescription(jsonReader.nextString());
                    break;
                case "status":
                    task.setStatus(Status.valueOf(jsonReader.nextString()));
                    break;
                case "startTime":
                    LocalDateTime startTime = LocalDateTime.parse(jsonReader.nextString(), formatter);
                    task.setStartTime(startTime);
                    break;
                case "duration":
                    int duration = jsonReader.nextInt();
                    task.setDuration(duration);
                    break;
                default:
                    throw new JsonSyntaxException(task.toString());
            }
        }
        jsonReader.endObject();
        return task;
    }
}
