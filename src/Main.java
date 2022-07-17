import constants.Status;
import service.TaskManager;
import task.Epic;
import task.SubTask;
import task.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task = new Task("some task name", "some task description", Status.NEW);
        taskManager.addTask(task);

        Epic epic = new Epic("some epic name", "some epic description");
        taskManager.addEpic(epic);

        SubTask subTask = new SubTask("some subtask name", "some subtask description", Status.NEW, epic.getId());
        taskManager.addSubtask(subTask);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
    }
}
