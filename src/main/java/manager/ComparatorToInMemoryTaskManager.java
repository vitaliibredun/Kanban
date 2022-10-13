package manager;

import tasks.Task;

import java.util.Comparator;

public class ComparatorToInMemoryTaskManager implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        if ((task1.getStartTime() == null) && (task2.getStartTime() != null)) {
            return 1;
        }
        if ((task1.getStartTime() != null) && (task2.getStartTime() == null)) {
            return -1;
        }
        if ((task1.getStartTime() == null) && (task2.getStartTime() == null)) {
            return 0;
        }
        return task1.getStartTime().compareTo(task2.getStartTime());
    }
}
