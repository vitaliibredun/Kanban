package tasks;
import constants.Status;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTasksId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public void addSubtaskId(int subTaskId) {
        subTasksId.add(subTaskId);
    }

    public List<Integer> getSubTasksId() {
        return subTasksId;
    }

    public TaskType getTaskType() {
        return TaskType.EPIC;
    }
}
