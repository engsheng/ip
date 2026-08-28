public class Event extends Task {
    private final String from;
    private final String to;

    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getScheduleDetails() {
        return " (from: " + from + " to: " + to + ")";
    }

    @Override
    public String toDataString() {
        return "E | " + (isDone ? 1 : 0) + " | " + description + " | " + from + " | " + to;
    }
}
