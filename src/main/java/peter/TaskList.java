package peter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Owns the in-memory collection of tasks and its basic list operations.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks in their current order.
     *
     * @param tasks initial tasks
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Returns the number of tasks in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at a zero-based index.
     *
     * @param index task index
     * @return task at the index
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Inserts a task at an index, primarily when restoring a failed deletion.
     *
     * @param index position at which to restore the task
     * @param task task to insert
     */
    public void add(int index, Task task) {
        tasks.add(index, task);
    }

    /**
     * Removes and returns the task at an index.
     *
     * @param index task index
     * @return removed task
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Changes whether a task is completed.
     *
     * @param index task index
     * @param isDone new completion status
     */
    public void setDone(int index, boolean isDone) {
        if (isDone) {
            tasks.get(index).markAsDone();
        } else {
            tasks.get(index).unmarkAsDone();
        }
    }

    /**
     * Provides a read-only view for saving the current tasks.
     *
     * @return unmodifiable task list view
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }
}
