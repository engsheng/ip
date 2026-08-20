import java.util.Scanner;

public class Peter {
    public static void main(String[] args) {
        String divider = "____________________________________________________________";
        String banner = " ____      _            \n"
                + "|  _ \\ ___| |_ ___ _ __ \n"
                + "| |_) / _ \\ __/ _ \\ '__|\n"
                + "|  __/  __/ ||  __/ |   \n"
                + "|_|   \\___|\\__\\___|_|   \n";

        System.out.println(divider);
        System.out.print(banner);
        System.out.println("Yo! I'm Peter.");
        System.out.println("What crazy adventures are we making today?");
        System.out.println(divider);

        Scanner scanner = new Scanner(System.in);
        String[] tasks = new String[100];
        boolean[] isDone = new boolean[100];
        int taskCount = 0;
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(divider);
            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(divider);
                break;
            } else if (command.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    String status = isDone[i] ? "[X]" : "[ ]";
                    System.out.println((i + 1) + "." + status + " " + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5));
                int taskIndex = taskNumber - 1;
                isDone[taskIndex] = true;
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  [X] " + tasks[taskIndex]);
            } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7));
                int taskIndex = taskNumber - 1;
                isDone[taskIndex] = false;
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  [ ] " + tasks[taskIndex]);
            } else {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println("added: " + command);
            }
            System.out.println(divider);
        }
    }
}
