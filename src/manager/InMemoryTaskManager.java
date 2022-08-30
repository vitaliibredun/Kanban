package manager;
import constants.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import java.util.*;

public class InMemoryTaskManager implements TaskManager{
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, SubTask> subtasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    public static HistoryManager historyManager = Managers.getInMemoryHistoryManager();
    protected int generator = 0;

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
        int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        int subtaskId = generator++;
        subTask.setId(subtaskId);
        subtasks.put(subtaskId, subTask);
        epic.addSubtaskId(subtaskId);
        epic.setStatus(updateEpicStatus(epic));
        epics.put(epicId, epic);
    }

    @Override
    public List<Task> getAllTasks() {
        Collection<Task> valuesOfTasks = tasks.values();
        return new ArrayList<>(valuesOfTasks);
    }

    @Override
    public List<Epic> getALLEpics() {
        Collection<Epic> valuesOfEpics = epics.values();
        return new ArrayList<>(valuesOfEpics);
    }

    @Override
    public List<SubTask> getALLSubtasks() {
        Collection<SubTask> valuesOfSubtasks = subtasks.values();
        return new ArrayList<>(valuesOfSubtasks);
    }

    @Override
    public Task getTask(int taskId) {
        //historyManager.add(tasks.get(taskId));
        Task task = tasks.get(taskId);
        historyManager.add(task);
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpic(int epicId) {
        historyManager.add(epics.get(epicId));
        return epics.get(epicId);
    }

    @Override
    public SubTask getSubtask(int subtaskId) {
        historyManager.add(subtasks.get(subtaskId));
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
        historyManager.remove(taskId);
    }

    @Override
    public void removeEpic(int epicId) {
        List<Integer> subTasksId = epics.remove(epicId).getSubTasksId();
        for (int subtasksId : subTasksId) {
            subtasks.remove(subtasksId);
        }
        historyManager.remove(epicId);
    }

    @Override
    public void removeSubtask(int subtaskId) {
        int epicId = subtasks.remove(subtaskId).getEpicId();
        Epic epic = epics.get(epicId);
        epic.setStatus(updateEpicStatus(epic));
        epics.put(epicId, epic);
        historyManager.remove(subtaskId);
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
