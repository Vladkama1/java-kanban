package service;

import model.Task;

import java.util.ArrayList;
import java.util.LinkedList;


public class InMemoryHistoryManager implements HistoryManager {
    private final int MAX_CONSTANT = 10;
    private final LinkedList<Task> history = new LinkedList<>();

    @Override
    public void addTask(Task task) {
        if (task != null) {
            history.addLast(task);
            if (history.size() > MAX_CONSTANT) {
                history.removeFirst();
            }
        } else {
            System.out.println("История пустая!");
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
