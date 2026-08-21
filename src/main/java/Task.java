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

    public abstract String getScheduleDetails();

    public void markAsDone() {
        isDone = true;
    }

    public void unmarkAsDone() {
        isDone = false;
    }
}
