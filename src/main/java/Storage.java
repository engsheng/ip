import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Saves the current task list to the application's data file.
 */
public final class Storage {
    private static final Path DATA_FILE = Path.of("data", "peter.txt");

    private Storage() {
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
