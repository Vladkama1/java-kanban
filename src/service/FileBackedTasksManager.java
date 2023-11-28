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
    private static final String FIRST_LINE = "id,type,name,status,description,startTime,duration,epic";
    private File file;


    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String historyLine;
            while ((historyLine = bufferedReader.readLine()) != null) {
                if (historyLine.isEmpty()) {
                    historyLine = bufferedReader.readLine();
                    if (!historyLine.isEmpty()) {
                        List<Integer> listId = HistoryStaticManager.historyFromString(historyLine);
                        for (Integer integer : listId) {
                            fileBackedTasksManager.addTask(integer);
                        }
                    }
                    break;
                } else if (!historyLine.equals(FIRST_LINE)) {
                    fileBackedTasksManager.putTask(historyLine);
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка, файл не считан: " + file.getName(), exception);
        }
        return fileBackedTasksManager;
    }

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    @Override
    public Task getTask(int globalId) {
        Task task = super.getTask(globalId);
        save();
        return task;
    }

    @Override
    public Epic getEpic(Integer globalId) {
        Epic epic = super.getEpic(globalId);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(int globalId) {
        Subtask subtask = super.getSubtask(globalId);
        save();
        return subtask;
    }

    @Override
    public ArrayList<Subtask> getAllSubtaskByEpic(int globalId) {
        ArrayList<Subtask> allSubtasksId = super.getAllSubtaskByEpic(globalId);
        save();
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

    private void putTask(String line) {
        String[] word = line.split(",");
        Integer id1 = Integer.parseInt(word[0]);
        if (id1 > globalId) {
            globalId = id1;
        }
        Type type = Type.valueOf(word[1]);
        switch (type) {
            case TASK:
                Task task = HistoryStaticManager.fromString(line);
                if (checkDateTimeTask(task)) {
                    tasks.put(task.getId(), task);
                    prioritet.add(task);
                }
                break;
            case EPIC:
                Epic epic = (Epic) HistoryStaticManager.fromString(line);
                epics.put(epic.getId(), epic);
                break;
            default:
                Subtask subtask = (Subtask) HistoryStaticManager.fromString(line);
                if (checkDateTimeTask(subtask)) {
                    subtasks.put(subtask.getId(), subtask);
                    prioritet.add(subtask);
                    Epic epic1 = epics.get(subtask.getEpicId());
                    ArrayList<Integer> subtasksId = epic1.getSubtasksId();
                    subtasksId.add(subtask.getId());
                    updateEpicStatus(epic1.getId());
                }
                break;
        }
    }
}
