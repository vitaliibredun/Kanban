package tasks;
import constants.Status;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, String description, Status status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        String format = String.format("%s,%s,%s,%s,%s,%s",
                getId(), getTaskType(), getName(), getStatus(),
                getDescription(), getEpicId());
        return format;
    }
}
