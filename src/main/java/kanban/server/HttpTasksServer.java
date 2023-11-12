package kanban.server;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.io.*;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import com.google.gson.*;
import com.sun.net.httpserver.*;
import kanban.manager.exception.*;
import kanban.manager.adapter.*;
import kanban.manager.memory.TasksManager;
import kanban.manager.file.FileBackedTasksManager;
import kanban.manager.memory.Managers;
import kanban.task.*;

public class HttpTasksServer {
    private static final int PORT = 8080;
    private static final int BACKLOG = 0;
    private final HttpServer httpServer;
    private final String filename = "kanban.csv";
    private TasksManager manager = Managers.getDefault();
    
    public HttpTasksServer() throws IOException, InterruptedException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), BACKLOG);
        httpServer.createContext("/tasks", new HttpTasksHandler());
    }
    
    public void start() {
        httpServer.start();
    }
    
    public void stop() {
        httpServer.stop(0);
    }
    
    class HttpTasksHandler implements HttpHandler {
        private Task getTask(int id, TaskType taskType) {
            switch (taskType) {
                case TASK:
                    return manager.getTask(id);
                case EPIC:
                    return manager.getEpic(id);
                case SUBTASK:
                    return manager.getSubtask(id);
                default:
                    return null;
            }
        }
        
        private void removeTask(int id, TaskType taskType) {
            switch (taskType) {
                case TASK:
                    manager.removeTask(id);
                    break;
                case EPIC:
                    manager.removeEpic(id);
                    break;
                case SUBTASK:
                    manager.removeSubtask(id);
                    break;
            }
        }
        
        private void addOrUpdateTask(Task task, TaskType taskType) {
            int id = task.getId();
            switch (taskType) {
                case TASK:
                    if (manager.getTask(id) != null) {
                        manager.updateTask((Task) task);
                    } else {
                        manager.addTask(task);
                    }
                    break;
                case EPIC:
                    if (manager.getEpic(id) != null) {
                        manager.updateEpic((Epic) task);
                    } else {
                        manager.addEpic((Epic) task);
                    }
                    break;
                case SUBTASK:
                    if (manager.getSubtask(id) != null) {
                        manager.updateSubtask((Subtask) task);
                    } else {
                        manager.addSubtask((Subtask) task);
                    }
            }
        }
        
        private void sendResponseBody(String response, HttpExchange exchange) throws IOException {
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                final RequestMethod method;
                try {
                    method = RequestMethod.valueOf(exchange.getRequestMethod());
                } catch (IllegalArgumentException e) {
                    Headers headers = exchange.getResponseHeaders();
                    headers.add("Allow", "GET, POST, DELETE");
                    throw new IllegalMethodException("Method is not allowed.");
                }
                
                final String[] pathElements = exchange.getRequestURI().toString().split("/");
                String response;
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Task.class, new TaskAdapter())
                        .registerTypeAdapter(Epic.class, new EpicAdapter())
                        .registerTypeAdapter(Subtask.class, new SubtaskAdapter())
                        .setPrettyPrinting()
                        .create();
                final int pathLength = pathElements.length;
                switch (pathLength) {
                    case 2:
                        switch (method) {
                            case GET:
                                response = gson.toJson(manager.getPrioritizedTasks(), ArrayList.class);
                                exchange.sendResponseHeaders(200, 0);
                                sendResponseBody(response, exchange);
                                break;
                            case DELETE:
                                manager.removeAllTasks();
                                manager.removeAllEpics();
                                exchange.sendResponseHeaders(204, -1);
                                break;
                            default:
                                Headers headers = exchange.getResponseHeaders();
                                headers.add("Allow", "GET, DELETE");
                                throw new IllegalMethodException("Method is not allowed for such resource.");
                        }
                    case 3:
                        switch (method) {
                            case POST:
                                try {
                                    final TaskType taskType = TaskType.valueOf(pathElements[2].toUpperCase(Locale.ROOT));
                                    InputStream inputStream = exchange.getRequestBody();
                                    final String body = new String(inputStream.readAllBytes());
                                    Type type = Object.class;
                                    switch (taskType) {
                                        case TASK:
                                            type = Task.class;
                                            break;
                                        case SUBTASK:
                                            type = Subtask.class;
                                            break;
                                        case EPIC:
                                            type = Epic.class;
                                    }
                                    addOrUpdateTask(gson.fromJson(body, type), taskType);
                                    exchange.sendResponseHeaders(204, -1);
                                } catch (IllegalArgumentException e) {
                                    throw new NotFoundException("Such type of tasks does not exist.");
                                }
                                break;
                            case GET:
                                if (Objects.equals(pathElements[2], "history")) {
                                    response = gson.toJson(manager.getHistory(), ArrayList.class);
                                    exchange.sendResponseHeaders(200, 0);
                                    sendResponseBody(response, exchange);
                                } else {
                                    throw new NotFoundException("\"/" + pathElements[2] + "\" not found.");
                                }
                                break;
                            default:
                                Headers headers = exchange.getResponseHeaders();
                                headers.add("Allow", "GET, POST");
                                throw new IllegalMethodException("Method is not allowed for such resource.");
                        }
                    case 4:
                        try {
                            final TaskType taskType = TaskType.valueOf(pathElements[2].toUpperCase(Locale.ROOT));
                            final int id;
                            switch (method) {
                                case GET:
                                    id = Integer.parseInt(pathElements[3].substring("?id=".length()));
                                    Task task = getTask(id, taskType);
                                    if (task == null) {
                                        throw new NotFoundException("Task with id " + id + " does not exist.");
                                    }
                                    response = gson.toJson(task);
                                    exchange.sendResponseHeaders(200, response.getBytes().length);
                                    sendResponseBody(response, exchange);
                                    break;
                                case DELETE:
                                    id = Integer.parseInt(pathElements[3].substring("?id=".length()));
                                    removeTask(id, taskType);
                                    exchange.sendResponseHeaders(204, -1);
                                    break;
                                default:
                                    Headers headers = exchange.getResponseHeaders();
                                    headers.add("Allow", "GET, DELETE");
                                    throw new IllegalMethodException("Method is not allowed for such resource.");
                            }
                        } catch (NumberFormatException e) {
                            throw new NotFoundException("Id has an incorrect format.");
                        } catch (IllegalArgumentException e) {
                            throw new NotFoundException("Such type does not exist.");
                        }
                    case 5:
                        if (method == RequestMethod.GET) {
                            if (pathElements[3].equals("epic") && pathElements[2].equals("subtask")) {
                                int id = -1;
                                try {
                                    id = Integer.parseInt(pathElements[4].substring("?id=".length()));
                                    if (manager.getEpic(id) == null) {
                                        throw new NotFoundException("Epic with id " + id + " not found.");
                                    }
                                    response = gson.toJson(manager.getEpicSubtasks(id), ArrayList.class);
                                } catch (NumberFormatException e) {
                                    throw new NotFoundException("Id has an incorrect format.");
                                }
                                exchange.sendResponseHeaders(200, 0);
                                sendResponseBody(response, exchange);
                            } else {
                                throw new NotFoundException("Invalid resource for length 5.");
                            }
                        } else {
                            Headers headers = exchange.getResponseHeaders();
                            headers.add("Allow", "GET");
                            throw new IllegalMethodException("Method is not allowed for such resource.");
                        }
                    default:
                        throw new NotFoundException("Invalid resource.");
                }
            } catch (NotFoundException e) {
                exchange.sendResponseHeaders(404, 0);
                sendResponseBody(e.getMessage(), exchange);
            } catch (IllegalMethodException e) {
                exchange.sendResponseHeaders(405, 0);
                sendResponseBody(e.getMessage(), exchange);
            } catch (JsonSyntaxException | IllegalStartTimeException e) {
                exchange.sendResponseHeaders(422, 0);
                sendResponseBody("Error in the request body.", exchange);
            }
        }
    }
}


