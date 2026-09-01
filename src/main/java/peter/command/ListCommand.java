package peter.command;

import peter.storage.Storage;
import peter.task.TaskList;
import peter.ui.Ui;

/**
 * Displays every task in the task list.
 */
public class ListCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
