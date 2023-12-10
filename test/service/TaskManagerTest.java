package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected Task task1;
    protected Epic epic1;
    protected Subtask subtask1;

    @Test
    void checPrioritet() {
        Task task2 = new Task("task2", "description1", 12,
                LocalDateTime.of(2004, 12, 12, 12, 1));
        Task task3 = new Task("task2", "description1", 12,
                LocalDateTime.of(2005, 12, 12, 12, 1));
        taskManager.saveTask(task2);
        taskManager.saveTask(task3);
        List<Task> priorit = taskManager.getPrioritet();
        List<Task> prioritets = new ArrayList<>();
        prioritets.add(task1);
        prioritets.add(subtask1);
        prioritets.add(task2);
        prioritets.add(task3);
        assertEquals(priorit, prioritets);
    }

    @Test
    void getEpic() {
        final int epicId = epic1.getId();
        final Epic savedEpic = taskManager.getEpic(epicId);
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic1, savedEpic, "Задачи не совпадают.");
    }

    @Test
    void getSubtask() {
        final int subtaskId = subtask1.getId();
        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask1, savedSubtask, "Задачи не совпадают.");
    }

    @Test
    void getTask() {
        final int taskId = task1.getId();
        final Task savedTask = taskManager.getTask(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");
    }

    @Test
    void getAllSubtaskByEpic() {
        final int subtaskId = subtask1.getId();
        final List<Subtask> subtasks = taskManager.getAllSubtask();
        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask1, subtasks.get(0), "Задачи не совпадают.");
        List<Integer> sabId = List.of(subtaskId);
        assertArrayEquals(sabId.toArray(), epic1.getSubtasksId().toArray());
    }

    @Test
    void saveTask() {
        final int taskId = task1.getId();
        final Task savedTask = taskManager.getTask(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");
        final List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void saveEpic() {
        final int epicId = epic1.getId();
        final Epic savedEpic = taskManager.getEpic(epicId);
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic1, savedEpic, "Задачи не совпадают.");
        final List<Epic> epics = taskManager.getAllEpic();
        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic1, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void saveSubtask() {
        final int subtaskId = subtask1.getId();
        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask1, savedSubtask, "Задачи не совпадают.");
        final List<Subtask> subtasks = taskManager.getAllSubtask();
        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask1, subtasks.get(0), "Задачи не совпадают.");
        List<Integer> sabId = List.of(subtaskId);
        assertArrayEquals(sabId.toArray(), epic1.getSubtasksId().toArray());
    }

    @Test
    void updateEpic() {
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
    void updateEpicEmptyMap() {
        final int epicId = epic1.getId();
        final Epic savedEpic = taskManager.getEpic(epicId);
        String name = "epic new name";
        savedEpic.setName(name);
        taskManager.deleteEpics();
        assertTrue(taskManager.getAllEpic().isEmpty());
        taskManager.updateEpic(savedEpic);
        final Epic epicUpdate = taskManager.getEpic(epicId);
        assertNull(epicUpdate, "Задача не найдена.");
    }

    @Test
    void updateEpicNull() {
        final int epicId = epic1.getId();
        final Epic savedEpic = taskManager.getEpic(epicId);
        String name = "epic new name";
        Integer id = null;
        savedEpic.setName(name);
        savedEpic.setId(id);
        taskManager.updateEpic(savedEpic);
        final Epic epicUpdate = taskManager.getEpic(id);
        assertNull(epicUpdate, "Задача не найдена.");
        assertNotEquals(taskManager.getEpic(epicId), epicUpdate, "Задача разные.");
    }


    @Test
    void updateTask() {
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
    void updateTaskEmptyMap() {
        final int taskId = task1.getId();
        final Task savedTask = taskManager.getTask(taskId);
        String name = "task new name";
        savedTask.setName(name);
        taskManager.deleteTasks();
        assertTrue(taskManager.getAllTasks().isEmpty());
        taskManager.updateTask(savedTask);
        final Task taskUpdate = taskManager.getTask(taskId);
        assertNull(taskUpdate, "Задача не найдена.");
    }

    @Test
    void updateTaskNull() {
        final int taskId = task1.getId();
        final Task savedTask = taskManager.getTask(taskId);
        String name = "task new name";
        Integer id = null;
        savedTask.setName(name);
        savedTask.setId(id);
        taskManager.updateTask(savedTask);
        final Task taskUpdate = taskManager.getEpic(id);
        assertNull(taskUpdate, "Задача не найдена.");
        assertNotEquals(taskManager.getTask(taskId), taskUpdate, "Задача разные.");
    }

    @Test
    void updateSubtask() {
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
    void updateSubtaskEmptyMap() {
        final int subtaskId = subtask1.getId();
        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);
        String name = "subtask new name";
        savedSubtask.setName(name);
        taskManager.deleteSubtasks();
        assertTrue(taskManager.getAllSubtask().isEmpty());
        taskManager.updateSubtask(savedSubtask);
        final Subtask subtaskUpdate = taskManager.getSubtask(subtaskId);
        assertNull(subtaskUpdate, "Задача не найдена.");
    }

    @Test
    void getAllTasks() {
        final List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void getAllEpic() {
        final List<Epic> epics = taskManager.getAllEpic();
        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic1, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void getAllSubtask() {
        final int subtaskId = subtask1.getId();
        final List<Subtask> subtasks = taskManager.getAllSubtask();
        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask1, subtasks.get(0), "Задачи не совпадают.");
        List<Integer> sabId = List.of(subtaskId);
        assertArrayEquals(sabId.toArray(), epic1.getSubtasksId().toArray());
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
        assertTrue(taskManager.getAllSubtask().isEmpty());
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
}