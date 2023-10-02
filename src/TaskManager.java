import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class TaskManager {
    private Integer globalId = 0;
    private HashMap<Integer, Task> tasksMap = new HashMap<>();
    private HashMap<Integer, Epic> epicsMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtasksMap = new HashMap<>();

    private Integer creatId() {
        return ++globalId;
    }

    public Task getTask(int globalId) {
        return tasksMap.get(globalId);
    }

    public Epic getEpic(int globalId) {
        return epicsMap.get(globalId);
    }

    public Subtask getSubtask(int globalId) {
        return subtasksMap.get(globalId);
    }

    public ArrayList<Subtask> getAllSubtaskByEpic(int globalId) {
        ArrayList<Subtask> allSubtasksId = new ArrayList<>();
        if (epicsMap.containsKey(globalId)) {
            ArrayList<Integer> arrayList = epicsMap.get(globalId).getSubtasksId();
            for (Object i : arrayList) {
                allSubtasksId.add(subtasksMap.get(i));
            }
        }
        return allSubtasksId;
    }

    public void saveTask(Task task) {
        if (task != null) {
            task.setId(creatId());
            tasksMap.put(task.getId(), task);
        }
    }

    public void saveEpic(Epic epic) {
        if (epic != null) {
            epic.setId(creatId());
            epicsMap.put(epic.getId(), epic);
        }
    }

    public void saveSubtask(Subtask subtask) {
        if (subtask != null) {
            subtask.setId(creatId());
            subtasksMap.put(subtask.getId(), subtask);
            Epic epic = epicsMap.get(subtask.getEpicId());// -
            ArrayList<Integer> subtasksId = epic.getSubtasksId();//ссылка-
            subtasksId.add(subtask.getId());//-
            updateEpic(epic);
        }
    }

    public void updateEpic(Epic epic) {
        if (epic != null && epicsMap.containsKey(epic.getId())) {
            if (epic.getSubtasksId().isEmpty()) {
                epic.setStatus("NEW");
                epicsMap.put(epic.getId(), epic);
            } else {
                ArrayList<Integer> subtasksId = epic.getSubtasksId();
                ArrayList<String> subtasksStatus = new ArrayList<>();
                for (Integer id : subtasksId) {
                    subtasksStatus.add(subtasksMap.get(id).getStatus());
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
    }

    public void updateTask(Task task) {
        if (task != null && tasksMap.containsKey(task.getId())) {
            tasksMap.put(task.getId(), task);
            String tasksStatus = task.getStatus();
            if (!tasksStatus.contains("NEW") && !tasksStatus.contains("DONE")) {
                task.setStatus("IN_PROGRESS");
            } else if (!tasksStatus.contains("IN_PROGRESS") && !tasksStatus.contains("DONE")) {
                task.setStatus("NEW");
            } else {
                task.setStatus("DONE");
            }
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtask != null && subtasksMap.containsKey(subtask.getId())) {
            subtask.setStatus("NEW");
            subtasksMap.put(subtask.getId(), subtask);
            String subtasksStatus = subtask.getStatus();
            if (!subtasksStatus.contains("NEW") && !subtasksStatus.contains("IN_PROGRESS")) {
                subtask.setStatus("DONE");
            } else if (!subtasksStatus.contains("NEW") && !subtasksStatus.contains("DONE")) {
                subtask.setStatus("IN_PROGRESS");
            } else {
                subtask.setStatus("NEW");
            }

        }
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasksMap.values());
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epicsMap.values());
    }

    public ArrayList<Subtask> getAllSubtask() {
        return new ArrayList<>(subtasksMap.values());
    }

    public void deleteTasks() {
        tasksMap.clear();
    }

    public void deleteEpics() {
        epicsMap.clear();
        deleteSubtasks();
    }

    public void deleteSubtasks() {
        subtasksMap.clear();
        if (!epicsMap.isEmpty()) {
            for (Epic values : epicsMap.values()) {
                values.setStatus("NEW");
                values.setSubtasksId(new ArrayList<>());
            }
        }
    }

    public void removeTask(int globalId) {
        tasksMap.remove(globalId);
    }

    public void removeEpic(int globalId) {
        epicsMap.remove(globalId);
        subtasksMap.remove(globalId);
    }

    public void removeSubtask(int globalId) {
        subtasksMap.remove(globalId);
        if (epicsMap.containsKey(globalId)) {
            for (Integer key : epicsMap.keySet()) {
                if (key == globalId) {
                    epicsMap.remove(globalId);
                    for (Epic values : epicsMap.values()) {
                        values.setStatus("NEW");
                        values.setSubtasksId(new ArrayList<>());
                    }
                }
            }
        }
    }

    public void printTask() {
        for (Task description : tasksMap.values()) {
            System.out.println(description);
        }
    }

    public void printEpic() {
        for (Task description : epicsMap.values()) {
            System.out.println(description);
        }
    }

    public void printSubtask() {
        for (Task description : subtasksMap.values()) {
            System.out.println(description);
        }
    }
}
