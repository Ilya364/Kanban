package kanban;

import kanban.server.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTasksServer tasksServer = new HttpTasksServer();
        tasksServer.start();
    }
}
