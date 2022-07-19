package manager;

public class Managers {
    private static HistoryManager InMemoryHistoryManager = new InMemoryHistoryManager();

    public static HistoryManager getDefaultHistory() {
        return InMemoryHistoryManager;
    }
}
