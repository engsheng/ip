package peter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents an event occurring between two dates and times.
 */
public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getScheduleDetails() {
        return " (from: " + ScheduleDateTime.format(from)
                + " to: " + ScheduleDateTime.format(to) + ")";
    }

    @Override
    public String toDataString() {
        return "E | " + (isDone ? 1 : 0) + " | " + description + " | " + from + " | " + to;
    }

    @Override
    public boolean occursOn(LocalDate date) {
        LocalDate startDate = from.toLocalDate();
        LocalDate endDate = to.toLocalDate();
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
