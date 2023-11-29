package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    List<Task> getPrioritet();

    Task getTask(int globalId);

    Epic getEpic(Integer globalId);

    Subtask getSubtask(int globalId);

    ArrayList<Subtask> getAllSubtaskByEpic(int globalId);

    void saveTask(Task task);

    void saveEpic(Epic epic);

    void saveSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpic();

    ArrayList<Subtask> getAllSubtask();

    void deleteTasks();

    void deleteEpics();

    void deleteSubtasks();

    void removeTask(int globalId);

    void removeEpic(int globalId);

    void removeSubtask(Integer globalId);

    List<Task> getHistory();
}
