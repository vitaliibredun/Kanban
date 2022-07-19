package manager;

import constants.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager{
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subtasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private int generator = 0;


    @Override
    public void addTask(Task task) {
        int taskId = generator++;
        task.setId(taskId);
        tasks.put(taskId, task);
    }

    @Override
    public void addEpic(Epic epic) {
        int epicId = generator++;
        epic.setId(epicId);
        epics.put(epicId, epic);
        updateEpicStatus(epic);
    }

    @Override
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

    @Override
    public List<Task> getTasks() {
        Collection<Task> valuesOfTasks = tasks.values();
        return new ArrayList<>(valuesOfTasks);
    }

    @Override
    public List<Epic> getEpics() {
        Collection<Epic> valuesOfEpics = epics.values();
        return new ArrayList<>(valuesOfEpics);
    }

    @Override
    public List<SubTask> getSubtasks() {
        Collection<SubTask> valuesOfSubtasks = subtasks.values();
        return new ArrayList<>(valuesOfSubtasks);
    }

    @Override
    public Task getTask(int taskId) {
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpic(int epicId) {
        return epics.get(epicId);
    }

    @Override
    public SubTask getSubtask(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        deleteAllSubtasks();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    @Override
    public void removeTask(int taskId) {
        tasks.remove(taskId);
    }

    @Override
    public void removeEpic(int epicId) {
        epics.remove(epicId);
        List<Integer> subTasksId = epics.get(epicId).getSubTasksId();
        for (int subtasksId : subTasksId) {
            subtasks.remove(subtasksId);
        }
    }

    @Override
    public void removeSubtask(int subtaskId) {
        subtasks.remove(subtaskId);
        int epicId = subtasks.get(subtaskId).getEpicId();
        Epic epic = epics.get(epicId);
        epic.setStatus(updateEpicStatus(epic));
        epics.put(epicId, epic);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(SubTask subTask) {
        subtasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.setStatus(updateEpicStatus(epic));
        epics.put(subTask.getEpicId(), epic);
    }

    @Override
    public List<SubTask> getSubtasksByEpic(int epicId) {
        Epic epic = epics.get(epicId);
        return (List<SubTask>) epic;
    }

    @Override
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
