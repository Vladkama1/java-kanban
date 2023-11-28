package service;

import exception.ManagerSaveException;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest<T extends TaskManager> extends TaskManagerTest<FileBackedTasksManager> {
    File file;
    private static final String FIRST_LINE = "id,type,name,status,description,startTime,duration,epic";

    @BeforeEach
    public void start() {
        file = new File("./src/resources/history.csv");
        taskManager = new FileBackedTasksManager(file);
        task1 = new Task("task1", "description1", 12,
                LocalDateTime.of(2000, 12, 12, 12, 1));
        taskManager.saveTask(task1);
        epic1 = new Epic("Epic11", "description11");
        taskManager.saveEpic(epic1);
        subtask1 = new Subtask("Sub111", "description111", 12,
                LocalDateTime.of(2000, 12, 12, 12, 16), epic1.getId());
        taskManager.saveSubtask(subtask1);
    }

    @Test
    @Override
    void saveTask() {
        super.saveTask();
        assertTrue(file.exists());
        String taskToStr = "1,TASK,task1,NEW,description1,2000-12-12T12:01,12,";
        String[] split = readFile();
        assertEquals(taskToStr, split[1]);
    }


    @Test
    @Override
    void saveEpic() {
        super.saveEpic();
        assertTrue(file.exists());
        String taskToStr = "2,EPIC,Epic11,NEW,description11,";
        String[] split = readFile();
        assertEquals(taskToStr, split[2]);
    }

    @Test
    @Override
    void saveSubtask() {
        super.saveSubtask();
        assertTrue(file.exists());
        String taskToStr = "3,SUBTASK,Sub111,NEW,description111,2000-12-12T12:16,12,2";
        String[] split = readFile();
        assertEquals(taskToStr, split[3]);
    }

    @Test
    void loadFromFile() {
        String line = "3";
        List<Integer> list = new ArrayList<>();
        list.add(3);
        List<Integer> lislId = HistoryStaticManager.historyFromString(line);
        assertEquals(list, lislId);
        assertNotNull(lislId, "Список пуст!");
        assertTrue(file.exists());
    }

    @Test
    @Override
    void getTask() {
        super.getTask();
        final int taskId = task1.getId();
        final Task savedTask = taskManager.getTask(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");
    }

    @Test
    @Override
    void getEpic() {
        super.getEpic();
        final int epicId = epic1.getId();
        final Epic savedEpic = taskManager.getEpic(epicId);
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic1, savedEpic, "Задачи не совпадают.");
    }

    @Test
    @Override
    void getSubtask() {
        super.getSubtask();
        final int subtaskId = subtask1.getId();
        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask1, savedSubtask, "Задачи не совпадают.");
    }

    @Test
    @Override
    void getAllSubtaskByEpic() {
        super.getAllSubtaskByEpic();
        final int subtaskId = subtask1.getId();
        final List<Subtask> subtasks = taskManager.getAllSubtask();
        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask1, subtasks.get(0), "Задачи не совпадают.");
        List<Integer> sabId = List.of(subtaskId);
        assertArrayEquals(sabId.toArray(), epic1.getSubtasksId().toArray());
    }

    @Test
    @Override
    void updateEpic() {
        super.updateEpic();
        final int epicId = epic1.getId();
        final Epic savedEpic = taskManager.getEpic(epicId);
        String name = "epic new name";
        savedEpic.setName(name);
        savedEpic.setStatus(Status.IN_PROGRESS);
        taskManager.updateEpic(savedEpic);
        final Epic epicUpdate = taskManager.getEpic(epicId);
        assertNotNull(epicUpdate, "Задача не найдена.");
        assertEquals(name, epicUpdate.getName(), "Имя не совпадает!");
        assertEquals(Status.NEW, epicUpdate.getStatus(), "Статус не совпадает!");
    }

    @Test
    @Override
    void updateTask() {
        super.updateTask();
        final int taskId = task1.getId();
        final Task savedTask = taskManager.getTask(taskId);
        String name = "task new name";
        savedTask.setName(name);
        savedTask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(savedTask);
        final Task taskUpdate = taskManager.getTask(taskId);
        assertNotNull(taskUpdate, "Задача не найдена.");
        assertEquals(name, taskUpdate.getName(), "Имя не совпадает!");
        assertEquals(Status.IN_PROGRESS, taskUpdate.getStatus(), "Статус не совпадает!");
    }

    @Test
    @Override
    void updateSubtask() {
        super.updateSubtask();
        final int subtask1Id = subtask1.getId();
        final Subtask savedSubtask = taskManager.getSubtask(subtask1Id);
        String name = "subtask new name";
        savedSubtask.setName(name);
        savedSubtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(savedSubtask);
        final Subtask subtaskUpdate = taskManager.getSubtask(subtask1Id);
        assertNotNull(subtaskUpdate, "Задача не найдена.");
        assertEquals(name, subtaskUpdate.getName(), "Имя не совпадает!");
        assertEquals(Status.IN_PROGRESS, subtaskUpdate.getStatus(), "Статус не совпадает!");
        final Epic savedEpic = taskManager.getEpic(savedSubtask.getEpicId());
        String name1 = "subtask new name";
        savedEpic.setName(name);
        savedEpic.setStatus(Status.IN_PROGRESS);
        taskManager.updateEpic(savedEpic);
        final Epic epicUpdate = taskManager.getEpic(savedSubtask.getEpicId());
        assertNotNull(epicUpdate, "Задача не найдена.");
        assertEquals(name1, epicUpdate.getName(), "Имя не совпадает!");
        assertEquals(Status.IN_PROGRESS, epicUpdate.getStatus(), "Статус не совпадает!");
    }

    @Test
    void deleteTasks() {
        assertTrue(!taskManager.getAllTasks().isEmpty());
        taskManager.deleteTasks();
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    void deleteEpics() {
        assertTrue(!taskManager.getAllEpic().isEmpty());
        taskManager.deleteEpics();
        assertTrue(taskManager.getAllEpic().isEmpty());
    }

    @Test
    void deleteSubtasks() {
        assertTrue(!taskManager.getAllSubtask().isEmpty());
        taskManager.deleteSubtasks();
        assertTrue(taskManager.getAllSubtask().isEmpty());
    }

    @Test
    void removeTask() {
        Task task = taskManager.getTask(task1.getId());
        assertNotNull(task);
        taskManager.removeTask(task.getId());
        assertNotNull(task, "Метод удаления задочи не работает");
    }

    @Test
    void removeEpic() {
        Epic epic = taskManager.getEpic(epic1.getId());
        assertNotNull(epic);
        taskManager.removeEpic(epic.getId());
        assertNotNull(epic, "Метод удаления задочи не работает");
    }

    @Test
    void removeSubtask() {
        Subtask subtask = taskManager.getSubtask(subtask1.getId());
        assertNotNull(subtask);
        taskManager.removeSubtask(subtask.getId());
        assertNotNull(subtask, "Метод удаления задочи не работает");
    }

    private String[] readFile() {
        try {
            String e = Files.readString(file.toPath());
            return e.split("\n");
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка, файл не считан: " + file.getName(), exception);
        }
    }
}