/**
 * Represents a task tracked by Peter.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;
    private final TaskType type;

    public Task(String description, TaskType type) {
        this.description = description;
        this.type = type;
        this.isDone = false;
    }

    public String getTaskTypeIcon() {
        return type.getIcon();
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    public abstract String getScheduleDetails();

    /**
     * Converts this task into the line format used in the data file.
     *
     * @return the serialized task
     */
    public abstract String toDataString();

    public void markAsDone() {
        isDone = true;
    }

    public void unmarkAsDone() {
        isDone = false;
    }
}
