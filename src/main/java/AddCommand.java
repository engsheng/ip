/**
 * Adds a new task to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command that adds an already-parsed task.
     *
     * @param task task to add
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PeterException {
        tasks.add(task);
        saveOrRollback(tasks, storage, () -> tasks.delete(tasks.size() - 1));
        ui.showAddedTask(task, tasks.size());
    }
}
