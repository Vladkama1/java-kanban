package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.Subtask;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private HttpServer server;
    private Gson gson;
    private TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefaultHttp());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        gson = Managers.getGson();
        this.taskManager = taskManager;
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks/", this::handler);
    }
    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    private void handler(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMetod = httpExchange.getRequestMethod();
            String query = httpExchange.getRequestURI().getQuery();
            switch (requestMetod) {
                case "GET": {
                    if (query != null) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (Pattern.matches("^/tasks/task/", path)) {
                            if (id != -1) {
                                String response = gson.toJson(taskManager.getTask(id));
                                sendText(httpExchange, response);
                                break;
                            } else {
                                System.out.println("Получен не корректный id = " + pathId);
                                httpExchange.sendResponseHeaders(405, 0);
                                break;
                            }
                        }
                        if (Pattern.matches("^/tasks/subtask/", path)) {
                            if (id != -1) {
                                String response = gson.toJson(taskManager.getSubtask(id));
                                sendText(httpExchange, response);
                                break;
                            } else {
                                System.out.println("Получен не корректный id = " + pathId);
                                httpExchange.sendResponseHeaders(405, 0);
                                break;
                            }
                        }
                        if (Pattern.matches("^/tasks/epic/", path)) {
                            if (id != -1) {
                                String response = gson.toJson(taskManager.getEpic(id));
                                sendText(httpExchange, response);
                                break;
                            } else {
                                System.out.println("Получен не корректный id = " + pathId);
                                httpExchange.sendResponseHeaders(405, 0);
                                break;
                            }
                        }
                    } else {
                        if (Pattern.matches("^/tasks/task/", path)) {
                            String response = gson.toJson(taskManager.getAllTasks());
                            sendText(httpExchange, response);
                            return;
                        }
                        if (Pattern.matches("^/tasks/subtask/", path)) {
                            String response = gson.toJson(taskManager.getAllSubtask());
                            sendText(httpExchange, response);
                            return;
                        }
                        if (Pattern.matches("^/tasks/epic/", path)) {
                            String response = gson.toJson(taskManager.getAllEpic());
                            sendText(httpExchange, response);
                            return;
                        }
                    }
                }
                case "DELETE": {
                    if (query != null) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (Pattern.matches("^/tasks/task/", path)) {
                            if (id != -1) {
                                taskManager.removeTask(id);
                                System.out.println("Удалили пользавателя по id = " + id);
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                System.out.println("Получен не корректный id = " + pathId);
                                httpExchange.sendResponseHeaders(405, 0);
                            }
                        } else {
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                        }
                        if (Pattern.matches("^/tasks/subtask/", path)) {
                            if (id != -1) {
                                taskManager.removeSubtask(id);
                                System.out.println("Удалили пользавателя по id = " + id);
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                System.out.println("Получен не корректный id = " + pathId);
                                httpExchange.sendResponseHeaders(405, 0);
                            }
                        } else {
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                        }
                        if (Pattern.matches("^/tasks/epic/", path)) {
                            if (id != -1) {
                                taskManager.removeEpic(id);
                                System.out.println("Удалили пользавателя по id = " + id);
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                System.out.println("Получен не корректный id = " + pathId);
                                httpExchange.sendResponseHeaders(405, 0);
                            }
                        } else {
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    } else {
                        if (Pattern.matches("^/tasks/task/", path)) {
                            taskManager.deleteTasks();
                            httpExchange.sendResponseHeaders(200, 0);
                        }
                        if (Pattern.matches("^/tasks/subtask/", path)) {
                            taskManager.deleteSubtasks();
                            httpExchange.sendResponseHeaders(200, 0);
                        }
                        if (Pattern.matches("^/tasks/epic/", path)) {
                            taskManager.deleteEpics();
                            httpExchange.sendResponseHeaders(200, 0);
                        }
                    }
                }
                case "POST": {
                    if (Pattern.matches("^/tasks/task/", path)) {
                        String requestBody = readText(httpExchange);
                        Task task = gson.fromJson(requestBody, Task.class);
                        taskManager.saveTask(task);
                        String response = gson.toJson(task);
                        sendText(httpExchange, response);
                        break;
                    }
                    if (Pattern.matches("^/tasks/subtask/", path)) {
                        String requestBody = readText(httpExchange);
                        Subtask subtask = gson.fromJson(requestBody, Subtask.class);
                        taskManager.saveSubtask(subtask);
                        String response = gson.toJson(subtask);
                        sendText(httpExchange, response);
                        break;
                    }
                    if (Pattern.matches("^/tasks/epic/", path)) {
                        String requestBody = readText(httpExchange);
                        Epic epic = gson.fromJson(requestBody, Epic.class);
                        taskManager.saveEpic(epic);
                        String response = gson.toJson(epic);
                        sendText(httpExchange, response);
                        break;
                    }
                    httpExchange.sendResponseHeaders(405, 0);
                    break;
                }
                default: {
                    System.out.println("Ждем GET или DELETE запрос, а получили - " + requestMetod);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }

    }

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}
