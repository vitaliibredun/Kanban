package manager;

import java.io.File;

public class Managers {

    public static HistoryManager getInMemoryHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getFileBackedTaskManager() {
        return new FileBackedTasksManager(new File("resources/tasksHistory.csv"));
    }
}
