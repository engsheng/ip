import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads and saves the task list using the application's data file.
 */
public final class Storage {
    private static final Path DATA_FILE = Path.of("data", "peter.txt");

    private Storage() {
    }

    /**
     * Loads tasks from the data file, or returns an empty list when the file
     * does not exist yet.
     *
     * @return tasks stored in the data file
     * @throws IOException if the data file cannot be read
     */
    public static ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(DATA_FILE)) {
            return tasks;
        }

        for (String taskLine : Files.readAllLines(DATA_FILE, StandardCharsets.UTF_8)) {
            String[] taskParts = taskLine.split(" \\| ", -1);
            Task task = switch (taskParts[0]) {
                case "T" -> new Todo(taskParts[2]);
                case "D" -> new Deadline(taskParts[2], taskParts[3]);
                case "E" -> new Event(taskParts[2], taskParts[3], taskParts[4]);
                default -> throw new IllegalArgumentException("Unknown task type: " + taskParts[0]);
            };
            if (taskParts[1].equals("1")) {
                task.markAsDone();
            }
            tasks.add(task);
        }
        return tasks;
    }

    /**
     * Rewrites the data file so that it matches the current task list.
     *
     * @param tasks tasks to save in their current order
     * @throws IOException if the data directory or file cannot be written
     */
    public static void save(List<Task> tasks) throws IOException {
        Files.createDirectories(DATA_FILE.getParent());
        List<String> taskLines = tasks.stream()
                .map(Task::toDataString)
                .toList();
        Files.write(DATA_FILE, taskLines, StandardCharsets.UTF_8);
    }
}
