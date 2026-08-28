public class Todo extends Task {
    public Todo(String description) {
        super(description, TaskType.TODO);
    }

    @Override
    public String getScheduleDetails() {
        return "";
    }

    @Override
    public String toDataString() {
        return "T | " + (isDone ? 1 : 0) + " | " + description;
    }
}
