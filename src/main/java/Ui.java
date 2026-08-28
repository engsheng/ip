import java.util.Scanner;

/**
 * Handles the application's basic console input and output.
 */
public class Ui {
    private static final String DIVIDER =
            "____________________________________________________________";
    private static final String BANNER = " ____      _\n"
            + "|  _ \\ ___| |_ ___ _ __\n"
            + "| |_) / _ \\ __/ _ \\ '__|\n"
            + "|  __/  __/ ||  __/ |\n"
            + "|_|   \\___|\\__\\___|_|\n";

    private final Scanner scanner;

    /**
     * Creates a UI that reads commands from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the application's greeting.
     */
    public void showWelcome() {
        showDivider();
        System.out.print(BANNER);
        System.out.println("Yo! I'm Peter.");
        System.out.println("What crazy adventures are we making today?");
        showDivider();
    }

    /**
     * Returns whether another command is available from the user.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the application's farewell message.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Displays an error message without exposing implementation details.
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays the divider used to separate console interactions.
     */
    public void showDivider() {
        System.out.println(DIVIDER);
    }
}
