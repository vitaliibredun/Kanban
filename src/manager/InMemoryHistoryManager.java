package manager;
import tasks.Task;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final int LIMIT_HISTORY = 10;
    private List<Task> tasksHistory = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (tasksHistory.size() >= LIMIT_HISTORY) {
            tasksHistory.remove(0);
        }
        tasksHistory.add(task);

    }

    @Override
    public List<Task> getHistory() {
        return tasksHistory;
    }
}
