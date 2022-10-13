package manager;

public class Node<T> {
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
