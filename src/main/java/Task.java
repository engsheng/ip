public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public abstract String getTaskTypeIcon();

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
