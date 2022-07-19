import constants.Status;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;



public class Main {

    public static void main(String[] args) {
        TaskManager manager = new InMemoryTaskManager();

        Task task = new Task("some task name", "some task description", Status.NEW);
        manager.addTask(task);

        Epic epic = new Epic("some epic name", "some epic description");
        manager.addEpic(epic);

        SubTask subTask = new SubTask("some subtask name", "some subtask description", Status.NEW, epic.getId());
        manager.addSubtask(subTask);

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
    }
}
