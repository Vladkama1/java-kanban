package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private Integer globalId = 0;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private Integer creatId() {
        return ++globalId;
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic.getSubtasksId().isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            ArrayList<Integer> arrayList = epic.getSubtasksId();
            ArrayList<Status> subtasksStatus = new ArrayList<>();
            for (Integer i : arrayList) {
                subtasksStatus.add(subtasks.get(i).getStatus());
            }
            if (!subtasksStatus.contains(Status.DONE) && !subtasksStatus.contains(Status.IN_PROGRESS)) {
                epic.setStatus(Status.NEW);
            } else if (!subtasksStatus.contains(Status.NEW) && !subtasksStatus.contains(Status.IN_PROGRESS)) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    @Override
    public Task getTask(int globalId) {
        if (tasks.containsKey(globalId)) {
            historyManager.addTask(tasks.get(globalId));
        }
        return tasks.get(globalId);
    }

    @Override
    public Epic getEpic(int globalId) {
        if (epics.containsKey(globalId)) {
            historyManager.addTask(epics.get(globalId));
        }
        return epics.get(globalId);
    }

    @Override
    public Subtask getSubtask(int globalId) {
        if (subtasks.containsKey(globalId)) {
            historyManager.addTask(subtasks.get(globalId));
        }
        return subtasks.get(globalId);
    }

    @Override
    public ArrayList<Subtask> getAllSubtaskByEpic(int globalId) {
        ArrayList<Subtask> allSubtasksId = new ArrayList<>();
        if (epics.containsKey(globalId)) {
            ArrayList<Integer> subtasksId = epics.get(globalId).getSubtasksId();
            for (Integer id : subtasksId) {
                allSubtasksId.add(subtasks.get(id));
            }
        }
        return allSubtasksId;
    }

    @Override
    public void saveTask(Task task) {
        if (task != null) {
            task.setId(creatId());
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void saveEpic(Epic epic) {
        if (epic != null) {
            epic.setId(creatId());
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        if (subtask != null) {
            subtask.setId(creatId());
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            ArrayList<Integer> subtasksId = epic.getSubtasksId();
            subtasksId.add(subtask.getId());
            updateEpic(epic);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null && epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic.getId());
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task != null && tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null && subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            updateEpicStatus(epic.getId());
        }
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteTasks() {
        for (Integer taskId : tasks.keySet()) {
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (Integer epicId : epics.keySet()) {
            historyManager.remove(epicId);
        }
        epics.clear();
        deleteSubtasks();
    }

    @Override
    public void deleteSubtasks() {
        for (Integer subtaskId : subtasks.keySet()) {
            historyManager.remove(subtaskId);
        }
        subtasks.clear();
        if (!epics.isEmpty()) {
            for (Epic values : epics.values()) {
                values.setStatus(Status.NEW);
                values.setSubtasksId(new ArrayList<>());
            }
        }
    }

    @Override
    public void removeTask(int globalId) {
        historyManager.remove(globalId);
        tasks.remove(globalId);
    }

    @Override
    public void removeEpic(int globalId) {
        if (epics.containsKey(globalId)) {
            historyManager.remove(globalId);
            Epic epic = epics.remove(globalId);
            ArrayList<Integer> Integer = epic.getSubtasksId();
            for (Integer id : Integer) {
                historyManager.remove(id);
                subtasks.remove(id);
            }
        }
    }

    @Override
    public void removeSubtask(Integer globalId) {
        if (globalId != null && subtasks.containsKey(globalId)) {
            Epic epic = epics.get(subtasks.get(globalId).getEpicId());
            epic.getSubtasksId().remove(globalId);
            updateEpic(epic);
            historyManager.remove(globalId);
            subtasks.remove(globalId);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
