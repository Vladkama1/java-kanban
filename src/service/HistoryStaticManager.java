package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class HistoryStaticManager {
    public static Task fromString(String value) {
        String[] values = value.split(",");
        int id = Integer.parseInt(values[0]);
        Type type = Type.valueOf(values[1]);
        String name = values[2];
        Status status = Status.valueOf(values[3]);
        String description = values[4];
        if (type.equals(Type.TASK)) {
            Task task = new Task(name, description);
            task.setId(id);
            task.setStatus(status);
            return task;
        } else if (type.equals(Type.SUBTASK)) {
            Subtask subtask = new Subtask(name, description, Integer.parseInt(values[5]));
            subtask.setId(id);
            subtask.setStatus(status);
            return subtask;
        } else {
            Epic epic = new Epic(name, description);
            epic.setId(id);
            epic.setStatus(status);
            return epic;
        }
    }

    public static String historyToString(HistoryManager manager) {
        StringJoiner stringJoiner = new StringJoiner(",");
        for (Task task : manager.getHistory()) {
            stringJoiner.add(String.valueOf(task.getId()));
        }
        return stringJoiner.toString();
    }

    public static List<Integer> historyFromString(String value) {
        String[] values = value.split(",");
        ArrayList<Integer> ids = new ArrayList<>(values.length);
        for (String id : values) {
            ids.add(Integer.parseInt(id));
        }
        return ids;
    }
}
