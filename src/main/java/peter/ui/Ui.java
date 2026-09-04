package peter.ui;

import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Scanner;

import peter.task.ScheduleDateTime;
import peter.task.Task;
import peter.task.TaskList;

/**
 * Handles the application's basic input and output.
 *
 * <p>Output goes to an injected stream rather than directly to
 * {@code System.out}, so a caller that needs the text of a response can
 * supply a buffer instead of the console.
 */
public class Ui {
    private static final String DIVIDER =
            "____________________________________________________________";
    private static final String BANNER = " ____      _\n"
            + "|  _ \\ ___| |_ ___ _ __\n"
            + "| |_) / _ \\ __/ _ \\ '__|\n"
            + "|  __/  __/ ||  __/ |\n"
            + "|_|   \\___|\\__\\___|_|\n";

    private final PrintStream out;

    /** Created on first use, since a UI that only writes never reads a command. */
    private Scanner scanner;

    /**
     * Creates a UI that reads commands from standard input and writes to
     * standard output.
     */
    public Ui() {
        this(System.out);
    }

    /**
     * Creates a UI that writes to the given stream.
     *
     * @param out stream to write all output to.
     */
    public Ui(PrintStream out) {
        this.out = out;
    }

    /**
     * Displays the application's full console welcome, banner included.
     */
    public void showWelcome() {
        showDivider();
        out.print(BANNER);
        showGreeting();
        showDivider();
    }

    /**
     * Displays the greeting on its own, without the banner or dividers that
     * only suit a console.
     */
    public void showGreeting() {
        showLines("Yo! I'm Peter.",
                "What crazy adventures are we making today?");
    }

    /**
     * Returns whether another command is available from the user.
     */
    public boolean hasNextCommand() {
        return getScanner().hasNextLine();
    }

    /**
     * Reads the next command entered by the user.
     */
    public String readCommand() {
        return getScanner().nextLine();
    }

    /** Returns the scanner over standard input, creating it if needed. */
    private Scanner getScanner() {
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        return scanner;
    }

    /**
     * Displays the application's farewell message.
     */
    public void showGoodbye() {
        out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Displays an error message without exposing implementation details.
     */
    public void showError(String message) {
        out.println(message);
    }

    /**
     * Displays the divider used to separate console interactions.
     */
    public void showDivider() {
        out.println(DIVIDER);
    }

    /**
     * Displays each of the given lines in order, one line per row.
     *
     * <p>Var-args keeps a multi-line message readable as a single call at the
     * call site, instead of a run of separate print statements.
     */
    private void showLines(String... lines) {
        for (String line : lines) {
            out.println(line);
        }
    }

    /**
     * Displays every task with its one-based list number.
     *
     * @param tasks tasks to display.
     */
    public void showTaskList(TaskList tasks) {
        out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            showNumberedTask(i, tasks.get(i));
        }
    }

    /**
     * Confirms that a task was added and reports the new task count.
     *
     * @param task task that was added.
     * @param taskCount number of tasks after the addition.
     */
    public void showAddedTask(Task task, int taskCount) {
        showLines("Got it. I've added this task:",
                "  " + formatTask(task));
        showTaskCount(taskCount);
    }

    /**
     * Confirms that a task was removed and reports the new task count.
     *
     * @param task task that was removed.
     * @param taskCount number of tasks after the removal.
     */
    public void showRemovedTask(Task task, int taskCount) {
        showLines("Noted. I've removed this task:",
                "  " + formatTask(task));
        showTaskCount(taskCount);
    }

    /**
     * Confirms a change to a task's completion status.
     *
     * @param task task whose status changed.
     * @param isDone new completion status.
     */
    public void showTaskStatusChange(Task task, boolean isDone) {
        if (isDone) {
            out.println("Nice! I've marked this task as done:");
        } else {
            out.println("OK, I've marked this task as not done yet:");
        }
        out.println("  [" + task.getStatusIcon() + "] " + task.getDescription());
    }

    /**
     * Displays the scheduled tasks occurring on a date, keeping their original
     * task numbers so subsequent task commands can refer to them directly.
     *
     * @param tasks tasks to search.
     * @param date date to report on.
     */
    public void showTasksOnDate(TaskList tasks, LocalDate date) {
        boolean hasFoundTask = false;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.occursOn(date)) {
                if (!hasFoundTask) {
                    out.println("Here are the scheduled tasks on "
                            + ScheduleDateTime.format(date) + ":");
                }
                showNumberedTask(i, task);
                hasFoundTask = true;
            }
        }
        if (!hasFoundTask) {
            out.println("There are no scheduled tasks on "
                    + ScheduleDateTime.format(date) + ".");
        }
    }

    /**
     * Displays the tasks whose descriptions contain a keyword, keeping their
     * original task numbers so subsequent task commands can refer to them
     * directly.
     *
     * @param tasks tasks to search.
     * @param keyword keyword to search descriptions for.
     */
    public void showMatchingTasks(TaskList tasks, String keyword) {
        boolean hasFoundTask = false;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.hasKeyword(keyword)) {
                if (!hasFoundTask) {
                    out.println("Here are the matching tasks in your list:");
                }
                showNumberedTask(i, task);
                hasFoundTask = true;
            }
        }
        if (!hasFoundTask) {
            out.println("There are no matching tasks in your list.");
        }
    }

    /** Displays a task prefixed by its one-based list number. */
    private void showNumberedTask(int index, Task task) {
        out.println((index + 1) + "." + formatTask(task));
    }

    /** Reports how many tasks the list now holds. */
    private void showTaskCount(int taskCount) {
        out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Renders a task as its type icon, status icon, description, and schedule,
     * which is the form used wherever a full task is displayed.
     */
    private String formatTask(Task task) {
        return "[" + task.getTaskTypeIcon() + "][" + task.getStatusIcon() + "] "
                + task.getDescription() + task.getScheduleDetails();
    }
}
