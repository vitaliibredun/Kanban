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
        subTask.setId(subtaskId);
        subtasks.put(subtaskId, subTask);
        Epic epic = epics.get(epicId);
        epic.addSubtaskId(subtaskId);
        epic.setStatus(updateEpicStatus(epic));
        epics.put(epicId, epic);
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

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        deleteAllSubtasks();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    public void removeTask(int taskId) {
        tasks.remove(taskId);
    }

    public void removeEpic(int epicId) {
        epics.remove(epicId);
        List<Integer> subTasksId = epics.get(epicId).getSubTasksId();
        for (int subtasksId : subTasksId) {
            subtasks.remove(subtasksId);
        }
    }

    public void removeSubtask(int subtaskId) {
        subtasks.remove(subtaskId);
        int epicId = subtasks.get(subtaskId).getEpicId();
        Epic epic = epics.get(epicId);
        epic.setStatus(updateEpicStatus(epic));
        epics.put(epicId, epic);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void updateSubtask(SubTask subTask) {
        subtasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.setStatus(updateEpicStatus(epic));
        epics.put(subTask.getEpicId(), epic);
    }

    public List<SubTask> getSubtasksByEpic(int epicId) {
        Epic epic = epics.get(epicId);
        return (List<SubTask>) epic;
    }

    public Status updateEpicStatus(Epic epic) {
        List<Integer> subTasksId = epic.getSubTasksId();
        if (subTasksId.isEmpty()) {
            return Status.NEW;
        }
        for (int subtaskId : subTasksId) {
            if (subtasks.get(subtaskId).getStatus() == Status.NEW) {
                return Status.NEW;
            }
        }
        for (int subtaskId : subTasksId) {
            if (subtasks.get(subtaskId).getStatus() == Status.DONE) {
                return Status.DONE;
            }

        }
        return Status.IN_PROGRESS;
    }
}