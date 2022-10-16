package tasks;

import constants.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTasksId = new ArrayList<>();
    protected LocalDateTime epicEndTime;

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public Epic(int id, String name, String description, Status status, Integer duration, String startTimeOfTask) {
        super(id, name, description, status, duration, startTimeOfTask);
    }

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public void setEpicEndTime(LocalDateTime epicEndTime) {
        this.epicEndTime = epicEndTime;
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

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                getId(), getTaskType(), getName(),
                getStatus(), getDescription(), duration, getStartTime());
    }
}
