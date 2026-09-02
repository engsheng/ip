package peter.task;

/**
 * The kinds of task the chatbot supports, each paired with the one-letter
 * icon that identifies it.
 *
 * <p>The same letter is used both in the task list shown to the user and as
 * the first field of a line in the data file, so these values must stay in
 * step with the type letters {@code Storage} recognises.
 */
public enum TaskType {
    /** A task with no date attached. */
    TODO("T"),

    /** A task due by a particular date and time. */
    DEADLINE("D"),

    /** A task running between two dates and times. */
    EVENT("E");

    private final String icon;

    TaskType(String icon) {
        this.icon = icon;
    }

    /**
     * Returns the one-letter icon for this task type.
     */
    public String getIcon() {
        return icon;
    }
}
