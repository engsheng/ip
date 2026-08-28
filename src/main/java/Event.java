import java.time.LocalDate;

/**
 * Represents an event occurring between two dates.
 */
public class Event extends Task {
    private final LocalDate from;
    private final LocalDate to;

    public Event(String description, LocalDate from, LocalDate to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getScheduleDetails() {
        return " (from: " + formatDate(from) + " to: " + formatDate(to) + ")";
    }

    @Override
    public String toDataString() {
        return "E | " + (isDone ? 1 : 0) + " | " + description + " | " + from + " | " + to;
    }
}
