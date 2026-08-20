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
        Task[] tasks = new Task[100];
        int taskCount = 0;
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
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + ".[" + tasks[i].getTaskTypeIcon() + "]["
                                + tasks[i].getStatusIcon() + "] "
                                + tasks[i].getDescription() + tasks[i].getScheduleDetails());
                    }
                } else if (command.equals("todo") || command.startsWith("todo ")) {
                    String description = command.length() == 4 ? "" : command.substring(5);
                    if (description.isBlank()) {
                        throw new PeterException("Please include a description after 'todo'.");
                    }
                    tasks[taskCount] = new Todo(description);
                    taskCount++;
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  [T][ ] " + description);
                    System.out.println("Now you have " + taskCount + " tasks in the list.");
                } else if (command.startsWith("deadline ")) {
                    int byMarkerIndex = command.indexOf(" /by ");
                    String description = command.substring(9, byMarkerIndex);
                    String by = command.substring(byMarkerIndex + 5);
                    tasks[taskCount] = new Deadline(description, by);
                    taskCount++;
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  [D][ ] " + description + " (by: " + by + ")");
                    System.out.println("Now you have " + taskCount + " tasks in the list.");
                } else if (command.startsWith("event ")) {
                    int fromMarkerIndex = command.indexOf(" /from ");
                    int toMarkerIndex = command.indexOf(" /to ");
                    String description = command.substring(6, fromMarkerIndex);
                    String from = command.substring(fromMarkerIndex + 7, toMarkerIndex);
                    String to = command.substring(toMarkerIndex + 5);
                    tasks[taskCount] = new Event(description, from, to);
                    taskCount++;
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  [E][ ] " + description + " (from: " + from + " to: " + to + ")");
                    System.out.println("Now you have " + taskCount + " tasks in the list.");
                } else if (command.startsWith("mark ")) {
                    int taskNumber = Integer.parseInt(command.substring(5));
                    int taskIndex = taskNumber - 1;
                    tasks[taskIndex].markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  [X] " + tasks[taskIndex].getDescription());
                } else if (command.startsWith("unmark ")) {
                    int taskNumber = Integer.parseInt(command.substring(7));
                    int taskIndex = taskNumber - 1;
                    tasks[taskIndex].unmarkAsDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  [ ] " + tasks[taskIndex].getDescription());
                } else {
                    throw new PeterException("I'm sorry, but I don't understand that command. Please try again.");
                }
            } catch (PeterException e) {
                System.out.println(e.getMessage());
            }
            System.out.println(divider);
        }
    }
}
