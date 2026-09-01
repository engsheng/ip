package peter.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a task that must be completed by a particular date and time.
 */
public class Deadline extends Task {
    private final LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    @Override
    public String getScheduleDetails() {
        return " (by: " + ScheduleDateTime.format(by) + ")";
    }

    @Override
    public String toDataString() {
        return "D | " + (isDone ? 1 : 0) + " | " + description + " | " + by;
    }

    @Override
    public boolean occursOn(LocalDate date) {
        return by.toLocalDate().equals(date);
    }
}
