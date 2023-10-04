package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class TaskManager {
    private Integer globalId = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private Integer creatId() {
        return ++globalId;
    }

    public Task getTask(int globalId) {
        return tasks.get(globalId);
    }

    public Epic getEpic(int globalId) {
        return epics.get(globalId);
    }

    public Subtask getSubtask(int globalId) {
        return subtasks.get(globalId);
    }

    public ArrayList<Subtask> getAllSubtaskByEpic(int globalId) {
        ArrayList<Subtask> allSubtasksId = new ArrayList<>();
        if (epics.containsKey(globalId)) {
            ArrayList<Integer> Integer = epics.get(globalId).getSubtasksId();
            for (Object i : Integer) {
                allSubtasksId.add(subtasks.get(i));
            }
        }
        return allSubtasksId;
    }

    public void saveTask(Task task) {
        if (task != null) {
            task.setId(creatId());
            tasks.put(task.getId(), task);
        }
    }

    public void saveEpic(Epic epic) {
        if (epic != null) {
            epic.setId(creatId());
            epics.put(epic.getId(), epic);
        }
    }

    public void saveSubtask(Subtask subtask) {
        if (subtask != null) {
            subtask.setId(creatId());
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());// -
            ArrayList<Integer> subtasksId = epic.getSubtasksId();//ссылка-
            subtasksId.add(subtask.getId());//-
            updateEpic(epic);
        }
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.getSubtasksId().isEmpty()) {
            epic.setStatus("NEW");
        } else {
            ArrayList<Integer> arrayList = epic.getSubtasksId();
            ArrayList<String> subtasksStatus = new ArrayList<>();
            for (Integer i : arrayList) {
                subtasksStatus.add(subtasks.get(i).getStatus());
            }
            if (!subtasksStatus.contains("NEW") && !subtasksStatus.contains("IN_PROGRESS")) {
                epic.setStatus("DONE");
            } else if (!subtasksStatus.contains("NEW") && !subtasksStatus.contains("DONE")) {
                epic.setStatus("IN_PROGRESS");
            } else {
                epic.setStatus("NEW");
            }
        }
    }

    public void updateEpic(Epic epic) {
        if (epic != null && epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic);
        }
    }

    public void updateTask(Task task) {//+
        if (task != null && tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateSubtask(Subtask subtask) {//+
        if (subtask != null && subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            updateEpicStatus(epic);
        }
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteSubtasks() {
        subtasks.clear();
        if (!epics.isEmpty()) {
            for (Epic values : epics.values()) {
                values.setStatus("NEW");
                values.setSubtasksId(new ArrayList<>());
            }
        }
    }

    public void removeTask(int globalId) {
        tasks.remove(globalId);
    }

    public void removeEpic(int globalId) {//+
        if (epics.containsKey(globalId)) {
            Epic epic = epics.get(globalId);
            ArrayList<Integer> Integer = epic.getSubtasksId();
            for (Integer i : Integer) {
                subtasks.remove(i);
            }
            epics.remove(globalId);
        }
    }

    public void removeSubtask(Integer globalId) {//+
        if (globalId != null && subtasks.containsKey(globalId)) {
            Epic epic = epics.get(subtasks.get(globalId).getEpicId());
            epic.getSubtasksId().remove(globalId);
            updateEpic(epic);
            subtasks.remove(globalId);
        }
    }

    public void printTask() {
        for (Task description : tasks.values()) {
            System.out.println(description);
        }
    }

    public void printEpic() {
        for (Task description : epics.values()) {
            System.out.println(description);
        }
    }

    public void printSubtask() {
        for (Task description : subtasks.values()) {
            System.out.println(description);
        }
    }
}
