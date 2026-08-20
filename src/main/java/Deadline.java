public class Deadline extends Task {
    private final String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String getTaskTypeIcon() {
        return "D";
    }

    @Override
    public String getScheduleDetails() {
        return " (by: " + by + ")";
    }
}
