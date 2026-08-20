public class Task {
    protected String description;
    protected boolean isDone;
    protected String taskType;
    protected String by;
    protected String from;
    protected String to;

    public Task(String description, String taskType) {
        this.description = description;
        this.isDone = false;
        this.taskType = taskType;
        this.by = null;
        this.from = null;
        this.to = null;
    }

    public Task(String description, String taskType, String by) {
        this(description, taskType);
        this.by = by;
    }

    public Task(String description, String taskType, String from, String to) {
        this(description, taskType);
        this.from = from;
        this.to = to;
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

    public String getScheduleDetails() {
        if (by != null) {
            return " (by: " + by + ")";
        }
        if (from != null && to != null) {
            return " (from: " + from + " to: " + to + ")";
        }
        return "";
    }

    public void markAsDone() {
        isDone = true;
    }

    public void unmarkAsDone() {
        isDone = false;
    }
}
