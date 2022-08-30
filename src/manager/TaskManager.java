package manager;
import constants.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TaskManager {
    Map<Integer, Task> tasks = new HashMap<>();
    Map<Integer, SubTask> subtasks = new HashMap<>();
    Map<Integer, Epic> epics = new HashMap<>();
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(SubTask subTask);

    List<Task> getAllTasks();

    List<Epic> getALLEpics();

    List<SubTask> getALLSubtasks();

    Task getTask(int taskId);

    Epic getEpic(int epicId);

    SubTask getSubtask(int subtaskId);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    void removeTask(int taskId);

    void removeEpic(int epicId);

    void removeSubtask(int subtaskId);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(SubTask subTask);

    List<SubTask> getSubtasksByEpic(int epicId);

    Status updateEpicStatus(Epic epic);
}
