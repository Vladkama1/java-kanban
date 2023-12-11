package http;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;
import service.Managers;
import service.TaskManagerTest;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    KVServer kvServer;

    @BeforeEach
    void start() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        super.taskManager = Managers.getDefaultHttp();
        task1 = new Task("task1", "description1", 12,
                LocalDateTime.of(2000, 12, 12, 12, 1));
        taskManager.saveTask(task1);
        epic1 = new Epic("Epic11", "description11");
        taskManager.saveEpic(epic1);
        subtask1 = new Subtask("Sub111", "description111", 12,
                LocalDateTime.of(2001, 12, 12, 12, 16), epic1.getId());
        taskManager.saveSubtask(subtask1);
    }

    @AfterEach
    void stop() {
        kvServer.stop();
    }

    @Test
    void load() {
        taskManager.getTask(task1.getId());
        taskManager.getSubtask(subtask1.getId());
        taskManager.getEpic(epic1.getId());
        HttpTaskManager httpTaskManagerLoad = new HttpTaskManager("http://localhost:8078", true);
        assertEquals(taskManager.getAllTasks().toString(), httpTaskManagerLoad.getAllTasks().toString());
        assertEquals(taskManager.getAllSubtask().toString(), httpTaskManagerLoad.getAllSubtask().toString());
        assertEquals(taskManager.getAllEpic().toString(), httpTaskManagerLoad.getAllEpic().toString());
        assertEquals(taskManager.getHistory().toString(), httpTaskManagerLoad.getHistory().toString());
        assertEquals(taskManager.getPrioritet().toString(), httpTaskManagerLoad.getPrioritet().toString());
    }
}
