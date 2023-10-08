package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final int MAX_CONSTANT = 10;
    private final List<Task> history = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        history.add(task);
        if (history.size() > MAX_CONSTANT) {
            history.remove(0);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
