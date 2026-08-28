import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task tracked by Peter.
 */
public abstract class Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);

    protected String description;
    protected boolean isDone;
    private final TaskType type;

    public Task(String description, TaskType type) {
        this.description = description;
        this.type = type;
        this.isDone = false;
    }

    public String getTaskTypeIcon() {
        return type.getIcon();
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    public abstract String getScheduleDetails();

    /**
     * Converts this task into the line format used in the data file.
     *
     * @return the serialized task
     */
    public abstract String toDataString();

    /**
     * Formats a date consistently for display to the user.
     *
     * @param date date to format
     * @return date in a friendly format such as {@code Oct 15 2019}
     */
    protected String formatDate(LocalDate date) {
        return date.format(DISPLAY_DATE_FORMAT);
    }

    public void markAsDone() {
        isDone = true;
    }

    public void unmarkAsDone() {
        isDone = false;
    }
}
