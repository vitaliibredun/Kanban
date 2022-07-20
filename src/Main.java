import constants.Status;
import manager.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager manager = Managers.getDefault();


        Task task = new Task("some task name", "some task description", Status.NEW);
        manager.addTask(task);

        Epic epic = new Epic("some epic name", "some epic description");
        manager.addEpic(epic);

        SubTask subTask = new SubTask("some subtask name", "some subtask description", Status.NEW, epic.getId());
        manager.addSubtask(subTask);

        manager.getTask(0);
        manager.getEpic(1);
        manager.getSubtask(2);

        List<Task> history = historyManager.getHistory();

        System.out.println(history);
    }
}
