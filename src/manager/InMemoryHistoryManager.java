package manager;
import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    CustomLinkedList<Task> customLinkedList = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        customLinkedList.removeNode(customLinkedList.nodesMap.get(task.getId()));
        customLinkedList.linkLast(task);
        customLinkedList.nodesMap.put(task.getId(), customLinkedList.tail);
    }

    @Override
    public void remove(int taskId) {
        customLinkedList.nodesMap.remove(taskId);
    }

    @Override
    public List<Object> getHistory() {
        return customLinkedList.getTasks();
    }

    public static class CustomLinkedList<T> {
        final private Map<Integer, Node<T>> nodesMap = new HashMap<>();
        private Node<T> head;
        private Node<T> tail;

        private void linkLast(T task) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(task, oldTail, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
        }

        private void removeNode(Node<T> node) {
            if (node == null) {
                return;
            } else if (head == tail) {
                head = null;
                tail = null;
            } else if (node.prev == null) {
                head = node.next;
                node.next = null;
            } else if (node.next == null) {
                tail = node.prev;
                node.prev = null;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
            nodesMap.remove(node.task);
        }

        public List<Object> getTasks() {
            List<Object> list = new ArrayList<>();

            for (Node<T> i = head; i != null; i = i.next) {
                list.add(i.task);
            }
            return list;
        }

        private static class Node<T> {
            T task;
            Node<T> prev;
            Node<T> next;

            public Node(T task, Node<T> prev, Node<T> next) {
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
}
