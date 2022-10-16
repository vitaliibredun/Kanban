package tasks;

import constants.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected Status status;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String name, String description, Status status, Integer duration, String startTimeOfTask) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTimeOfTask, DateTimeFormatter.ofPattern("HH/mm/dd/MM/yyyy"));
    }

    public Task(int id, String name, String description, Status status, Integer duration, String startTimeOfTask) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        if (duration == null) {
            this.duration = null;
        } else {
            this.duration = Duration.ofMinutes(duration);
        }

        if (startTimeOfTask == null) {
            this.startTime = null;
        } else {
            this.startTime = LocalDateTime.parse(startTimeOfTask);
        }
    }

    public Task(int id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                getId(), getTaskType(), getName(),
                getStatus(), getDescription(), duration, getStartTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return getId() == task.getId() && Objects.equals(getName(), task.getName()) && Objects.equals(getDescription(), task.getDescription()) && getStatus() == task.getStatus() && Objects.equals(duration, task.duration) && Objects.equals(getStartTime(), task.getStartTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getStatus(), duration, getStartTime());
    }
}
