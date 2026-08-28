import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Interprets user commands and converts their arguments into application data.
 */
public final class Parser {
    private Parser() {
    }

    /**
     * Identifies the command word while rejecting unsupported command shapes.
     *
     * @param command complete command entered by the user
     * @return recognized command word
     * @throws PeterException if the command is not recognized
     */
    public static String getCommandWord(String command) throws PeterException {
        if (command.equals("bye") || command.equals("list")) {
            return command;
        }

        String[] commandsWithArguments = {
            "on", "todo", "deadline", "event", "mark", "unmark", "delete"
        };
        for (String commandWord : commandsWithArguments) {
            if (command.equals(commandWord) || command.startsWith(commandWord + " ")) {
                return commandWord;
            }
        }
        throw new PeterException("I'm sorry, but I don't understand that command. Please try again.");
    }

    /**
     * Creates a task from a todo, deadline, or event command.
     *
     * @param command complete task-creation command
     * @return task described by the command
     * @throws PeterException if a required field is missing or invalid
     */
    public static Task parseTask(String command) throws PeterException {
        return switch (getCommandWord(command)) {
            case "todo" -> parseTodo(command);
            case "deadline" -> parseDeadline(command);
            case "event" -> parseEvent(command);
            default -> throw new PeterException(
                    "I'm sorry, but I don't understand that command. Please try again.");
        };
    }

    /**
     * Extracts the date requested by an {@code on} command.
     *
     * @param command complete on command
     * @return requested date
     * @throws PeterException if the date is missing or invalid
     */
    public static LocalDate parseQueryDate(String command) throws PeterException {
        String dateText = command.substring("on".length()).trim();
        if (dateText.isEmpty()) {
            throw new PeterException("Use 'on <date>' (e.g., on 2019-12-02).");
        }
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            throw new PeterException(
                    "Please enter the date in yyyy-MM-dd format (e.g., 2019-12-02).", e);
        }
    }

    /**
     * Converts a one-based task number in a command into a list index.
     *
     * @param command complete mark, unmark, or delete command
     * @param taskCount current number of tasks
     * @return zero-based task index
     * @throws PeterException if the task number is missing or invalid
     */
    public static int parseTaskIndex(String command, int taskCount) throws PeterException {
        String action = getCommandWord(command);
        String taskNumberText = command.substring(action.length()).trim();
        if (taskNumberText.isEmpty()) {
            throw new PeterException("Oh dear! Please provide a task number to " + action + ".");
        }
        if (taskCount == 0) {
            throw new PeterException("Oh dear! There are no tasks to " + action + ".");
        }

        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new PeterException(
                        "Oh dear! Task number must be between 1 and " + taskCount + ".");
            }
            return taskNumber - 1;
        } catch (NumberFormatException e) {
            throw new PeterException(
                    "Oh dear! Please enter an integer task number to " + action + ".");
        }
    }

    private static Todo parseTodo(String command) throws PeterException {
        String description = command.length() == "todo".length()
                ? "" : command.substring("todo ".length());
        if (description.isBlank()) {
            throw new PeterException("Please include a description after 'todo'.");
        }
        validateStorageFields(description);
        return new Todo(description);
    }

    private static Deadline parseDeadline(String command) throws PeterException {
        int byMarkerIndex = command.indexOf(" /by ");
        if (byMarkerIndex == -1) {
            if (command.endsWith(" /by")) {
                throw new PeterException("Please include a due date after '/by'.");
            }
            throw new PeterException("Use 'deadline <description> /by <date>'.");
        }
        if (byMarkerIndex <= "deadline".length()) {
            throw new PeterException("Please include a description before '/by'.");
        }

        String description = command.substring("deadline ".length(), byMarkerIndex);
        String dueDate = command.substring(byMarkerIndex + " /by ".length());
        if (description.isBlank()) {
            throw new PeterException("Please include a description before '/by'.");
        }
        if (dueDate.isBlank()) {
            throw new PeterException("Please include a due date after '/by'.");
        }
        validateStorageFields(description, dueDate);
        return new Deadline(description, parseDate(dueDate, "due"));
    }

    private static Event parseEvent(String command) throws PeterException {
        int fromMarkerIndex = command.indexOf(" /from ");
        int toMarkerIndex = command.indexOf(" /to ");
        if (fromMarkerIndex == -1 || toMarkerIndex == -1 || fromMarkerIndex >= toMarkerIndex) {
            if (command.endsWith(" /from")) {
                throw new PeterException("Please include a start date after '/from'.");
            }
            if (command.endsWith(" /to")) {
                throw new PeterException("Please include an end date after '/to'.");
            }
            throw new PeterException(
                    "Use 'event <description> /from <start-date> /to <end-date>'.");
        }
        if (fromMarkerIndex <= "event".length()) {
            throw new PeterException("Please include a description before '/from'.");
        }
        if (toMarkerIndex <= fromMarkerIndex + " /from ".length()) {
            throw new PeterException("Please include a start date after '/from'.");
        }

        String description = command.substring("event ".length(), fromMarkerIndex);
        String startDate = command.substring(fromMarkerIndex + " /from ".length(), toMarkerIndex);
        String endDate = command.substring(toMarkerIndex + " /to ".length());
        if (description.isBlank()) {
            throw new PeterException("Please include a description before '/from'.");
        }
        if (startDate.isBlank()) {
            throw new PeterException("Please include a start date after '/from'.");
        }
        if (endDate.isBlank()) {
            throw new PeterException("Please include an end date after '/to'.");
        }
        validateStorageFields(description, startDate, endDate);
        return new Event(description, parseDate(startDate, "start"), parseDate(endDate, "end"));
    }

    private static LocalDateTime parseDate(String dateText, String dateName) throws PeterException {
        try {
            return ScheduleDateTime.parseUserInput(dateText);
        } catch (DateTimeParseException e) {
            throw new PeterException("Please enter the " + dateName + " date as yyyy-MM-dd"
                    + " or d/M/yyyy HHmm (e.g., 2019-10-15 or 2/12/2019 1800).", e);
        }
    }

    /** Rejects the delimiter used to separate fields in the data file. */
    private static void validateStorageFields(String... fields) throws PeterException {
        for (String field : fields) {
            if (field.contains(" | ")) {
                throw new PeterException("Oh dear!Task details cannot contain ' | '.");
            }
        }
    }
}
