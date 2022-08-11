import constants.Status;
import manager.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        // test
        testing();
    }

    private static void testing() {
        HistoryManager manager = new InMemoryHistoryManager();

        Epic epic1 = new Epic("epic 1", "some description");
        manager.add(epic1);

        SubTask subTask1 = new SubTask("subtask 1", "some description", Status.NEW, epic1.getId());
        SubTask subTask2 = new SubTask("some subtask 2", "some description", Status.NEW, epic1.getId());
        SubTask subTask3 = new SubTask("some subtask 3", "some description", Status.NEW, epic1.getId());
        manager.add(subTask1);
        manager.add(subTask2);
        manager.add(subTask3);

        Epic epic2 = new Epic("epic 2", "some description");
        manager.add(epic2);

        List<Task> history = manager.getHistory();

        System.out.println(epic1.equals(history.get(0)));
        System.out.println(subTask1.equals(history.get(1)));
        System.out.println(subTask2.equals(history.get(2)));
        System.out.println(subTask3.equals(history.get(3)));
        System.out.println(epic2.equals(history.get(4)));

        System.out.println(history.size() == 5);
    }
}