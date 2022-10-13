package manager;

import constants.Status;
import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task1;
    protected Task task2;
    protected Task task3;
    protected Epic epic1;
    protected Epic epic2;
    protected Epic epic3;
    protected SubTask subTask1;
    protected SubTask subTask2;
    protected SubTask subTask3;
    protected SubTask subTask4;
    protected Task taskTimePoint1;
    protected Task taskTimePoint2;
    protected Task taskTimePoint3;
    protected Task taskTimePoint4;
    protected Epic epicTimePoint;
    protected SubTask subtaskTimePoint;

    @BeforeEach
    void initialization() {
        task1 = new Task("task 1", "some description", Status.NEW);
        task2 = new Task("task 2", "some description", Status.IN_PROGRESS);
        task3 = new Task("task 3", "some description", Status.DONE);

        epic1 = new Epic("epic 1", "some description");
        epic2 = new Epic("epic 2", "some description");
        epic3 = new Epic("epic 3", "some description");

        subTask1 = new SubTask("subtask 1", "some description for subtask", Status.NEW, epic1.getId());
        subTask2 = new SubTask("subtask 2", "some description for subtask", Status.IN_PROGRESS, epic2.getId());
        subTask3 = new SubTask("subtask 3", "some description for subtask", Status.DONE, epic3.getId());
        subTask4 = new SubTask("subtask 4", "some description for subtask", Status.DONE, epic1.getId());

        taskTimePoint1 = new Task("task 1", "some description for task", Status.NEW, 30, "10/00/22/09/2022");
        taskTimePoint2 = new Task("task 2", "some description for task", Status.NEW, 30, "19/05/19/09/2022");
        taskTimePoint3 = new Task("task 3", "some description for task", Status.NEW, 30, "14/05/20/09/2022");
        taskTimePoint4 = new Task("task 4", "some description for task", Status.NEW, 30, "11/00/29/09/2022");

        epicTimePoint = new Epic("epic 1", "some description for epic");

        subtaskTimePoint = new SubTask("subtask 1", "some description", Status.NEW, 15, "11/00/12/09/2022", epicTimePoint.getId());
    }
}