import constants.Status;
import manager.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        // test 1
        compareAddAndReturnTasks();

        // test 2
        compareAddAndReturnSumTasks();
    }

    private static void compareAddAndReturnTasks() {
        HistoryManager manager = new InMemoryHistoryManager();

        Task task = new Task("some task name", "some task description", Status.NEW);
        manager.add(task);

        Epic epic = new Epic("some epic name", "some epic description");
        manager.add(epic);

        SubTask subTask = new SubTask("some subtask name", "some subtask description", Status.NEW, epic.getId());
        manager.add(subTask);

        List<Task> history = manager.getHistory();

        System.out.println(task.equals(history.get(0)));
        System.out.println(epic.equals(history.get(1)));
        System.out.println(subTask.equals(history.get(2)));
    }

    private static void compareAddAndReturnSumTasks() {
        HistoryManager manager = new InMemoryHistoryManager();

        Task task = new Task("some task name", "some task description", Status.NEW);
        manager.add(task);

        Epic epic = new Epic("some epic name", "some epic description");
        manager.add(epic);

        SubTask subTask = new SubTask("some subtask name", "some subtask description", Status.NEW, epic.getId());
        manager.add(subTask);

        List<Task> history = manager.getHistory();

        System.out.println(history.size() == 3);
    }
}