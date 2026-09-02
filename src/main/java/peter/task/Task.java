package peter.task;

import java.time.LocalDate;
import java.util.Locale;

/**
 * Represents a task tracked by Peter.
 */
public abstract class Task {
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
     * @return the serialized task.
     */
    public abstract String toDataString();

    /**
     * Checks whether this task is scheduled on a given date. Tasks without a
     * schedule return {@code false}; scheduled task types override this method.
     *
     * @param date date to check.
     * @return whether the task occurs on the date.
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }

    /**
     * Returns whether this task's description contains the given keyword,
     * ignoring case.
     *
     * <p>Only the description is searched, so a keyword matching a task's
     * dates or type icon does not count. Matching is on any substring rather
     * than whole words, so {@code book} also finds {@code bookshop}.
     *
     * @param keyword keyword to look for.
     * @return whether the description contains the keyword.
     */
    public boolean hasKeyword(String keyword) {
        return description.toLowerCase(Locale.ROOT)
                .contains(keyword.toLowerCase(Locale.ROOT));
    }

    public void markAsDone() {
        isDone = true;
    }

    public void unmarkAsDone() {
        isDone = false;
    }
}
