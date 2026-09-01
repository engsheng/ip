/**
 * Coordinates the task list, storage, and console UI for the chatbot.
 */
public class Peter {
    private final Storage storage;
    private final Ui ui;
    private final TaskList tasks;

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

        TaskList loadedTasks;
        PeterException loadError = null;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (PeterException e) {
            loadedTasks = new TaskList();
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
                    ui.showTaskList(tasks);
                    break;
                case "on":
                    ui.showTasksOnDate(tasks, Parser.parseQueryDate(command));
                    break;
                case "todo", "deadline", "event":
                    Task task = Parser.parseTask(command);
                    addTask(task);
                    ui.showAddedTask(task, tasks.size());
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
     * Parses, applies, and reports a mark or unmark command.
     */
    private void updateTaskStatus(String command, boolean isDone) throws PeterException {
        int taskIndex = Parser.parseTaskIndex(command, tasks.size());
        changeTaskStatus(taskIndex, isDone);
        ui.showTaskStatusChange(tasks.get(taskIndex), isDone);
    }

    /**
     * Parses, applies, and reports a delete command.
     */
    private void removeTask(String command) throws PeterException {
        int taskIndex = Parser.parseTaskIndex(command, tasks.size());
        Task removedTask = deleteTask(taskIndex);
        ui.showRemovedTask(removedTask, tasks.size());
    }

    /**
     * Adds and saves a task, undoing the addition if saving fails.
     */
    private void addTask(Task task) throws PeterException {
        tasks.add(task);
        try {
            storage.save(tasks.asList());
        } catch (PeterException e) {
            tasks.delete(tasks.size() - 1);
            throw e;
        }
    }

    /**
     * Changes and saves a task status, restoring the old status if saving fails.
     */
    private void changeTaskStatus(int taskIndex, boolean isDone) throws PeterException {
        Task task = tasks.get(taskIndex);
        boolean previousStatus = task.isDone();
        tasks.setDone(taskIndex, isDone);
        try {
            storage.save(tasks.asList());
        } catch (PeterException e) {
            tasks.setDone(taskIndex, previousStatus);
            throw e;
        }
    }

    /**
     * Deletes and saves a task, restoring it at the same position if saving fails.
     */
    private Task deleteTask(int taskIndex) throws PeterException {
        Task removedTask = tasks.delete(taskIndex);
        try {
            storage.save(tasks.asList());
            return removedTask;
        } catch (PeterException e) {
            tasks.add(taskIndex, removedTask);
            throw e;
        }
    }

}
