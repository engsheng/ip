import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Coordinates the task list, storage, and console UI for the chatbot.
 */
public class Peter {
    private final Storage storage;
    private final Ui ui;
    private final ArrayList<Task> tasks;

    /** Loading failure retained so it can be displayed after the welcome message. */
    private final PeterException loadingError;

    /**
     * Creates the chatbot and loads tasks from the given data file.
     *
     * @param filePath path to the task data file
     */
    public Peter(String filePath) {
        this.storage = new Storage(filePath);
        this.ui = new Ui();

        ArrayList<Task> loadedTasks;
        PeterException loadError = null;
        try {
            loadedTasks = storage.load();
        } catch (PeterException e) {
            loadedTasks = new ArrayList<>();
            loadError = e;
        }
        this.tasks = loadedTasks;
        this.loadingError = loadError;
    }

    /**
     * Starts the console interaction loop.
     */
    public void run() {
        ui.showWelcome();
        if (loadingError != null) {
            ui.showError(loadingError.getMessage());
            ui.showDivider();
            return;
        }

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            ui.showDivider();
            try {
                switch (Parser.getCommandWord(command)) {
                case "bye":
                    ui.showGoodbye();
                    ui.showDivider();
                    return;
                case "list":
                    showTaskList();
                    break;
                case "on":
                    printTasksOnDate(tasks, Parser.parseQueryDate(command));
                    break;
                case "todo", "deadline", "event":
                    Task task = Parser.parseTask(command);
                    addTask(tasks, task);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  [" + task.getTaskTypeIcon() + "][ ] "
                            + task.getDescription() + task.getScheduleDetails());
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    break;
                case "mark":
                    updateTaskStatus(command, true);
                    break;
                case "unmark":
                    updateTaskStatus(command, false);
                    break;
                case "delete":
                    removeTask(command);
                    break;
                default:
                    throw new AssertionError("Parser returned an unsupported command");
                }
            } catch (PeterException e) {
                ui.showError(e.getMessage());
            }
            ui.showDivider();
        }
    }

    public static void main(String[] args) {
        new Peter("data/peter.txt").run();
    }

    /**
     * Displays every task with its one-based list number.
     */
    private void showTaskList() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println((i + 1) + ".[" + task.getTaskTypeIcon() + "]["
                    + task.getStatusIcon() + "] "
                    + task.getDescription() + task.getScheduleDetails());
        }
    }

    /**
     * Parses, applies, and reports a mark or unmark command.
     */
    private void updateTaskStatus(String command, boolean isDone) throws PeterException {
        int taskIndex = Parser.parseTaskIndex(command, tasks.size());
        changeTaskStatus(tasks, taskIndex, isDone);
        if (isDone) {
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  [X] " + tasks.get(taskIndex).getDescription());
        } else {
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  [ ] " + tasks.get(taskIndex).getDescription());
        }
    }

    /**
     * Parses, applies, and reports a delete command.
     */
    private void removeTask(String command) throws PeterException {
        int taskIndex = Parser.parseTaskIndex(command, tasks.size());
        Task removedTask = deleteTask(tasks, taskIndex);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  [" + removedTask.getTaskTypeIcon() + "]["
                + removedTask.getStatusIcon() + "] " + removedTask.getDescription()
                + removedTask.getScheduleDetails());
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
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
    private void addTask(ArrayList<Task> tasks, Task task) throws PeterException {
        tasks.add(task);
        try {
            storage.save(tasks);
        } catch (PeterException e) {
            tasks.remove(tasks.size() - 1);
            throw e;
        }
    }

    /**
     * Changes and saves a task status, restoring the old status if saving fails.
     */
    private void changeTaskStatus(ArrayList<Task> tasks, int taskIndex, boolean isDone)
            throws PeterException {
        Task task = tasks.get(taskIndex);
        boolean previousStatus = task.isDone();
        if (isDone) {
            task.markAsDone();
        } else {
            task.unmarkAsDone();
        }
        try {
            storage.save(tasks);
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
    private Task deleteTask(ArrayList<Task> tasks, int taskIndex) throws PeterException {
        Task removedTask = tasks.remove(taskIndex);
        try {
            storage.save(tasks);
            return removedTask;
        } catch (PeterException e) {
            tasks.add(taskIndex, removedTask);
            throw e;
        }
    }

}
