package service;

import model.Task;

import java.util.HashMap;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> history = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(getTasks());
    }

    @Override
    public void addTask(Task task) {
        if (task != null) {
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        removeNode(history.remove(id));
    }

    private ArrayList<Task> getTasks() {
        Node start = first;
        ArrayList<Task> histor = new ArrayList<>();
        while (start != null) {
            histor.add(start.task);
            start = start.next;
        }
        return histor;
    }

    private void linkLast(Task task) {
        final Node finish = last;
        final Node newNoda = new Node(finish, task, null);
        last = newNoda;
        if (finish == null) {
            first = newNoda;
        } else {
            finish.next = newNoda;
            if (history.containsKey(task.getId())) {
                remove(task.getId());
            }
        }
        history.put(task.getId(), newNoda);
    }

    private void removeNode(Node node) {
        if (node != null) {
            if (node.next == null && node.prev == null) {
                first = null;
            } else if (node.equals(first)) {
                first = node.next;
                node.next.prev = null;
            } else if (node.equals(last)) {
                last = node.prev;
                node.prev.next = null;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
        }
    }
}
