package manager;
import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public void add(Task task) {
        removeNode(nodeMap.get(task));
        linkLast(task);
        nodeMap.put(task.getId(), last);
    }

    private void linkLast(Task task) {
        final Node oldLast = last;
        final Node newNode = new Node(task, oldLast, null);
        last = newNode;
        if (oldLast == null) {
            first = newNode;
        } else {
            oldLast.next = newNode;
        }
    }

    @Override
    public void remove(int taskId) {
        nodeMap.remove(taskId);
    }

    private void removeNode(Node node) {
        nodeMap.remove(node);
        if (node == null) {
            return;
        }
        if (node != null) {
            if (node.prev != null) {
                node.prev.next = node.next;
            } else {
                first = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            } else {
                last = node.prev;
            }
        }
    }

    public List<Task> getTasks() {
        List<Task> list = new ArrayList<>();

        for (Node i = first; i != null; i = i.next) {
            list.add(i.task);
        }
        return list;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private static class Node {
        Task task;
        Node prev;
        Node next;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "task=" + task +
                    ", prev=" + (prev != null ? prev.task : null) +
                    ", next=" + (next != null ? next.task : null) +
                    '}';
        }
    }
}
