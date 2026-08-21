import java.util.ArrayList;
import java.util.Scanner;

public class Peter {
    public static void main(String[] args) {
        String divider = "____________________________________________________________";
        String banner = " ____      _\n"
                + "|  _ \\ ___| |_ ___ _ __\n"
                + "| |_) / _ \\ __/ _ \\ '__|\n"
                + "|  __/  __/ ||  __/ |\n"
                + "|_|   \\___|\\__\\___|_|\n";

        System.out.println(divider);
        System.out.print(banner);
        System.out.println("Yo! I'm Peter.");
        System.out.println("What crazy adventures are we making today?");
        System.out.println(divider);

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(divider);
            try {
                if (command.equals("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    System.out.println(divider);
                    break;
                } else if (command.equals("list")) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        Task task = tasks.get(i);
                        System.out.println((i + 1) + ".[" + task.getTaskTypeIcon() + "]["
                                + task.getStatusIcon() + "] "
                                + task.getDescription() + task.getScheduleDetails());
                    }
                } else if (command.equals("todo") || command.startsWith("todo ")) {
                    String description = command.length() == 4 ? "" : command.substring(5);
                    if (description.isBlank()) {
                        throw new PeterException("Please include a description after 'todo'.");
                    }
                    tasks.add(new Todo(description));
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  [T][ ] " + description);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                    int byMarkerIndex = command.indexOf(" /by ");
                    if (byMarkerIndex == -1) {
                        if (command.endsWith(" /by")) {
                            throw new PeterException("Please include a due date after '/by'.");
                        }
                        throw new PeterException("Use 'deadline <description> /by <date>'.");
                    }
                    if (byMarkerIndex <= 9) {
                        throw new PeterException("Please include a description before '/by'.");
                    }
                    String description = command.substring(9, byMarkerIndex);
                    String by = command.substring(byMarkerIndex + 5);
                    // To handle cases where the user enters blank description or blank due date
                    if (description.isBlank()) {
                        throw new PeterException("Please include a description before '/by'.");
                    }
                    if (by.isBlank()) {
                        throw new PeterException("Please include a due date after '/by'.");
                    }
                    tasks.add(new Deadline(description, by));
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  [D][ ] " + description + " (by: " + by + ")");
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else if (command.equals("event") || command.startsWith("event ")) {
                    int fromMarkerIndex = command.indexOf(" /from ");
                    int toMarkerIndex = command.indexOf(" /to ");
                    if (fromMarkerIndex == -1 || toMarkerIndex == -1 || fromMarkerIndex >= toMarkerIndex) {
                        if (command.endsWith(" /from")) {
                            throw new PeterException("Please include a start time after '/from'.");
                        }
                        if (command.endsWith(" /to")) {
                            throw new PeterException("Please include an end time after '/to'.");
                        }
                        throw new PeterException("Use 'event <description> /from <start> /to <end>'.");
                    }
                    if (fromMarkerIndex <= 6) {
                        throw new PeterException("Please include a description before '/from'.");
                    }
                    if (toMarkerIndex <= fromMarkerIndex + 7) {
                        throw new PeterException("Please include a start time after '/from'.");
                    }
                    String description = command.substring(6, fromMarkerIndex);
                    String from = command.substring(fromMarkerIndex + 7, toMarkerIndex);
                    String to = command.substring(toMarkerIndex + 5);
                    if (description.isBlank()) {
                        throw new PeterException("Please include a description before '/from'.");
                    }
                    if (from.isBlank()) {
                        throw new PeterException("Please include a start time after '/from'.");
                    }
                    if (to.isBlank()) {
                        throw new PeterException("Please include an end time after '/to'.");
                    }
                    tasks.add(new Event(description, from, to));
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  [E][ ] " + description + " (from: " + from + " to: " + to + ")");
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    int taskIndex = getTaskIndex(command, "mark", tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  [X] " + tasks.get(taskIndex).getDescription());
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    int taskIndex = getTaskIndex(command, "unmark", tasks.size());
                    tasks.get(taskIndex).unmarkAsDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  [ ] " + tasks.get(taskIndex).getDescription());
                } else {
                    throw new PeterException("I'm sorry, but I don't understand that command. Please try again.");
                }
            } catch (PeterException e) {
                System.out.println(e.getMessage());
            }
            System.out.println(divider);
        }
    }

    private static int getTaskIndex(String command, String action, int taskCount) throws PeterException {
        String taskNumberText = command.substring(action.length()).trim();
        if (taskNumberText.isEmpty()) {
            throw new PeterException("Please provide a task number to " + action + ".");
        }
        if (taskCount == 0) {
            throw new PeterException("There are no tasks to " + action + ".");
        }

        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new PeterException("Task number must be between 1 and " + taskCount + ".");
            }
            return taskNumber - 1;
        } catch (NumberFormatException e) {
            throw new PeterException("Please enter an integer task number to " + action + ".");
        }
    }
}
