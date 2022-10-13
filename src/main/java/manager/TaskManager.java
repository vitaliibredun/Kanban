package manager;

import constants.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(SubTask subTask);

    List<Task> getAllTasks();

    List<Epic> getALLEpics();

    List<SubTask> getALLSubtasks();

    Task getTask(int taskId);

    Epic getEpic(int epicId);

    SubTask getSubtask(int subtaskId);

    Set<Task> getPrioritizedTasks();

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

    void updateEpicDurationAndStartTime(Epic epic);

    void checkCrossDateAndTimeTasks(Task task);
}
