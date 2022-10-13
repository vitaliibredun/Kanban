package manager;

import constants.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private File fileTest;
    private File file;
    private TaskManager manager;

    @BeforeEach
    void setUp() throws IOException {
        super.initialization();
        fileTest = new File("src/test/testResources/taskHistoryForTest.csv");
        file = new File("src/main/resources/tasksHistory.csv");
        manager = Managers.getFileBackedTaskManager();
        new FileWriter(file,false).close();
    }


    @Test
    @DisplayName("Проверка записи задач в файл csv")
    void saveTest() throws IOException {
        String dataFromFile = Files.readString(Path.of(String.valueOf(file)));
        String dataFromFileTest = Files.readString(Path.of(String.valueOf(fileTest)));

        Assertions.assertAll(
                () -> Assertions.assertNotEquals(dataFromFile, dataFromFileTest, "Файлы равны"),
                () -> Assertions.assertNotNull(dataFromFileTest, "В файле есть данные")
        );

        Task task = new Task("task name", "some description", Status.IN_PROGRESS);
        manager.addTask(task);

        Task taskTimePoint = new Task("task name", "some description", Status.NEW,45,"08/30/12/11/2022");
        manager.addTask(taskTimePoint);

        Epic epic = new Epic("epic name", "some description");
        manager.addEpic(epic);

        Epic epicWithSubtask = new Epic("epic with subtask", "some description");
        manager.addEpic(epicWithSubtask);

        SubTask subTask = new SubTask("subtask name", "some description", Status.DONE, 25, "11/00/22/10/2022", epicWithSubtask.getId());
        manager.addSubtask(subTask);

        manager.getEpic(2);
        manager.getTask(0);
        manager.getSubtask(4);

        String newDataFromFile = Files.readString(Path.of(String.valueOf(file)));
        String newDataFromFileTest = Files.readString(Path.of(String.valueOf(fileTest)));

        Assertions.assertAll(
                () -> Assertions.assertEquals(newDataFromFile, newDataFromFileTest, "Файлы не равны"),
                () -> Assertions.assertNotNull(newDataFromFileTest, "В файле нет данных"),
                () -> Assertions.assertNotNull(newDataFromFile, "В файле есть данные")
        );
    }

    @Test
    @DisplayName("Проверка чтения задач из файла csv")
    void loadFromFileTest() {
        FileBackedTasksManager.loadFromFile(fileTest);

        int sizeTasks = manager.getAllTasks().size();
        int sizeEpics = manager.getALLEpics().size();
        int sizeSubtasks = manager.getALLSubtasks().size();

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, sizeTasks, "Не верное количество tasks"),
                () -> Assertions.assertEquals(2, sizeEpics, "Не верное количество epics"),
                () -> Assertions.assertEquals(1, sizeSubtasks, "Не верное количество subtasks")
        );
    }
}