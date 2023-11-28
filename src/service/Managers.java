package service;

import java.io.File;

public class Managers {
    private Managers() {
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return FileBackedTasksManager.loadFromFile(new File("./src/resources/history.csv"));
//        return new InMemoryTaskManager();
    }
}
