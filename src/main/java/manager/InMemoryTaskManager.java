package manager;

import constants.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager{
    protected static Map<Integer, Task> tasks = new HashMap<>();
    protected static Map<Integer, SubTask> subtasks = new HashMap<>();
    protected static Map<Integer, Epic> epics = new HashMap<>();
    protected Set<Task> prioritizedTasks = new TreeSet<>(new ComparatorToInMemoryTaskManager());
    public static HistoryManager historyManager = Managers.getInMemoryHistoryManager();
    public int generator = 0;

    @Override
    public void addTask(Task task) {
        int taskId = generator++;
        task.setId(taskId);
        tasks.put(taskId, task);
        checkCrossDateAndTimeTasks(task);
        prioritizedTasks.add(task);
    }

    @Override
    public void addEpic(Epic epic) {
        int epicId = generator++;
        epic.setId(epicId);
        epics.put(epicId, epic);
        updateEpicStatus(epic);
        checkCrossDateAndTimeTasks(epic);
        prioritizedTasks.add(epic);
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
        updateEpicDurationAndStartTime(epic);
        checkCrossDateAndTimeTasks(subTask);
        prioritizedTasks.add(subTask);
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
        Task task = tasks.get(taskId);
        historyManager.add(task);
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpic(int epicId) {
        Epic epic = epics.get(epicId);
        historyManager.add(epic);
        return epics.get(epicId);
    }

    @Override
    public SubTask getSubtask(int subtaskId) {
        SubTask subTask = subtasks.get(subtaskId);
        historyManager.add(subTask);
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
    public List getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
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
        checkCrossDateAndTimeTasks(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        checkCrossDateAndTimeTasks(epic);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(SubTask subTask) {
        checkCrossDateAndTimeTasks(subTask);
        subtasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.setStatus(updateEpicStatus(epic));
        epics.put(subTask.getEpicId(), epic);
    }

    @Override
    public List<SubTask> getSubtasksByEpic(int epicId) {
        List<SubTask> subTaskList = new ArrayList<>();

        for (SubTask subTask : subtasks.values()) {
            if (subTask.getEpicId() == epicId) {
                subTaskList.add(subTask);
            }
        }
        return subTaskList;
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

    @Override
    public void updateEpicDurationAndStartTime(Epic epic) {
        List<SubTask> subtasksByEpic = getSubtasksByEpic(epic.getId());
        for (SubTask subTask : subtasksByEpic) {
            if (subTask.getStartTime() == null) {
                return;
            }
        }
        if (!subtasksByEpic.isEmpty()) {
            LocalDateTime minStartTime = subtasksByEpic.stream()
                    .map(SubTask::getStartTime).min(LocalDateTime::compareTo).get();

            epic.setStartTime(minStartTime);

            LocalDateTime maxEndTime = subtasksByEpic.stream()
                    .map(SubTask::getEndTime).max(LocalDateTime::compareTo).get();

            epic.setEpicEndTime(maxEndTime);

            epic.setDuration(Duration.between(minStartTime, maxEndTime));
        }
        if (subtasksByEpic.isEmpty()) {
            epic.setDuration(null);
            epic.setStartTime(null);
            epic.setEpicEndTime(null);
        }
    }

    @Override
    public void checkCrossDateAndTimeTasks(Task task) {
        LocalDateTime startTime = task.getStartTime();

        if (startTime == null) {
            return;
        }

        for (Task prioritizedTask : prioritizedTasks) {
            if (prioritizedTask.getStartTime() == null) {
                return;
            }
            boolean firstValidateCross = task.getStartTime().isEqual(prioritizedTask.getStartTime());
            boolean secondValidateCross = task.getEndTime().isEqual(prioritizedTask.getEndTime());
            if (firstValidateCross && secondValidateCross) {
                System.out.println("На заданное время уже есть задача");
                break;
            }
        }
    }
}
