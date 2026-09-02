package peter.task;

/**
 * Represents a task with no date attached to it.
 *
 * <p>Being unscheduled is what distinguishes a todo from the other task
 * types: it displays no schedule and never matches an {@code on <date>}
 * search, for which it relies on {@link Task#occursOn} returning
 * {@code false} by default.
 */
public class Todo extends Task {
    /**
     * Creates an incomplete todo.
     *
     * @param description what the task says
     */
    public Todo(String description) {
        super(description, TaskType.TODO);
    }

    /**
     * Returns an empty string, since a todo has no schedule to display.
     */
    @Override
    public String getScheduleDetails() {
        return "";
    }

    /**
     * {@inheritDoc}
     *
     * <p>A todo line holds only the type, status, and description, giving the
     * three fields {@code Storage} expects for type {@code T}.
     */
    @Override
    public String toDataString() {
        return "T | " + (isDone ? 1 : 0) + " | " + description;
    }
}
