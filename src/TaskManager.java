import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class TaskManager {
    private Integer managerId = 0;
    private HashMap<Integer, Task> tasksMap = new HashMap<>();
    private HashMap<Integer, Epic> epicsMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtasksMap = new HashMap<>();

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
                if (subtasksStatus.contains("NEW") && subtasksStatus.contains("IN_PROGRESS")) {
                    epic.setStatus("IN_PROGRESS");
                } else if (subtasksStatus.contains("NEW") && subtasksStatus.contains("DONE")) {
                    epic.setStatus("IN_PROGRESS");
                } else if (subtasksStatus.contains("DONE ") && subtasksStatus.contains("IN_PROGRESS")) {
                    epic.setStatus("IN_PROGRESS");
                } else if (subtasksStatus.contains("NEW ") && subtasksStatus.contains("NEW")) {
                    epic.setStatus("NEW");
                } else if (subtasksStatus.contains("IN_PROGRESS ") && subtasksStatus.contains("IN_PROGRESS")) {
                    epic.setStatus("IN_PROGRESS");
                } else {
                    epic.setStatus("DONE");
                }
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

    public void deleteTask() {
        tasksMap.clear();
    }

    public void deleteEpic() {
        epicsMap.clear();
        subtasksMap.clear();
    }

    public void deleteSubtask() {
        subtasksMap.clear();
    }


    public void removeTask(int managerId) {
        tasksMap.remove(managerId);
    }

    public void removeEpic(int managerId) {
        for (Subtask subtask : subtasksMap.values()) {
            if (subtask.getEpicId() == managerId) {
                subtasksMap.remove(subtask.getEpicId());
            }
        }
    }

    public void removeSubtask(int managerId) {
        subtasksMap.remove(managerId);
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

    private Integer creatId() {
        return ++managerId;
    }
}
