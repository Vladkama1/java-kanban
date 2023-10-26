import model.Epic;
import model.Subtask;
import model.Task;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("task1", "description1");
        taskManager.saveTask(task1);

        Task task2 = new Task("task2", "description2");
        taskManager.saveTask(task2);

        Epic epic1 = new Epic("Epic11", "description11");
        taskManager.saveEpic(epic1);

        Subtask subtask1 = new Subtask("Sub111", "description111", epic1.getId());
        taskManager.saveSubtask(subtask1);

        Epic epic2 = new Epic("Epic22", "description22");
        taskManager.saveEpic(epic2);

        Subtask subtask2 = new Subtask("Sub222", "", epic2.getId());
        taskManager.saveSubtask(subtask2);

        Subtask subtask3 = new Subtask("Sub333", "", epic2.getId());
        taskManager.saveSubtask(subtask3);

        Epic epic3 = new Epic("Epic33", "");
        taskManager.saveEpic(epic3);

        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());

        taskManager.getEpic(epic1.getId());
        taskManager.getEpic(epic2.getId());
        taskManager.getEpic(epic3.getId());

        taskManager.getSubtask(subtask1.getId());
        taskManager.getSubtask(subtask2.getId());
        taskManager.getSubtask(subtask3.getId());

        System.out.println("Сейчас размер истории - " + taskManager.getHistory().size());
        System.out.println("Начинается с Id - " + taskManager.getHistory().get(0).getId());
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
        taskManager.getEpic(epic2.getId());
        taskManager.getEpic(epic3.getId());

        taskManager.getSubtask(subtask1.getId());
        taskManager.getSubtask(subtask2.getId());

        System.out.println("Теперь размер истории - " + taskManager.getHistory().size());
        System.out.println("Начинается с Id - " + taskManager.getHistory().get(0).getId());
        taskManager.getTask(task1.getId());
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
     }
}
