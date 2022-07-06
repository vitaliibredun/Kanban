package task;

import constants.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTasksId = new ArrayList<>();

    public Epic(String name, Status status, String description) {
        super(name, status, description);
    }

    public void addSubtask(int subTaskId) {
        subTasksId.add(subTaskId);
    }

    public List<Integer> getSubTasksId() {
        return subTasksId;
    }
}
