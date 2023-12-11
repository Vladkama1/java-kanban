package http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Subtask;
import model.Task;
import service.FileBackedTasksManager;
import service.Managers;

import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;
    private Gson gson = Managers.getGson();

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

    public void load() {
        List<Task> taskArrayList = gson.fromJson(kvTaskClient.load("/tasks"), new TypeToken<List<Task>>() {
        }.getType());
        List<Epic> epicArrayList = gson.fromJson(kvTaskClient.load("/epics"), new TypeToken<List<Epic>>() {
        }.getType());
        List<Subtask> subtaskArrayList = gson.fromJson(kvTaskClient.load("/subtasks"), new TypeToken<List<Subtask>>() {
        }.getType());
        List<Task> historyArrayList = gson.fromJson(kvTaskClient.load("/history"), new TypeToken<List<Task>>() {
        }.getType());
        putTasks(taskArrayList);
        putEpics(epicArrayList);
        putSubtasks(subtaskArrayList);
        putHistory(historyArrayList);
    }

    @Override
    protected void save() {
        kvTaskClient.put("/tasks", gson.toJson(getAllTasks()));
        kvTaskClient.put("/epics", gson.toJson(getAllEpic()));
        kvTaskClient.put("/subtasks", gson.toJson(getAllSubtask()));
        kvTaskClient.put("/history", gson.toJson(getHistory()));
    }

    private void putHistory(List<Task> historyArrayList) {
        for (Task task : historyArrayList) {
            if (tasks.containsKey(task.getId())) {
                historyManager.addTask(task);
            } else if (subtasks.containsKey(task.getId())) {
                historyManager.addTask(subtasks.get(task.getId()));
            } else if (epics.containsKey(task.getId())) {
                historyManager.addTask(epics.get(task.getId()));
            }
        }
    }

    private void putSubtasks(List<Subtask> subtaskArrayList) {
        for (Subtask subtask : subtaskArrayList) {
            globalId = Math.max(globalId, subtask.getId());
            subtasks.put(subtask.getId(), subtask);
            prioritet.add(subtask);
        }
    }

    private void putEpics(List<Epic> epicArrayList) {
        for (Epic epic : epicArrayList) {
            globalId = Math.max(globalId, epic.getId());
            epics.put(epic.getId(), epic);
        }
    }

    private void putTasks(List<Task> taskArrayList) {
        for (Task task : taskArrayList) {
            globalId = Math.max(globalId, task.getId());
            tasks.put(task.getId(), task);
            prioritet.add(task);
        }
    }
}
