package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {
  void addTask(Task task);
  void remove(int id);
    List<Task> getHistory();
}
