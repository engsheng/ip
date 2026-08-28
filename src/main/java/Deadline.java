import java.time.LocalDate;

/**
 * Represents a task that must be completed by a particular date.
 */
public class Deadline extends Task {
    private final LocalDate by;

    public Deadline(String description, LocalDate by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    @Override
    public String getScheduleDetails() {
        return " (by: " + formatDate(by) + ")";
    }

    @Override
    public String toDataString() {
        return "D | " + (isDone ? 1 : 0) + " | " + description + " | " + by;
    }
}
