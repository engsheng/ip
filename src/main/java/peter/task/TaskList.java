package peter.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Owns the in-memory collection of tasks and its basic list operations.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates a task list holding the given tasks in the order supplied.
     *
     * <p>Being var-args, this one constructor covers both an empty list
     * ({@code new TaskList()}) and a known set of tasks
     * ({@code new TaskList(todo, deadline)}), so a caller that already has the
     * tasks in hand need not wrap them in a collection first.
     *
     * @param tasks initial tasks, in the order they should appear.
     */
    public TaskList(Task... tasks) {
        this.tasks = new ArrayList<>(List.of(tasks));
    }

    /**
     * Creates a task list containing the supplied tasks in their current order.
     *
     * <p>Kept alongside the var-args constructor for callers such as storage,
     * whose tasks arrive as a collection whose size is unknown until run time.
     *
     * @param tasks initial tasks.
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
     * @param index task index.
     * @return task at the index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Inserts a task at an index, primarily when restoring a failed deletion.
     *
     * @param index position at which to restore the task.
     * @param task task to insert.
     */
    public void add(int index, Task task) {
        tasks.add(index, task);
    }

    /**
     * Removes and returns the task at an index.
     *
     * @param index task index.
     * @return removed task.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Changes whether a task is completed.
     *
     * @param index task index.
     * @param isDone new completion status.
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
     * @return unmodifiable task list view.
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }
}
