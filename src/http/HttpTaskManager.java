package http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Subtask;
import model.Task;
import service.FileBackedTasksManager;
import service.Managers;

import java.util.ArrayList;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;
    Gson gson = Managers.getGson();

    public HttpTaskManager(String url) {
        this(url, false);
    }

    public HttpTaskManager(String url, boolean isLoad) {
        super(null);
        kvTaskClient = new KVTaskClient(url);
        if (isLoad) {
            load();
        }
    }

    @Override
    protected void save() {
        kvTaskClient.put("/tasks", gson.toJson(getAllTasks()));
        kvTaskClient.put("/epics", gson.toJson(getAllEpic()));
        kvTaskClient.put("/subtasks", gson.toJson(getAllSubtask()));
        kvTaskClient.put("/history", gson.toJson(getHistory()));
    }

    public void load() {
        ArrayList<Task> taskArrayList = gson.fromJson(kvTaskClient.load("/tasks"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        ArrayList<Epic> epicArrayList = gson.fromJson(kvTaskClient.load("/epics"), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        ArrayList<Subtask> subtaskArrayList = gson.fromJson(kvTaskClient.load("/subtasks"), new TypeToken<ArrayList<Subtask>>() {
        }.getType());
        ArrayList<Task> historyArrayList = gson.fromJson(kvTaskClient.load("/history"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        putTasks(taskArrayList);
        putEpics(epicArrayList);
        putSubtasks(subtaskArrayList);
        putHistory(historyArrayList);
    }

    private void putHistory(ArrayList<Task> historyArrayList) {
        for (Task task : historyArrayList) {
            globalId = Math.max(globalId, task.getId());
            if (tasks.containsKey(globalId)) {
                historyArrayList.add(task);
            } else if (subtasks.containsKey(globalId)) {
                historyArrayList.add(task);
            } else if (epics.containsKey(globalId)) {
                historyArrayList.add(task);
            } else {
                System.out.println("ERROR");
            }
        }
    }

    private void putSubtasks(ArrayList<Subtask> subtaskArrayList) {
        for (Subtask subtask : subtaskArrayList) {
            globalId = Math.max(globalId, subtask.getId());
            subtasks.put(globalId, subtask);
            prioritet.add(subtask);
        }
    }

    private void putEpics(ArrayList<Epic> epicArrayList) {
        for (Epic epic : epicArrayList) {
            globalId = Math.max(globalId, epic.getId());
            epics.put(globalId, epic);
        }
    }

    private void putTasks(ArrayList<Task> taskArrayList) {
        for (Task task : taskArrayList) {
            globalId = Math.max(globalId, task.getId());
            tasks.put(globalId, task);
            prioritet.add(task);
        }
    }
}
