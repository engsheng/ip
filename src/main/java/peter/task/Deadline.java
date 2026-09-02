package peter.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a task that must be completed by a particular date and time.
 */
public class Deadline extends Task {
    private final LocalDateTime by;

    /**
     * Creates an incomplete deadline.
     *
     * @param description what the task says
     * @param by date and time the task is due, at midnight if the user gave
     *     only a date
     */
    public Deadline(String description, LocalDateTime by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    /**
     * Returns the due date in brackets. The time is included only when the
     * deadline is not at midnight, so a date-only deadline reads naturally.
     */
    @Override
    public String getScheduleDetails() {
        return " (by: " + ScheduleDateTime.format(by) + ")";
    }

    /**
     * {@inheritDoc}
     *
     * <p>The due date is written in ISO form so that it can be read back
     * exactly, rather than in the friendlier display format.
     */
    @Override
    public String toDataString() {
        return "D | " + (isDone ? 1 : 0) + " | " + description + " | " + by;
    }

    /**
     * Returns whether the deadline falls due on the given date.
     *
     * <p>Only the date is compared, so a deadline due at any time of day
     * matches its own date and no other.
     *
     * @param date date to check
     * @return whether the task is due on the date
     */
    @Override
    public boolean occursOn(LocalDate date) {
        return by.toLocalDate().equals(date);
    }
}
