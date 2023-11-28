package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    TaskManager taskManager;
    Task task1;
    Task task2;
    Epic epic1;
    Subtask subtask1;

    @BeforeEach
    public void start() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager();
        task1 = new Task("task1", "description1", 12,
                LocalDateTime.of(2000, 12, 12, 12, 1));
        task2 = new Task("task2", "description1", 12,
                LocalDateTime.of(2001, 12, 12, 12, 1));
        epic1 = new Epic("Epic11", "description11");
        subtask1 = new Subtask("Sub111", "description111", 12,
                LocalDateTime.of(2002, 12, 12, 12, 16), 7);
    }

    @Test
    void getHistory() {
        task1.setId(1);
        epic1.setId(2);
        subtask1.setId(3);
        historyManager.addTask(task1);
        historyManager.addTask(epic1);
        historyManager.addTask(subtask1);
        List<Task> listTask = historyManager.getHistory();
        assertEquals(3, listTask.size());
    }

    @Test
    void addTask() {
        task1.setId(1);
        task2.setId(2);
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task1);
        List<Task> listTask = historyManager.getHistory();
        assertEquals(2, listTask.size());
        assertEquals(task1, listTask.get(1));
    }

    @Test
    void remove() {
        task1.setId(1);
        task2.setId(2);
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        List<Task> listTasks = historyManager.getHistory();
        assertEquals(2, listTasks.size());
        historyManager.remove(1);
        List<Task> listTask = historyManager.getHistory();
        assertEquals(1, listTask.size());
    }
}