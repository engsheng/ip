package peter;

import peter.command.Command;
import peter.storage.Storage;
import peter.task.TaskList;
import peter.ui.Ui;

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

        // The extra hasNextCommand check stops the loop when input runs out
        // without a bye command, which happens when input is piped in.
        boolean isExit = false;
        while (!isExit && ui.hasNextCommand()) {
            try {
                String fullCommand = ui.readCommand();
                ui.showDivider();
                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (PeterException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showDivider();
            }
        }
    }

    public static void main(String[] args) {
        new Peter("data/peter.txt").run();
    }
}
