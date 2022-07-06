package service;

import constants.Status;
import task.Epic;
import task.SubTask;
import task.Task;
import java.util.*;

public class TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subtasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();

    private int generator = 0;

    public void addTask(Task task) {
        int taskId = generator++;
        task.setId(taskId);
        tasks.put(taskId, task);
    }

    public void addEpic(Epic epic) {
        int epicId = generator++;
        epic.setId(epicId);
        epics.put(epicId, epic);
        updateEpicStatus(epic);
    }

    public void addSubtask(SubTask subTask) {
        int subtaskId = generator++;
        int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        subTask.setEpicId(subtaskId);
        subtasks.put(subtaskId, subTask);
        epic.addSubtask(subTask.getId());
    }

    public List<Task> getTasks() {
        Collection<Task> valuesOfTasks = tasks.values();
        return new ArrayList<>(valuesOfTasks);
    }

    public List<Epic> getEpics() {
        Collection<Epic> valuesOfEpics = epics.values();
        return new ArrayList<>(valuesOfEpics);
    }

    public List<SubTask> getSubtasks() {
        Collection<SubTask> valuesOfSubtasks = subtasks.values();
        return new ArrayList<>(valuesOfSubtasks);
    }

    public Task getTask(int taskId) {
        return tasks.get(taskId);
    }

    public Epic getEpic(int epicId) {
        return epics.get(epicId);
    }

    public SubTask getSubtask(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    public void clearAllTasks() {
        tasks.clear();
    }

    public void clearAllEpics() {
        epics.clear();
    }

    public void clearAllSubtasks() {
        subtasks.clear();
    }

    public void removeTask(int taskId) {
        tasks.remove(taskId);
    }

    public void removeEpic(int epicId) {
        epics.remove(epicId);
    }

    public void removeSubtask(int subtaskId) {
        subtasks.remove(subtaskId);
    }

    public void updateTask(int taskId, Task task) {
        tasks.put(taskId, task);
    }

    public void updateEpic(int epicId, Epic epic) {
        epics.put(epicId, epic);
    }

    public void updateSubtask(int subtaskId, SubTask subTask) {
        SubTask checkSubTask = subtasks.get(subtaskId);
        if (checkSubTask == null) {
            return;
        }
        subtasks.put(subtaskId, subTask);
    }

    public List<SubTask> getSubtasksByEpic(int epicId) {
        Epic epic = epics.get(epicId);
        return (List<SubTask>) epic;
    }

    public void updateEpicStatus(Epic epic) {
        if (epics.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
        if (epics.containsValue(Status.NEW)) {
            epic.setStatus(Status.NEW);
        }
        if (epics.containsValue(Status.DONE)
                && (!epics.containsValue(Status.NEW)
                || !epics.containsValue(Status.IN_PROGRESS))) {
            epic.setStatus(Status.DONE);
        }
    }






}
