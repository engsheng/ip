package peter.command;

import peter.storage.Storage;
import peter.task.TaskList;
import peter.ui.Ui;

/**
 * Displays the tasks whose descriptions contain a keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a command that reports the tasks matching a keyword.
     *
     * @param keyword keyword to search descriptions for.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Reads the task list without changing it, so nothing is saved and no
     * rollback is needed.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMatchingTasks(tasks, keyword);
    }
}
