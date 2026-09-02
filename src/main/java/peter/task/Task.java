package peter.task;

import java.time.LocalDate;
import java.util.Locale;

/**
 * Represents a task tracked by Peter.
 */
public abstract class Task {
    /**
     * What the task says, as the user typed it. Subclasses read this directly
     * when building their data-file line, so it must never contain the
     * {@code " | "} field delimiter used by that file.
     */
    protected String description;

    /**
     * Whether the task has been completed. Subclasses read this directly to
     * write the status flag into their data-file line.
     */
    protected boolean isDone;

    /** Fixed task type, used to pick the icon shown in the task list. */
    private final TaskType type;

    /**
     * Creates a task that starts out incomplete.
     *
     * @param description what the task says
     * @param type kind of task being created
     */
    public Task(String description, TaskType type) {
        this.description = description;
        this.type = type;
        this.isDone = false;
    }

    /**
     * Returns the one-letter icon identifying this task's type, such as
     * {@code T} for a todo.
     */
    public String getTaskTypeIcon() {
        return type.getIcon();
    }

    /**
     * Returns {@code X} when the task is done and a single space otherwise, so
     * that {@code [ ]} and {@code [X]} stay the same width in the task list.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns what the task says.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether the task has been completed.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the schedule to display after the description, including its
     * leading space and brackets, or an empty string for a task with no
     * schedule.
     *
     * @return displayable schedule text
     */
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

    /**
     * Marks the task as completed. Marking an already-completed task has no
     * further effect.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks the task as not yet completed. Unmarking an incomplete task has no
     * further effect.
     */
    public void unmarkAsDone() {
        isDone = false;
    }
}
