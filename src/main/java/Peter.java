import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Peter {
    public static void main(String[] args) {
        String divider = "____________________________________________________________";
        String banner = " ____      _\n"
                + "|  _ \\ ___| |_ ___ _ __\n"
                + "| |_) / _ \\ __/ _ \\ '__|\n"
                + "|  __/  __/ ||  __/ |\n"
                + "|_|   \\___|\\__\\___|_|\n";

        System.out.println(divider);
        System.out.print(banner);
        System.out.println("Yo! I'm Peter.");
        System.out.println("What crazy adventures are we making today?");
        System.out.println(divider);

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks;
        try {
            tasks = Storage.load();
        } catch (PeterException e) {
            System.out.println(e.getMessage());
            System.out.println(divider);
            return;
        }
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(divider);
            try {
                if (command.equals("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    System.out.println(divider);
                    break;
                } else if (command.equals("list")) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        Task task = tasks.get(i);
                        System.out.println((i + 1) + ".[" + task.getTaskTypeIcon() + "]["
                                + task.getStatusIcon() + "] "
                                + task.getDescription() + task.getScheduleDetails());
                    }
                } else if (command.equals("on") || command.startsWith("on ")) {
                    String dateText = command.substring(2).trim();
                    if (dateText.isEmpty()) {
                        throw new PeterException("Use 'on <date>' (e.g., on 2019-12-02).");
                    }
                    LocalDate date = parseQueryDate(dateText);
                    printTasksOnDate(tasks, date);
                } else if (command.equals("todo") || command.startsWith("todo ")) {
                    String description = command.length() == 4 ? "" : command.substring(5);
                    if (description.isBlank()) {
                        throw new PeterException("Please include a description after 'todo'.");
                    }
                    validateStorageFields(description);
                    addTask(tasks, new Todo(description));
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  [T][ ] " + description);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                    int byMarkerIndex = command.indexOf(" /by ");
                    if (byMarkerIndex == -1) {
                        if (command.endsWith(" /by")) {
                            throw new PeterException("Please include a due date after '/by'.");
                        }
                        throw new PeterException("Use 'deadline <description> /by <date>'.");
                    }
                    if (byMarkerIndex <= 9) {
                        throw new PeterException("Please include a description before '/by'.");
                    }
                    String description = command.substring(9, byMarkerIndex);
                    String by = command.substring(byMarkerIndex + 5);
                    // To handle cases where the user enters blank description or blank due date
                    if (description.isBlank()) {
                        throw new PeterException("Please include a description before '/by'.");
                    }
                    if (by.isBlank()) {
                        throw new PeterException("Please include a due date after '/by'.");
                    }
                    validateStorageFields(description, by);
                    Deadline deadline = new Deadline(description, parseDate(by, "due"));
                    addTask(tasks, deadline);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  [D][ ] " + description + deadline.getScheduleDetails());
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else if (command.equals("event") || command.startsWith("event ")) {
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
                    if (fromMarkerIndex <= 6) {
                        throw new PeterException("Please include a description before '/from'.");
                    }
                    if (toMarkerIndex <= fromMarkerIndex + 7) {
                        throw new PeterException("Please include a start date after '/from'.");
                    }
                    String description = command.substring(6, fromMarkerIndex);
                    String from = command.substring(fromMarkerIndex + 7, toMarkerIndex);
                    String to = command.substring(toMarkerIndex + 5);
                    if (description.isBlank()) {
                        throw new PeterException("Please include a description before '/from'.");
                    }
                    if (from.isBlank()) {
                        throw new PeterException("Please include a start date after '/from'.");
                    }
                    if (to.isBlank()) {
                        throw new PeterException("Please include an end date after '/to'.");
                    }
                    validateStorageFields(description, from, to);
                    Event event = new Event(description, parseDate(from, "start"),
                            parseDate(to, "end"));
                    addTask(tasks, event);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  [E][ ] " + description + event.getScheduleDetails());
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    int taskIndex = getTaskIndex(command, "mark", tasks.size());
                    changeTaskStatus(tasks, taskIndex, true);
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  [X] " + tasks.get(taskIndex).getDescription());
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    int taskIndex = getTaskIndex(command, "unmark", tasks.size());
                    changeTaskStatus(tasks, taskIndex, false);
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  [ ] " + tasks.get(taskIndex).getDescription());
                } else if (command.equals("delete") || command.startsWith("delete ")) {
                    int taskIndex = getTaskIndex(command, "delete", tasks.size());
                    Task removedTask = deleteTask(tasks, taskIndex);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  [" + removedTask.getTaskTypeIcon() + "]["
                            + removedTask.getStatusIcon() + "] " + removedTask.getDescription()
                            + removedTask.getScheduleDetails());
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else {
                    throw new PeterException("I'm sorry, but I don't understand that command. Please try again.");
                }
            } catch (PeterException e) {
                System.out.println(e.getMessage());
            }
            System.out.println(divider);
        }
    }

    /**
     * Rejects the delimiter used to separate fields in the data file.
     */
    private static void validateStorageFields(String... fields) throws PeterException {
        for (String field : fields) {
            if (field.contains(" | ")) {
                throw new PeterException("Oh dear!Task details cannot contain ' | '.");
            }
        }
    }

    /**
     * Parses a user-entered date in the application's ISO date format.
     */
    private static LocalDateTime parseDate(String dateText, String dateName) throws PeterException {
        try {
            return ScheduleDateTime.parseUserInput(dateText);
        } catch (DateTimeParseException e) {
            throw new PeterException("Please enter the " + dateName + " date as yyyy-MM-dd"
                    + " or d/M/yyyy HHmm (e.g., 2019-10-15 or 2/12/2019 1800).", e);
        }
    }

    /**
     * Parses the ISO date used by the {@code on} command.
     */
    private static LocalDate parseQueryDate(String dateText) throws PeterException {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            throw new PeterException(
                    "Please enter the date in yyyy-MM-dd format (e.g., 2019-12-02).", e);
        }
    }

    /**
     * Prints scheduled tasks occurring on a date, using their original task
     * numbers so subsequent task commands can refer to them directly.
     */
    private static void printTasksOnDate(ArrayList<Task> tasks, LocalDate date) {
        boolean foundTask = false;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.occursOn(date)) {
                if (!foundTask) {
                    System.out.println("Here are the scheduled tasks on "
                            + ScheduleDateTime.format(date) + ":");
                }
                System.out.println((i + 1) + ".[" + task.getTaskTypeIcon() + "]["
                        + task.getStatusIcon() + "] " + task.getDescription()
                        + task.getScheduleDetails());
                foundTask = true;
            }
        }
        if (!foundTask) {
            System.out.println("There are no scheduled tasks on "
                    + ScheduleDateTime.format(date) + ".");
        }
    }

    /**
     * Adds and saves a task, undoing the addition if saving fails.
     */
    private static void addTask(ArrayList<Task> tasks, Task task) throws PeterException {
        tasks.add(task);
        try {
            Storage.save(tasks);
        } catch (PeterException e) {
            tasks.remove(tasks.size() - 1);
            throw e;
        }
    }

    /**
     * Changes and saves a task status, restoring the old status if saving fails.
     */
    private static void changeTaskStatus(ArrayList<Task> tasks, int taskIndex, boolean isDone)
            throws PeterException {
        Task task = tasks.get(taskIndex);
        boolean previousStatus = task.isDone();
        if (isDone) {
            task.markAsDone();
        } else {
            task.unmarkAsDone();
        }
        try {
            Storage.save(tasks);
        } catch (PeterException e) {
            if (previousStatus) {
                task.markAsDone();
            } else {
                task.unmarkAsDone();
            }
            throw e;
        }
    }

    /**
     * Deletes and saves a task, restoring it at the same position if saving fails.
     */
    private static Task deleteTask(ArrayList<Task> tasks, int taskIndex) throws PeterException {
        Task removedTask = tasks.remove(taskIndex);
        try {
            Storage.save(tasks);
            return removedTask;
        } catch (PeterException e) {
            tasks.add(taskIndex, removedTask);
            throw e;
        }
    }

    private static int getTaskIndex(String command, String action, int taskCount) throws PeterException {
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
                throw new PeterException("Oh dear! Task number must be between 1 and " + taskCount + ".");
            }
            return taskNumber - 1;
        } catch (NumberFormatException e) {
            throw new PeterException("Oh dear! Please enter an integer task number to " + action + ".");
        }
    }
}
