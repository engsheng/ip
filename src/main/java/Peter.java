/**
 * Starts the Peter chatbot and prints its initial greeting.
 */
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
        System.out.println("Bye! Hope to see you again.");
        System.out.println(divider);
    }
}
