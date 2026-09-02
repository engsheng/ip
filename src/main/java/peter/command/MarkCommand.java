package peter.command;

import peter.Parser;
import peter.PeterException;
import peter.storage.Storage;
import peter.task.TaskList;
import peter.ui.Ui;

/**
 * Marks a task as done or not done. One class covers both directions because
 * they differ only in the status being applied.
 */
public class MarkCommand extends Command {
    private final String command;
    private final boolean isDone;

    /**
     * Creates a command that changes a task's completion status.
     *
     * <p>The command text is kept rather than a task index because validating
     * the task number needs the task count, which is only known when the
     * command runs.
     *
     * @param command complete mark or unmark command entered by the user
     * @param isDone status to apply to the task
     */
    public MarkCommand(String command, boolean isDone) {
        this.command = command;
        this.isDone = isDone;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The task number is validated here rather than during parsing, since
     * the check needs the current task count. A failed save restores the
     * task's previous status, which may already have been the requested one.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PeterException {
        int taskIndex = Parser.parseTaskIndex(command, tasks.size());
        boolean previousStatus = tasks.get(taskIndex).isDone();
        tasks.setDone(taskIndex, isDone);
        saveOrRollback(tasks, storage, () -> tasks.setDone(taskIndex, previousStatus));
        ui.showTaskStatusChange(tasks.get(taskIndex), isDone);
    }
}
