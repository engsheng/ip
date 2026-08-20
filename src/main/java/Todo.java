public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String getTaskTypeIcon() {
        return "T";
    }

    @Override
    public String getScheduleDetails() {
        return "";
    }
}
