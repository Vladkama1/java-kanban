package service;

import exception.ManagerSaveException;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static final String FIRST_LINE = "id,type,name,status,description,epic";
    private File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    private void addTask(Integer Id) {
        if (tasks.containsKey(Id)) {
            historyManager.addTask(tasks.get(Id));
        }
        if (epics.containsKey(Id)) {
            historyManager.addTask(epics.get(Id));
        }
        if (subtasks.containsKey(Id)) {
            historyManager.addTask(subtasks.get(Id));
        }
    }

    protected void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            if (!Files.exists(Paths.get(file.getParent(), file.getName()))) {
                Files.createFile(Paths.get(file.getParent(), file.getName()));
            }
            writer.write(FIRST_LINE);
            for (Task value : tasks.values()) {
                writer.append(value.toString());
            }
            for (Epic value : epics.values()) {
                writer.append(value.toString());
            }
            for (Subtask value : subtasks.values()) {
                writer.append(value.toString());
            }
            if (!HistoryStaticManager.historyToString(historyManager).isEmpty()) {
                writer.append("\n\n" + HistoryStaticManager.historyToString(historyManager));
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка, файл не считан: " + file.getName(), exception);
        }
    }

    private void putTask(String line) {
        String[] word = line.split(",");
        int id = Integer.parseInt(word[0]);
        if (id > globalId) {
            globalId = id;
        }
        Type type = Type.valueOf(word[1]);
        if (type == Type.TASK) {
            Task task = HistoryStaticManager.fromString(line);
            tasks.put(task.getId(), task);
        } else if (type == Type.EPIC) {
            Epic epic = (Epic) HistoryStaticManager.fromString(line);
            epics.put(epic.getId(), epic);
        } else {
            Subtask subtask = (Subtask) HistoryStaticManager.fromString(line);
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            ArrayList<Integer> subtasksId = epic.getSubtasksId();
            subtasksId.add(subtask.getId());
            updateEpicStatus(epic.getId());
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try {
            String historyFile = Files.readString(file.toPath());
            String[] historyLine = historyFile.split("\n");
            for (int i = 1; i < historyLine.length; i++) {
                if (historyLine[i].isEmpty()) {
                    if (!historyLine[++i].isEmpty()) {
                        List<Integer> listId = HistoryStaticManager.historyFromString(historyLine[i]);
                        for (Integer integer : listId) {
                            fileBackedTasksManager.addTask(integer);
                        }
                    }
                    break;
                } else {
                    fileBackedTasksManager.putTask(historyLine[i]);
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка, файл не считан: " + file.getName(), exception);
        }
        return fileBackedTasksManager;
    }

    public static void main(String[] args) {
        File file1 = new File("./src/resources/history.csv");
        TaskManager taskManager = FileBackedTasksManager.loadFromFile(file1);
        Task task1 = new Task("task1", "task1 from taskManager1");
        taskManager.saveTask(task1);
        Task task2 = new Task("task2", "task2 from taskManager1");
        taskManager.saveTask(task2);
        Epic epic1 = new Epic("Epic11", "epic1 from taskManager1");
        taskManager.saveEpic(epic1);
        Subtask subtask1 = new Subtask("Sub111", "subtask1 from taskManager1", epic1.getId());
        taskManager.saveSubtask(subtask1);
        Subtask subtask2 = new Subtask("Sub222", "subtask2 from taskManager1", epic1.getId());
        taskManager.saveSubtask(subtask2);
        Epic epic2 = new Epic("Epic22", "epic2 from taskManager1");
        taskManager.saveEpic(epic2);
        Subtask subtask3 = new Subtask("Sub333", "subtask3 from taskManager1", epic2.getId());
        taskManager.saveSubtask(subtask3);
        subtask3.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask3);
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getEpic(epic1.getId());
        taskManager.getSubtask(subtask1.getId());
        taskManager.getSubtask(subtask2.getId());
        taskManager.getEpic(epic2.getId());
        taskManager.getSubtask(subtask3.getId());
        TaskManager taskManager2 = FileBackedTasksManager.loadFromFile(file1);
        Task task3 = new Task("task1", "task3 from taskManager2");
        taskManager2.saveTask(task3);
        Task task4 = new Task("task2", "task4 from taskManager2");
        taskManager2.saveTask(task4);
        taskManager2.getTask(task3.getId());
        taskManager2.getTask(task4.getId());
        taskManager2.removeEpic(epic1.getId());
    }

    @Override
    public Task getTask(int globalId) {
        if (tasks.containsKey(globalId)) {
            historyManager.addTask(tasks.get(globalId));
            save();
        }
        return tasks.get(globalId);
    }

    @Override
    public Epic getEpic(int globalId) {
        if (epics.containsKey(globalId)) {
            historyManager.addTask(epics.get(globalId));
            save();
        }
        return epics.get(globalId);
    }

    @Override
    public Subtask getSubtask(int globalId) {
        if (subtasks.containsKey(globalId)) {
            historyManager.addTask(subtasks.get(globalId));
            save();
        }
        return subtasks.get(globalId);
    }

    @Override
    public ArrayList<Subtask> getAllSubtaskByEpic(int globalId) {
        ArrayList<Subtask> allSubtasksId = new ArrayList<>();
        if (epics.containsKey(globalId)) {
            ArrayList<Integer> subtasksId = epics.get(globalId).getSubtasksId();
            for (Integer id : subtasksId) {
                allSubtasksId.add(subtasks.get(id));
                save();
            }
        }
        return allSubtasksId;
    }

    @Override
    public void saveTask(Task task) {
        super.saveTask(task);
        save();
    }

    @Override
    public void saveEpic(Epic epic) {
        super.saveEpic(epic);
        save();
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        super.saveSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void removeTask(int globalId) {
        super.removeTask(globalId);
        save();
    }

    @Override
    public void removeEpic(int globalId) {
        super.removeEpic(globalId);
        save();
    }

    @Override
    public void removeSubtask(Integer globalId) {
        super.removeSubtask(globalId);
        save();
    }
}
