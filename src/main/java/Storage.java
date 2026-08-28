import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads and saves the task list using the application's data file.
 */
public final class Storage {
    private static final Path DATA_FILE = Path.of("data", "peter.txt");
    private static final Path TEMP_DATA_FILE = Path.of("data", "peter.txt.tmp");

    private Storage() {
    }

    /**
     * Loads tasks from the data file, or returns an empty list when the file
     * does not exist yet.
     *
     * @return tasks stored in the data file
     * @throws PeterException if the file cannot be read or contains invalid data
     */
    public static ArrayList<Task> load() throws PeterException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(DATA_FILE)) {
            return tasks;
        }

        try {
            List<String> taskLines = Files.readAllLines(DATA_FILE, StandardCharsets.UTF_8);
            for (int i = 0; i < taskLines.size(); i++) {
                String taskLine = taskLines.get(i);
                if (i == 0 && taskLine.startsWith("\uFEFF")) {
                    taskLine = taskLine.substring(1);
                }
                if (!taskLine.isBlank()) {
                    tasks.add(parseTask(taskLine, i + 1));
                }
            }
        } catch (IOException | SecurityException e) {
            throw new PeterException(
                    "Oh dear! I couldn't read the task data file. Please check that it is accessible.", e);
        }
        return tasks;
    }

    private static Task parseTask(String taskLine, int lineNumber) throws PeterException {
        String[] taskParts = taskLine.split(" \\| ", -1);
        if (taskParts.length < 2 || (!taskParts[1].equals("0") && !taskParts[1].equals("1"))) {
            throw invalidDataException(lineNumber);
        }

        int expectedPartCount = switch (taskParts[0]) {
            case "T" -> 3;
            case "D" -> 4;
            case "E" -> 5;
            default -> throw invalidDataException(lineNumber);
        };
        if (taskParts.length != expectedPartCount) {
            throw invalidDataException(lineNumber);
        }
        for (int i = 2; i < taskParts.length; i++) {
            if (taskParts[i].isBlank()) {
                throw invalidDataException(lineNumber);
            }
        }

        Task task;
        try {
            task = switch (taskParts[0]) {
                case "T" -> new Todo(taskParts[2]);
                case "D" -> new Deadline(taskParts[2], LocalDate.parse(taskParts[3]));
                case "E" -> new Event(taskParts[2], LocalDate.parse(taskParts[3]),
                        LocalDate.parse(taskParts[4]));
                default -> throw invalidDataException(lineNumber);
            };
        } catch (DateTimeParseException e) {
            throw invalidDataException(lineNumber);
        }
        if (taskParts[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    private static PeterException invalidDataException(int lineNumber) {
        return new PeterException("Oh dear! The task data file is invalid at line " + lineNumber
                + ". Please fix or remove it before restarting me!");
    }

    /**
     * Rewrites the data file so that it matches the current task list.
     *
     * @param tasks tasks to save in their current order
     * @throws PeterException if the data directory or file cannot be written
     */
    public static void save(List<Task> tasks) throws PeterException {
        try {
            Files.createDirectories(DATA_FILE.getParent());
            List<String> taskLines = tasks.stream()
                    .map(Task::toDataString)
                    .toList();
            Files.write(TEMP_DATA_FILE, taskLines, StandardCharsets.UTF_8);
            moveTemporaryFileIntoPlace();
        } catch (IOException | SecurityException e) {
            try {
                Files.deleteIfExists(TEMP_DATA_FILE);
            } catch (IOException | SecurityException ignored) {
                // Keep the original save error, which is more useful to the user.
            }
            throw new PeterException(
                    "Oh dear! I couldn't save your tasks. Please check that the data folder is writable.", e);
        }
    }

    /**
     * Replaces the data file atomically when supported by the file system.
     */
    private static void moveTemporaryFileIntoPlace() throws IOException {
        try {
            Files.move(TEMP_DATA_FILE, DATA_FILE,
                    StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(TEMP_DATA_FILE, DATA_FILE, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
