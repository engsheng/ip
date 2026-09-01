package peter.ui;

import java.time.LocalDate;
import java.util.Scanner;
import peter.task.ScheduleDateTime;
import peter.task.Task;
import peter.task.TaskList;

/**
 * Handles the application's basic console input and output.
 */
public class Ui {
    private static final String DIVIDER =
            "____________________________________________________________";
    private static final String BANNER = " ____      _\n"
            + "|  _ \\ ___| |_ ___ _ __\n"
            + "| |_) / _ \\ __/ _ \\ '__|\n"
            + "|  __/  __/ ||  __/ |\n"
            + "|_|   \\___|\\__\\___|_|\n";

    private final Scanner scanner;

    /**
     * Creates a UI that reads commands from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the application's greeting.
     */
    public void showWelcome() {
        showDivider();
        System.out.print(BANNER);
        System.out.println("Yo! I'm Peter.");
        System.out.println("What crazy adventures are we making today?");
        showDivider();
    }

    /**
     * Returns whether another command is available from the user.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the application's farewell message.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Displays an error message without exposing implementation details.
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays the divider used to separate console interactions.
     */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /**
     * Displays every task with its one-based list number.
     *
     * @param tasks tasks to display
     */
    public void showTaskList(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            showNumberedTask(i, tasks.get(i));
        }
    }

    /**
     * Confirms that a task was added and reports the new task count.
     *
     * @param task task that was added
     * @param taskCount number of tasks after the addition
     */
    public void showAddedTask(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + formatTask(task));
        showTaskCount(taskCount);
    }

    /**
     * Confirms that a task was removed and reports the new task count.
     *
     * @param task task that was removed
     * @param taskCount number of tasks after the removal
     */
    public void showRemovedTask(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + formatTask(task));
        showTaskCount(taskCount);
    }

    /**
     * Confirms a change to a task's completion status.
     *
     * @param task task whose status changed
     * @param isDone new completion status
     */
    public void showTaskStatusChange(Task task, boolean isDone) {
        if (isDone) {
            System.out.println("Nice! I've marked this task as done:");
        } else {
            System.out.println("OK, I've marked this task as not done yet:");
        }
        System.out.println("  [" + task.getStatusIcon() + "] " + task.getDescription());
    }

    /**
     * Displays the scheduled tasks occurring on a date, keeping their original
     * task numbers so subsequent task commands can refer to them directly.
     *
     * @param tasks tasks to search
     * @param date date to report on
     */
    public void showTasksOnDate(TaskList tasks, LocalDate date) {
        boolean foundTask = false;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.occursOn(date)) {
                if (!foundTask) {
                    System.out.println("Here are the scheduled tasks on "
                            + ScheduleDateTime.format(date) + ":");
                }
                showNumberedTask(i, task);
                foundTask = true;
            }
        }
        if (!foundTask) {
            System.out.println("There are no scheduled tasks on "
                    + ScheduleDateTime.format(date) + ".");
        }
    }

    /** Displays a task prefixed by its one-based list number. */
    private void showNumberedTask(int index, Task task) {
        System.out.println((index + 1) + "." + formatTask(task));
    }

    /** Reports how many tasks the list now holds. */
    private void showTaskCount(int taskCount) {
        System.out.println("Now you have " + taskCount + " tasks in the list.");
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
