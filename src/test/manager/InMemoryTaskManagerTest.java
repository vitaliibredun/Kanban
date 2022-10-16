package manager;

import constants.Status;
import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUp() {
        super.initialization();
        taskManager = new InMemoryTaskManager();
    }

    @AfterEach
    void cleanUp() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        taskManager.deleteAllSubtasks();
    }

    @Test
    @DisplayName("Проверка добавления task")
    void addTaskTest() {
        taskManager.addTask(task1);
        List<Task> allTasks = taskManager.getAllTasks();
        Task savedTask = taskManager.getTask(0);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedTask, "Задача не найдена"),
                () -> Assertions.assertNotNull(allTasks, "Список задач пустой."),
                () -> Assertions.assertEquals(1, allTasks.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(task1, savedTask, "Задачи не совпадают"),
                () -> Assertions.assertEquals(task1, allTasks.get(0), "Задачи не совпадают")
        );
    }

    @Test
    @DisplayName("Проверка добавления epic")
    void addEpicTest() {
        taskManager.addEpic(epic1);
        List<Epic> allEpics = taskManager.getALLEpics();
        Epic savedEpic = taskManager.getEpic(0);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedEpic, "Задача не найдена"),
                () -> Assertions.assertNotNull(allEpics, "Список задач пустой."),
                () -> Assertions.assertEquals(1, allEpics.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(epic1, savedEpic, "Задачи не совпадают"),
                () -> Assertions.assertEquals(epic1, allEpics.get(0), "Задачи не совпадают")
        );
    }

    @Test
    @DisplayName("Проверка добавления subtask")
    void addSubtaskTest() {
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subTask1);
        List<SubTask> allSubtasks = taskManager.getALLSubtasks();
        SubTask savedSubtask = taskManager.getSubtask(1);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedSubtask, "Задача не найдена"),
                () -> Assertions.assertNotNull(allSubtasks, "Список задач пустой."),
                () -> Assertions.assertEquals(1, allSubtasks.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(subTask1, savedSubtask, "Задачи не совпадают"),
                () -> Assertions.assertEquals(subTask1, allSubtasks.get(0), "Задачи не совпадают")
        );
    }

    @Test
    @DisplayName("Проверка запроса всех task")
    void getAllTasksTest() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        List<Task> allTasks = taskManager.getAllTasks();
        Task savedTask1 = taskManager.getTask(0);
        Task savedTask2 = taskManager.getTask(1);
        Task savedTask3 = taskManager.getTask(2);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedTask1, "Задача не найдена"),
                () -> Assertions.assertNotNull(savedTask2, "Задача не найдена"),
                () -> Assertions.assertNotNull(savedTask3, "Задача не найдена"),
                () -> Assertions.assertNotNull(allTasks, "Список задач пустой."),
                () -> Assertions.assertEquals(3, allTasks.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(task1, savedTask1, "Задачи не совпадают"),
                () -> Assertions.assertEquals(task2, savedTask2, "Задачи не совпадают"),
                () -> Assertions.assertEquals(task3, savedTask3, "Задачи не совпадают"),
                () -> Assertions.assertEquals(task1, allTasks.get(0), "Задачи не совпадают"),
                () -> Assertions.assertEquals(task2, allTasks.get(1), "Задачи не совпадают"),
                () -> Assertions.assertEquals(task3, allTasks.get(2), "Задачи не совпадают")
        );
    }

    @Test
    @DisplayName("Проверка запроса всех epic")
    void getAllEpicsTest() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);
        List<Epic> allEpics = taskManager.getALLEpics();
        Epic savedEpic1 = taskManager.getEpic(0);
        Epic savedEpic2 = taskManager.getEpic(1);
        Epic savedEpic3 = taskManager.getEpic(2);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedEpic1, "Задача не найдена"),
                () -> Assertions.assertNotNull(savedEpic2, "Задача не найдена"),
                () -> Assertions.assertNotNull(savedEpic3, "Задача не найдена"),
                () -> Assertions.assertNotNull(allEpics, "Список задач пустой."),
                () -> Assertions.assertEquals(3, allEpics.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(epic1, savedEpic1, "Задачи не совпадают"),
                () -> Assertions.assertEquals(epic2, savedEpic2, "Задачи не совпадают"),
                () -> Assertions.assertEquals(epic3, savedEpic3, "Задачи не совпадают"),
                () -> Assertions.assertEquals(epic1, allEpics.get(0), "Задачи не совпадают"),
                () -> Assertions.assertEquals(epic2, allEpics.get(1), "Задачи не совпадают"),
                () -> Assertions.assertEquals(epic3, allEpics.get(2), "Задачи не совпадают")
        );
    }

    @Test
    @DisplayName("Проверка запроса всех subtask")
    void getAllSubtasksTest() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);
        taskManager.addSubtask(subTask1);
        taskManager.addSubtask(subTask2);
        taskManager.addSubtask(subTask3);
        List<SubTask> allSubtasks = taskManager.getALLSubtasks();
        SubTask savedSubtask1 = taskManager.getSubtask(3);
        SubTask savedSubtask2 = taskManager.getSubtask(4);
        SubTask savedSubtask3 = taskManager.getSubtask(5);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedSubtask1, "Задача не найдена"),
                () -> Assertions.assertNotNull(savedSubtask2, "Задача не найдена"),
                () -> Assertions.assertNotNull(savedSubtask3, "Задача не найдена"),
                () -> Assertions.assertNotNull(allSubtasks, "Список задач пустой."),
                () -> Assertions.assertEquals(3, allSubtasks.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(subTask1, savedSubtask1, "Задачи не совпадают"),
                () -> Assertions.assertEquals(subTask2, savedSubtask2, "Задачи не совпадают"),
                () -> Assertions.assertEquals(subTask3, savedSubtask3, "Задачи не совпадают"),
                () -> Assertions.assertEquals(subTask1, allSubtasks.get(0), "Задачи не совпадают"),
                () -> Assertions.assertEquals(subTask2, allSubtasks.get(1), "Задачи не совпадают"),
                () -> Assertions.assertEquals(subTask3, allSubtasks.get(2), "Задачи не совпадают")
        );
    }

    @Test
    @DisplayName("Проверка запроса task")
    void getTaskTest() {
        taskManager.addTask(task1);
        List<Task> allTasks = taskManager.getAllTasks();
        Task savedTask = taskManager.getTask(0);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedTask, "Задача не найдена"),
                () -> Assertions.assertNotNull(allTasks, "Список задач пустой."),
                () -> Assertions.assertEquals(1, allTasks.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(task1, savedTask, "Задачи не совпадают"),
                () -> Assertions.assertEquals(task1, allTasks.get(0), "Задачи не совпадают")
        );
    }

    @Test
    @DisplayName("Проверка запроса epic")
    void getEpicTest() {
        taskManager.addEpic(epic1);
        List<Epic> allEpics = taskManager.getALLEpics();
        Epic savedEpic = taskManager.getEpic(0);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedEpic, "Задача не найдена"),
                () -> Assertions.assertNotNull(allEpics, "Список задач пустой."),
                () -> Assertions.assertEquals(1, allEpics.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(epic1, savedEpic, "Задачи не совпадают"),
                () -> Assertions.assertEquals(epic1, allEpics.get(0), "Задачи не совпадают")
        );
    }

    @Test
    @DisplayName("Проверка запроса subtask")
    void getSubtaskTest() {
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subTask1);
        List<SubTask> allSubtasks = taskManager.getALLSubtasks();
        SubTask savedSubtask = taskManager.getSubtask(1);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedSubtask, "Задача не найдена"),
                () -> Assertions.assertNotNull(allSubtasks, "Список задач пустой."),
                () -> Assertions.assertEquals(1, allSubtasks.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(subTask1, savedSubtask, "Задачи не совпадают"),
                () -> Assertions.assertEquals(subTask1, allSubtasks.get(0), "Задачи не совпадают")
        );
    }

    @Test
    @DisplayName("Проверка удаления всех task")
    void deleteAllTasksTest() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        List<Task> allTasks = taskManager.getAllTasks();

        Assertions.assertAll(
                () -> Assertions.assertNotNull(allTasks, "Список задач пустой."),
                () -> Assertions.assertEquals(3, allTasks.size(), "Неверное количество задач.")
        );

        allTasks.clear();

        Assertions.assertEquals(0, allTasks.size(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("Проверка удаления всех epic")
    void deleteAllEpicsTest() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);
        List<Epic> allEpics = taskManager.getALLEpics();

        Assertions.assertAll(
                () -> Assertions.assertNotNull(allEpics, "Список задач пустой."),
                () -> Assertions.assertEquals(3, allEpics.size(), "Неверное количество задач.")
        );

        allEpics.clear();

        Assertions.assertEquals(0, allEpics.size(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("Проверка удаления всех subtask")
    void deleteAllSubtasksTest() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);
        taskManager.addSubtask(subTask1);
        taskManager.addSubtask(subTask2);
        taskManager.addSubtask(subTask3);
        List<SubTask> allSubtasks = taskManager.getALLSubtasks();

        Assertions.assertAll(
                () -> Assertions.assertNotNull(allSubtasks, "Список задач пустой."),
                () -> Assertions.assertEquals(3, allSubtasks.size(), "Неверное количество задач.")
        );

        allSubtasks.clear();

        Assertions.assertEquals(0, allSubtasks.size(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("Проверка запроса списка задач отсортированных по времени")
    void getPrioritizedTasksTest() {
        Task taskTimePoint1 = new Task("task 1", "some description for task", Status.NEW, 30, "10/00/22/09/2022");
        Task taskTimePoint2 = new Task("task 2", "some description for task", Status.NEW, 30, "19/05/19/09/2022");
        Task taskTimePoint3 = new Task("task 3", "some description for task", Status.NEW, 30, "14/05/20/09/2022");
        Task taskTimePoint4 = new Task("task 4", "some description for task", Status.NEW, 30, "11/00/29/09/2022");
        taskManager.addTask(taskTimePoint1);
        taskManager.addTask(taskTimePoint2);
        taskManager.addTask(taskTimePoint3);
        taskManager.addTask(taskTimePoint4);

        Epic epicTimePoint = new Epic("epic 1", "some description for epic");
        taskManager.addEpic(epicTimePoint);

        SubTask subtaskTimePoint = new SubTask("subtask 1", "some description", Status.NEW, 15, "11/00/12/09/2022", epicTimePoint.getId());
        taskManager.addSubtask(subtaskTimePoint);

        List tasks = taskManager.getPrioritizedTasks();

        Assertions.assertAll(
                () -> Assertions.assertEquals(6, taskManager.getPrioritizedTasks().size(), "Неверное количество задач"),
                () -> Assertions.assertEquals(subtaskTimePoint, tasks.get(0), "Не верный порядок задачи"),
                () -> Assertions.assertEquals(taskTimePoint2, tasks.get(1), "Не верный порядок задачи"),
                () -> Assertions.assertEquals(epicTimePoint, tasks.get(5), "Не верный порядок задачи")
        );
    }

    @Test
    @DisplayName("Проверка удаления task")
    void removeTaskTest() {
        taskManager.addTask(task1);
        List<Task> allTasks = taskManager.getAllTasks();
        Task savedTask = taskManager.getTask(task1.getId());

        Assertions.assertAll(
                () -> Assertions.assertNotNull(allTasks, "Список задач пустой."),
                () -> Assertions.assertNotNull(savedTask, "Список задач пустой."),
                () -> Assertions.assertEquals(1, allTasks.size(), "Неверное количество задач.")
        );

        allTasks.remove(task1);

        Assertions.assertEquals(0, allTasks.size(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("Проверка удаления epic")
    void removeEpicTest() {
        taskManager.addEpic(epic1);
        List<Epic> allEpics = taskManager.getALLEpics();
        Epic savedEpic = taskManager.getEpic(epic1.getId());

        Assertions.assertAll(
                () -> Assertions.assertNotNull(allEpics, "Список задач пустой."),
                () -> Assertions.assertNotNull(savedEpic, "Список задач пустой."),
                () -> Assertions.assertEquals(1, allEpics.size(), "Неверное количество задач.")
        );

        allEpics.remove(epic1);

        Assertions.assertEquals(0, allEpics.size(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("Проверка удаления subtask")
    void removeSubtaskTest() {
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subTask1);
        List<SubTask> allSubtasks = taskManager.getALLSubtasks();
        SubTask savedSubtask = taskManager.getSubtask(subTask1.getId());

        Assertions.assertAll(
                () -> Assertions.assertNotNull(allSubtasks, "Список задач пустой."),
                () -> Assertions.assertNotNull(savedSubtask, "Список задач пустой."),
                () -> Assertions.assertEquals(1, allSubtasks.size(), "Неверное количество задач.")
        );

        allSubtasks.remove(subTask1);

        Assertions.assertEquals(0, allSubtasks.size(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("Проверка обновления task")
    void updateTaskTest() {
        taskManager.addTask(task1);
        List<Task> allTasks = taskManager.getAllTasks();
        Task savedFirstTask = taskManager.getTask(0);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedFirstTask, "Задача не найдена"),
                () -> Assertions.assertNotNull(allTasks, "Список задач пустой."),
                () -> Assertions.assertEquals(1, allTasks.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(task1, savedFirstTask, "Задачи не совпадают"),
                () -> Assertions.assertEquals(task1, allTasks.get(0), "Задачи не совпадают")
        );

        Task newTask = new Task(0, "new task", "with new description", Status.DONE);
        taskManager.updateTask(newTask);
        Task newSavedTask = taskManager.getTask(0);
        List<Task> newAllTasks = taskManager.getAllTasks();

        Assertions.assertAll(
                () -> Assertions.assertNotEquals(savedFirstTask, newSavedTask, "Задачи одинаковы"),
                () -> Assertions.assertEquals(allTasks.size(), newAllTasks.size(), "Неверное количество задач.")
        );
    }

    @Test
    @DisplayName("Проверка обновления epic")
    void updateEpicTest() {
        taskManager.addEpic(epic1);
        List<Epic> allEpics = taskManager.getALLEpics();
        Epic savedFirstEpic = taskManager.getEpic(0);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedFirstEpic, "Задача не найдена"),
                () -> Assertions.assertNotNull(allEpics, "Список задач пустой."),
                () -> Assertions.assertEquals(1, allEpics.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(epic1, savedFirstEpic, "Задачи не совпадают"),
                () -> Assertions.assertEquals(epic1, allEpics.get(0), "Задачи не совпадают")
        );

        Epic newEpic = new Epic(0, "new epic", "with new description", Status.DONE);
        taskManager.updateEpic(newEpic);
        Epic newSavedEpic = taskManager.getEpic(0);
        List<Epic> newAllEpics = taskManager.getALLEpics();

        Assertions.assertAll(
                () -> Assertions.assertNotEquals(savedFirstEpic, newSavedEpic, "Задачи одинаковы"),
                () -> Assertions.assertEquals(allEpics.size(), newAllEpics.size(), "Неверное количество задач.")
        );
    }

    @Test
    @DisplayName("Проверка обновления subtask")
    void updateSubtaskTest() {
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subTask1);
        List<SubTask> allSubtasks = taskManager.getALLSubtasks();
        SubTask savedFirstSubtask = taskManager.getSubtask(1);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedFirstSubtask, "Задача не найдена"),
                () -> Assertions.assertNotNull(allSubtasks, "Список задач пустой."),
                () -> Assertions.assertEquals(1, allSubtasks.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(subTask1, savedFirstSubtask, "Задачи не совпадают"),
                () -> Assertions.assertEquals(subTask1, allSubtasks.get(0), "Задачи не совпадают")
        );

        SubTask newSubtask = new SubTask(1, "new subtask", "with new description", Status.DONE, epic1.getId());
        taskManager.updateSubtask(newSubtask);
        SubTask newSavedSubtask = taskManager.getSubtask(1);
        List<SubTask> newAllSubtasks = taskManager.getALLSubtasks();

        Assertions.assertAll(
                () -> Assertions.assertNotEquals(savedFirstSubtask, newSavedSubtask, "Задачи одинаковы"),
                () -> Assertions.assertEquals(allSubtasks.size(), newAllSubtasks.size(), "Неверное количество задач.")
        );
    }

    @Test
    @DisplayName("Проверка запроса всех subtasks по epic")
    void getSubtasksByEpicTest() {
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subTask1);
        taskManager.addSubtask(subTask4);
        List<SubTask> allSubtasks = taskManager.getALLSubtasks();

        Assertions.assertAll(
                () -> Assertions.assertNotNull(allSubtasks, "Список задач пустой."),
                () -> Assertions.assertEquals(2, allSubtasks.size(), "Неверное количество задач.")
        );

        List<SubTask> subtasksByEpic = taskManager.getSubtasksByEpic(epic1.getId());

        Assertions.assertAll(
                () -> Assertions.assertNotNull(subtasksByEpic, "Список задач пустой."),
                () -> Assertions.assertEquals(2, subtasksByEpic.size(), "Неверное количество задач.")
        );
    }

    @Test
    @DisplayName("Проверка обновления статуса epic")
    void updateEpicStatusTest() {
        taskManager.addEpic(epic1);
        Status statusOfEpic = epic1.getStatus();
        List<SubTask> subtasksByEpic = taskManager.getSubtasksByEpic(epic1.getId());

        Assertions.assertAll(
                () -> Assertions.assertEquals(0, subtasksByEpic.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(Status.NEW, statusOfEpic, "У epic другой статус")
        );

        taskManager.addSubtask(subTask1);
        taskManager.addSubtask(subTask4);
        Status statusOfEpic1Example = epic1.getStatus();
        Status statusOfSubtaskNew = taskManager.getSubtask(1).getStatus();
        Status statusOfSubtaskDone = taskManager.getSubtask(2).getStatus();
        List<SubTask> subtasksByEpic1Example = taskManager.getSubtasksByEpic(epic1.getId());

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, subtasksByEpic1Example.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(Status.NEW, statusOfSubtaskNew, "У subtask другой статус."),
                () -> Assertions.assertEquals(Status.DONE, statusOfSubtaskDone, "У subtask другой статус."),
                () -> Assertions.assertEquals(Status.NEW, statusOfEpic1Example, "У epic другой статус")
        );

        SubTask newSubTask2Example = new SubTask(2, "new subtask", "with new description", Status.NEW, epic1.getId());
        taskManager.updateSubtask(newSubTask2Example);
        Status statusOfSubtask2Example = taskManager.getSubtask(2).getStatus();
        Status statusOfEpic2Example = epic1.getStatus();
        List<SubTask> subtasksByEpic2Example = taskManager.getSubtasksByEpic(epic1.getId());

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, subtasksByEpic2Example.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(Status.NEW, statusOfSubtaskNew, "У subtask другой статус."),
                () -> Assertions.assertEquals(Status.NEW, statusOfSubtask2Example, "У subtask другой статус."),
                () -> Assertions.assertEquals(Status.NEW, statusOfEpic2Example, "У epic другой статус")
        );

        SubTask newSubTask3Example = new SubTask(2, "new subtask", "with new description", Status.IN_PROGRESS, epic1.getId());
        taskManager.updateSubtask(newSubTask3Example);
        Status statusOfSubtask3Example = taskManager.getSubtask(2).getStatus();
        Status statusOfEpic3Example = epic1.getStatus();
        List<SubTask> subtasksByEpic3Example = taskManager.getSubtasksByEpic(epic1.getId());

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, subtasksByEpic3Example.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(Status.NEW, statusOfSubtaskNew, "У subtask другой статус."),
                () -> Assertions.assertEquals(Status.IN_PROGRESS, statusOfSubtask3Example, "У subtask другой статус."),
                () -> Assertions.assertEquals(Status.NEW, statusOfEpic3Example, "У epic другой статус")
        );

        SubTask newSubTask4Example = new SubTask(1, "new subtask", "with new description", Status.IN_PROGRESS, epic1.getId());
        taskManager.updateSubtask(newSubTask4Example);
        Status statusOfSubtask4Example = taskManager.getSubtask(1).getStatus();
        Status statusOfEpic4Example = epic1.getStatus();
        List<SubTask> subtasksByEpic4Example = taskManager.getSubtasksByEpic(epic1.getId());

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, subtasksByEpic4Example.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(Status.IN_PROGRESS, statusOfSubtask3Example, "У subtask другой статус."),
                () -> Assertions.assertEquals(Status.IN_PROGRESS, statusOfSubtask4Example, "У subtask другой статус."),
                () -> Assertions.assertEquals(Status.IN_PROGRESS, statusOfEpic4Example, "У epic другой статус")
        );

        SubTask newSubTask5Example = new SubTask(1, "new subtask", "with new description", Status.DONE, epic1.getId());
        taskManager.updateSubtask(newSubTask5Example);
        Status statusOfSubtask5Example = taskManager.getSubtask(1).getStatus();
        Status statusOfEpic5Example = epic1.getStatus();
        List<SubTask> subtasksByEpic5Example = taskManager.getSubtasksByEpic(epic1.getId());

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, subtasksByEpic5Example.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(Status.DONE, statusOfSubtaskDone, "У subtask другой статус."),
                () -> Assertions.assertEquals(Status.DONE, statusOfSubtask5Example, "У subtask другой статус."),
                () -> Assertions.assertEquals(Status.DONE, statusOfEpic5Example, "У epic другой статус")
        );

        SubTask newSubTask6Example = new SubTask(1, "new subtask", "with new description", Status.IN_PROGRESS, epic1.getId());
        taskManager.updateSubtask(newSubTask6Example);
        Status statusOfSubtask6Example = taskManager.getSubtask(1).getStatus();
        Status statusOfEpic6Example = epic1.getStatus();
        List<SubTask> subtasksByEpic6Example = taskManager.getSubtasksByEpic(epic1.getId());

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, subtasksByEpic6Example.size(), "Неверное количество задач."),
                () -> Assertions.assertEquals(Status.DONE, statusOfSubtaskDone, "У subtask другой статус."),
                () -> Assertions.assertEquals(Status.IN_PROGRESS, statusOfSubtask6Example, "У subtask другой статус."),
                () -> Assertions.assertEquals(Status.IN_PROGRESS, statusOfEpic6Example, "У epic другой статус")
        );
    }

    @Test
    @DisplayName("Проверка обновления полей duration и startTime у epic")
    void updateEpicDurationAndStartTimeTest() {
        Duration expectedDuration = Duration.ofMinutes(15);
        LocalDateTime expectedStartTime = LocalDateTime.of(2022, 9, 12, 11, 0);


        Epic epicTimePoint = new Epic("epic 1", "some description for epic");
        taskManager.addEpic(epicTimePoint);

        Duration epicDurationBeforeAddSubtask = epicTimePoint.getDuration();
        LocalDateTime epicStartTimeBeforeAddSubtask = epicTimePoint.getStartTime();

        Assertions.assertAll(
                () -> Assertions.assertNull(epicDurationBeforeAddSubtask, "Не верное значение duration"),
                () -> Assertions.assertNull(epicStartTimeBeforeAddSubtask, "Не верное значение duration")
        );

        SubTask subtaskTimePoint = new SubTask("subtask 1", "some description", Status.NEW, 15, "11/00/12/09/2022", epicTimePoint.getId());
        taskManager.addSubtask(subtaskTimePoint);

        Duration epicDurationAfterAddSubtask = epicTimePoint.getDuration();
        LocalDateTime epicStartTimeAfterAddSubtask = epicTimePoint.getStartTime();

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedDuration,epicDurationAfterAddSubtask, "Не верное значение duration"),
                () -> Assertions.assertEquals(expectedStartTime,epicStartTimeAfterAddSubtask, "Не верное значение startTime")
        );
    }

    @Test
    @DisplayName("Проверка пересечения задач по времени")
    void checkCrossDateAndTimeTasksTest() {
        taskManager.addTask(taskTimePoint1);
        Task taskSameTimePoint = new Task("task with same time point", "some description for task", Status.NEW, 30, "10/00/22/09/2022");
        taskManager.addTask(taskSameTimePoint);
        LocalDateTime startTime1 = taskTimePoint1.getStartTime();
        LocalDateTime startTime2 = taskSameTimePoint.getStartTime();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1,taskManager.getPrioritizedTasks().size(), "Добавлена задача пересекающаяся по времени"),
                () -> Assertions.assertEquals(startTime1,startTime2, "Задачи не равны")
        );
    }
}