import com.sun.net.httpserver.HttpServer;
import http.HttpTaskServer;
import model.Epic;
import model.Subtask;
import model.Task;
import server.KVServer;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        TaskManager taskManager = Managers.getDefaultHttp();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
        Task task1 = new Task("task1", "description1", 12,
                LocalDateTime.of(2000, 12, 12, 12, 1));
        taskManager.saveTask(task1);
        Epic epic1 = new Epic("Epic11", "description11");
        taskManager.saveEpic(epic1);
        Subtask subtask1 = new Subtask("Sub111", "description111", 12,
                LocalDateTime.of(2001, 12, 12, 12, 16), epic1.getId());
        taskManager.saveSubtask(subtask1);
    }

    public static void print(TaskManager taskManager) {
        System.out.println("Сейчас размер истории - " + taskManager.getHistory().size());
        System.out.println("Начинается с Id - " + taskManager.getHistory().get(0).getId());
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}
