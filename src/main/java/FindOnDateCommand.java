import java.time.LocalDate;

/**
 * Displays the scheduled tasks occurring on a given date.
 */
public class FindOnDateCommand extends Command {
    private final LocalDate date;

    /**
     * Creates a command that reports the tasks scheduled on a date.
     *
     * @param date date to report on
     */
    public FindOnDateCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTasksOnDate(tasks, date);
    }
}
