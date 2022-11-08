package manager;

import constants.Status;
import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FileBackedTasksManager
        extends InMemoryTaskManager implements TaskManager {
    private final File file;
    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    protected  void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bufferedWriter.write("id,type,name,status,description,duration,startTime,epic by subtask");
            bufferedWriter.newLine();

            tasksToFile(bufferedWriter, tasks);
            tasksToFile(bufferedWriter, epics);
            tasksToFile(bufferedWriter, subtasks);
            bufferedWriter.newLine();

            historyOfTasksToFile(bufferedWriter, historyManager);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи");
        }
    }

    private <T extends Task> void tasksToFile(BufferedWriter bufferedWriter,
                                              Map<Integer, T> tasks) throws IOException {
        for (T value : tasks.values()) {
            bufferedWriter.write(value.toString());
            bufferedWriter.newLine();
        }
    }

    private void historyOfTasksToFile(BufferedWriter bufferedWriter,
                                      HistoryManager historyManager) throws IOException {
        List<Task> history = historyManager.getHistory();
        for (Task task : history) {
            bufferedWriter.write(String.format("%s,", task.getId()));
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

        try {
            String stringFromFile = Files.readString(file.toPath());

            List<Task> listOfTasks = parseTasksFromCSV(stringFromFile);
            for (Task task : listOfTasks) {
                switch (task.getTaskType()) {
                    case TASK -> fileBackedTasksManager.addTask(task);
                    case EPIC -> fileBackedTasksManager.addEpic((Epic) task);
                    case SUBTASK -> fileBackedTasksManager.addSubtask((SubTask) task);
                }
            }

            List<Integer> listOfIDs = parseTaskHistoryFromCSV(stringFromFile);
            for (Integer listOfID : listOfIDs) {
                if (tasks.containsKey(listOfID)) {
                    fileBackedTasksManager.getTask(listOfID);
                } else if (epics.containsKey(listOfID)) {
                    fileBackedTasksManager.getEpic(listOfID);
                } else if (subtasks.containsKey(listOfID)) {
                    fileBackedTasksManager.getSubtask(listOfID);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения");
        }
        return fileBackedTasksManager;
    }

    private static List<Task> parseTasksFromCSV(String value) {
        List<Task> tasks = new ArrayList<>();
        String[] split = value.split("\n");

        for (int i = 1; i < split.length - 2; i++) {
            String[] valueFromFile = split[i].split(",");

            int id = Integer.parseInt(valueFromFile[0]);
            TaskType type = TaskType.valueOf(valueFromFile[1]);
            String name = valueFromFile[2];
            Status status = Status.valueOf(valueFromFile[3]);
            String description = valueFromFile[4];
            Integer duration;
            if (valueFromFile[5].contains("null")) {
                duration = null;
            } else {
                String volume = valueFromFile[5].substring(2, valueFromFile[5].length() - 1);
                duration = Integer.valueOf(volume);
            }

            String startTime;
            if (valueFromFile[6].contains("null")) {
                startTime = null;
            } else {
                startTime = valueFromFile[6];
            }

            Integer epicBySubtask = null;
            if (type.equals(TaskType.SUBTASK)) {
                epicBySubtask = Integer.valueOf(valueFromFile[7]);
            }

            switch (type) {
                case TASK -> tasks.add(new Task(id, name, description, status, duration, startTime));
                case EPIC -> tasks.add(new Epic(id, name, description, status, duration, startTime));
                case SUBTASK -> tasks.add(new SubTask(id, name, description, status, duration,startTime, epicBySubtask));
            }
        }
        return tasks;
    }

    private static List<Integer> parseTaskHistoryFromCSV(String value) {
        List<Integer> listOfId = new ArrayList<>();

        String[] valuesFromFile = value.split("\n\n");
        String historyIDs = valuesFromFile[1];
        String[] historyID = historyIDs.split(",");

        for (String idFromHistory : historyID) {
            listOfId.add(Integer.valueOf(idFromHistory));
        }
        return listOfId;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(SubTask subTask) {
        super.addSubtask(subTask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void removeTask(int taskId) {
        super.removeTask(taskId);
        save();
    }

    @Override
    public void removeEpic(int epicId) {
        super.removeEpic(epicId);
        save();
    }

    @Override
    public void removeSubtask(int subtaskId) {
        super.removeSubtask(subtaskId);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(SubTask subTask) {
        super.updateSubtask(subTask);
        save();
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> list = super.getAllTasks();
        save();
        return list;
    }

    @Override
    public List<Epic> getALLEpics() {
        List<Epic> list = super.getALLEpics();
        save();
        return list;
    }

    @Override
    public List<SubTask> getALLSubtasks() {
        List<SubTask> list = super.getALLSubtasks();
        save();
        return list;
    }

    @Override
    public Task getTask(int taskId) {
        Task task = super.getTask(taskId);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int epicId) {
        Epic epic = super.getEpic(epicId);
        save();
        return epic;
    }

    @Override
    public SubTask getSubtask(int subtaskId) {
        SubTask subtask = super.getSubtask(subtaskId);
        save();
        return subtask;
    }

    @Override
    public List<SubTask> getSubtasksByEpic(int epicId) {
        List<SubTask> list = super.getSubtasksByEpic(epicId);
        save();
        return list;
    }

    @Override
    public Status updateEpicStatus(Epic epic) {
        Status status = super.updateEpicStatus(epic);
        save();
        return status;
    }
}
