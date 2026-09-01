package peter.command;

import peter.storage.Storage;
import peter.task.TaskList;
import peter.ui.Ui;

/**
 * Says goodbye and ends the program.
 */
public class ExitCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
