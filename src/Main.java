import model.Epic;
import model.Subtask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {//NEW ,IN_PROGRESS , DONE статус задачи
        System.out.println("Тесты!");

        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("task1", "description1");
        Task task2 = new Task("task2", "description2");

        Epic epic1 = new Epic("Epic11", "description11");
        Subtask subtask1 = new Subtask("Sub111", "description111", 3);

        Epic epic2 = new Epic("Epic22", "description22");
        Subtask subtask2 = new Subtask("Sub222", "", 5);
        Subtask subtask3 = new Subtask("Sub333", "", 5);

        Epic epic3 = new Epic("Epic33", "");

        taskManager.saveTask(task1);
        taskManager.saveTask(task2);

        taskManager.saveEpic(epic1);
        taskManager.saveSubtask(subtask1);
        taskManager.updateEpic(epic1);

        taskManager.saveEpic(epic2);
        taskManager.saveSubtask(subtask2);
        taskManager.saveSubtask(subtask3);
        taskManager.updateEpic(epic2);

        taskManager.saveEpic(epic3);
        taskManager.updateEpic(epic3);

        System.out.println("Список задач:");
        for (Subtask subtask : taskManager.getAllSubtask()) {
            System.out.println(subtask.toString() + "\n");
        }
        for (Epic epic : taskManager.getAllEpic()) {
            System.out.println(epic.toString() + "\n");
        }
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task.toString() + "\n");
        }

        System.out.println("Проверяем статус epic1 при удалении сабтаска");
        System.out.println(epic1.toString() + "\n");

        System.out.println("После удаления:");
        System.out.println(epic1.toString() + "\n");

        System.out.println("Проверяем удаление epic2(не останется ссылок на сабтаски и останется 2 epic");
        taskManager.removeEpic(5);
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubtask());

        System.out.println("/*--");
        taskManager.printTask();
        taskManager.printEpic();
        taskManager.printSubtask();
        System.out.println("/*--");
        taskManager.removeSubtask(4);
        taskManager.printTask();
        taskManager.printEpic();
        taskManager.printSubtask();
        System.out.println("/*--");
    }
}
