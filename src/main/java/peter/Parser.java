package peter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import peter.command.AddCommand;
import peter.command.Command;
import peter.command.DeleteCommand;
import peter.command.ExitCommand;
import peter.command.FindCommand;
import peter.command.FindOnDateCommand;
import peter.command.ListCommand;
import peter.command.MarkCommand;
import peter.task.Deadline;
import peter.task.Event;
import peter.task.ScheduleDateTime;
import peter.task.Task;
import peter.task.Todo;

/**
 * Interprets user commands and converts their arguments into application data.
 */
public final class Parser {
    private Parser() {
    }

    /**
     * Turns a line of user input into the command it asks for.
     *
     * @param command complete command entered by the user.
     * @return command ready to be executed.
     * @throws PeterException if the command is unrecognized or its arguments are invalid.
     */
    public static Command parse(String command) throws PeterException {
        return switch (getCommandWord(command)) {
            case "bye" -> new ExitCommand();
            case "list" -> new ListCommand();
            case "on" -> new FindOnDateCommand(parseQueryDate(command));
            case "find" -> new FindCommand(parseKeyword(command));
            case "todo", "deadline", "event" -> new AddCommand(parseTask(command));
            case "mark" -> new MarkCommand(command, true);
            case "unmark" -> new MarkCommand(command, false);
            case "delete" -> new DeleteCommand(command);
            default -> throw new AssertionError("Unhandled command word");
        };
    }

    /**
     * Identifies the command word while rejecting unsupported command shapes.
     *
     * @param command complete command entered by the user.
     * @return recognized command word.
     * @throws PeterException if the command is not recognized.
     */
    private static String getCommandWord(String command) throws PeterException {
        if (command.equals("bye") || command.equals("list")) {
            return command;
        }

        String commandWord = matchCommandWord(command,
                "on", "find", "todo", "deadline", "event", "mark", "unmark", "delete");
        if (commandWord == null) {
            throw new PeterException("I'm sorry, but I don't understand that command. Please try again.");
        }
        return commandWord;
    }

    /**
     * Returns the first of the given command words that the command starts
     * with, or {@code null} if the command matches none of them.
     *
     * <p>The candidates are var-args so the caller can list them inline,
     * rather than building an array only for this method to read.
     *
     * @param command complete command entered by the user.
     * @param commandWords command words to test, in order of preference.
     * @return matching command word, or {@code null} if there is none.
     */
    private static String matchCommandWord(String command, String... commandWords) {
        for (String commandWord : commandWords) {
            if (command.equals(commandWord) || command.startsWith(commandWord + " ")) {
                return commandWord;
            }
        }
        return null;
    }

    /**
     * Creates a task from a todo, deadline, or event command.
     *
     * @param command complete task-creation command.
     * @return task described by the command.
     * @throws PeterException if a required field is missing or invalid.
     */
    private static Task parseTask(String command) throws PeterException {
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
     * @param command complete on command.
     * @return requested date.
     * @throws PeterException if the date is missing or invalid.
     */
    private static LocalDate parseQueryDate(String command) throws PeterException {
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
     * Extracts the keyword requested by a {@code find} command.
     *
     * <p>The keyword is not checked against the storage delimiter, since a
     * search term is never written to the data file.
     *
     * @param command complete find command.
     * @return keyword to search descriptions for.
     * @throws PeterException if the keyword is missing.
     */
    private static String parseKeyword(String command) throws PeterException {
        String keyword = command.substring("find".length()).trim();
        if (keyword.isEmpty()) {
            throw new PeterException("Use 'find <keyword>' (e.g., find book).");
        }
        return keyword;
    }

    /**
     * Converts a one-based task number in a command into a list index.
     *
     * <p>Called by the mark, unmark, and delete commands while they run, since
     * the checks below need the current task count.
     *
     * @param command complete mark, unmark, or delete command.
     * @param taskCount current number of tasks.
     * @return zero-based task index.
     * @throws PeterException if the task number is missing or invalid.
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

        LocalDateTime start = parseDate(startDate, "start");
        LocalDateTime end = parseDate(endDate, "end");
        // An event whose end precedes its start covers no dates at all, so it
        // would never appear under 'on'. Reject it here rather than storing a
        // task the user can never see again.
        if (end.isBefore(start)) {
            throw new PeterException(
                    "Please make sure the end date is not before the start date.");
        }
        return new Event(description, start, end);
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
                throw new PeterException("Oh dear! Task details cannot contain ' | '.");
            }
        }
    }
}
