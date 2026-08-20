public class Task {
    protected String description;
    protected boolean isDone;
    protected String taskType;
    protected String by;

    public Task(String description, String taskType) {
        this.description = description;
        this.isDone = false;
        this.taskType = taskType;
        this.by = null;
    }

    public Task(String description, String taskType, String by) {
        this(description, taskType);
        this.by = by;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    public String getTaskTypeIcon() {
        return taskType;
    }

    public String getDescription() {
        return description;
    }

    public String getDeadlineDetails() {
        return by == null ? "" : " (by: " + by + ")";
    }

    public void markAsDone() {
        isDone = true;
    }

    public void unmarkAsDone() {
        isDone = false;
    }
}
