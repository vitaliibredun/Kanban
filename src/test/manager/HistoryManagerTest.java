package manager;

import constants.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;


public class HistoryManagerTest {
    protected InMemoryHistoryManager historyManager;
    protected InMemoryTaskManager taskManager;
    protected Task task1;
    protected Task task2;
    protected Task task3;
    protected Epic epic1;
    protected Epic epic2;
    protected Epic epic3;
    protected SubTask subTask1;
    protected SubTask subTask2;
    protected SubTask subTask3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager();

        task1 = new Task("task 1", "some description", Status.NEW);
        task2 = new Task("task 2", "some description", Status.IN_PROGRESS);
        task3 = new Task("task 3", "some description", Status.DONE);

        epic1 = new Epic("epic 1", "some description");
        epic2 = new Epic("epic 2", "some description");
        epic3 = new Epic("epic 3", "some description");

        subTask1 = new SubTask("subtask 1", "some description for subtask", Status.NEW, epic1.getId());
        subTask2 = new SubTask("subtask 2", "some description for subtask", Status.IN_PROGRESS, epic2.getId());
        subTask3 = new SubTask("subtask 3", "some description for subtask", Status.DONE, epic3.getId());
    }

    @Test
    @DisplayName("Проверка добавления любой task при условии удаления прошлой")
    void addTest() {
        List<Task> listBeforeTask = historyManager.getHistory();
        Assertions.assertEquals(0, listBeforeTask.size(), "Не верное количество задач");

        historyManager.add(task1);
        List<Task> allSavedTasks1Example = historyManager.getHistory();
        Task savedTaskFirstExample = allSavedTasks1Example.get(0);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(allSavedTasks1Example, "Список задач пустой."),
                () -> Assertions.assertEquals(1, allSavedTasks1Example.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(task1, savedTaskFirstExample, "Задачи не совпадают.")
        );

        historyManager.add(epic1);
        List<Task> allSavedTasks2Example = historyManager.getHistory();
        Task savedTask2Example = allSavedTasks2Example.get(0);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(allSavedTasks2Example, "Список задач пустой."),
                () -> Assertions.assertEquals(1, allSavedTasks2Example.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(epic1, savedTask2Example, "Задачи не совпадают.")
        );

        historyManager.add(subTask1);
        List<Task> allSavedTasks3Example = historyManager.getHistory();
        Task savedTask3Example = allSavedTasks3Example.get(0);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(allSavedTasks3Example, "Список задач пустой."),
                () -> Assertions.assertEquals(1, allSavedTasks3Example.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(subTask1, savedTask3Example, "Задачи не совпадают.")
        );
    }

    @Test
    @DisplayName("Проверка удаления любой task")
    void removeTest() {
        List<Task> listBeforeTask = historyManager.getHistory();
        Assertions.assertEquals(0, listBeforeTask.size(), "Не верное количество задач");

        historyManager.add(task1);
        List<Task> allSavedTasks1Example = historyManager.getHistory();
        Task savedTaskFirstExample = allSavedTasks1Example.get(0);
        Assertions.assertAll(
                () -> Assertions.assertNotNull(allSavedTasks1Example, "Список задач пустой."),
                () -> Assertions.assertEquals(1, allSavedTasks1Example.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(task1, savedTaskFirstExample, "Задачи не совпадают.")
        );

        historyManager.remove(task1.getId());

        List<Task> listTasksExample = historyManager.getHistory();
        Assertions.assertEquals(0, listTasksExample.size(), "Не верное количество задач");
    }

    @Test
    @DisplayName("Проверка запроса задач из customLinkedList")
    void getHistoryTest() {
        List<Task> historyBeforeTasks = InMemoryTaskManager.historyManager.getHistory();
        Assertions.assertEquals(0, historyBeforeTasks.size(), "Не верное количество задач");

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);
        taskManager.addSubtask(subTask2);
        taskManager.getTask(task2.getId());
        taskManager.getEpic(epic2.getId());

        List<Task> historyAfterTasksIn = InMemoryTaskManager.historyManager.getHistory();

        Assertions.assertEquals(2, historyAfterTasksIn.size(), "Неверное количество задач.");
    }
}