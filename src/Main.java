import constants.Status;
import manager.*;
import tasks.Epic;
import tasks.SubTask;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        // test
        doTest();
    }

    private static void doTest() {
        TaskManager taskManager = new InMemoryTaskManager();
        HistoryManager historyManager = new InMemoryHistoryManager();

        Epic epic1 = new Epic("epic 1", "some description");
        taskManager.addEpic(epic1);

        SubTask subTask1 = new SubTask("subtask 1", "some description", Status.NEW, epic1.getId());
        SubTask subTask2 = new SubTask("some subtask 2", "some description", Status.NEW, epic1.getId());
        SubTask subTask3 = new SubTask("some subtask 3", "some description", Status.NEW, epic1.getId());
        taskManager.addSubtask(subTask1);
        taskManager.addSubtask(subTask2);
        taskManager.addSubtask(subTask3);

        Epic epic2 = new Epic("epic 2", "some description");
        taskManager.addEpic(epic2);

        historyManager.add(epic2);
        historyManager.add(epic1);
        historyManager.add(epic2);
        historyManager.add(subTask1);
        historyManager.add(epic2);
        historyManager.add(subTask1);
        historyManager.add(subTask1);
        historyManager.add(subTask2);

        List<Object> history = historyManager.getHistory();

        System.out.println(history.size() == 4);

        System.out.println(history);
    }
}