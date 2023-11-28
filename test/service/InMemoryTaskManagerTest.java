package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;

public class InMemoryTaskManagerTest<T extends TaskManager> extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void start() {
        taskManager = new InMemoryTaskManager();
        task1 = new Task("task1", "description1", 12,
                LocalDateTime.of(2000, 12, 12, 12, 1));
        taskManager.saveTask(task1);
        epic1 = new Epic("Epic11", "description11");
        taskManager.saveEpic(epic1);
        subtask1 = new Subtask("Sub111", "description111", 12,
                LocalDateTime.of(2000, 12, 12, 12, 16), epic1.getId());
        taskManager.saveSubtask(subtask1);
    }
}
