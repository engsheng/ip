package peter.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import peter.Peter;

/**
 * Controller for the main window: sends what the user types to the chatbot and
 * displays the reply.
 *
 * <p>Each transcript entry is a plain {@link Label} for now, so that this stage
 * proves the wiring between the window and the chatbot before any styling is
 * added.
 *
 * <p>This class does not extend the root's type. The FXML names a concrete
 * root, so the loader builds that root itself and creates this controller
 * separately; extending it would only produce a second, unused pane.
 */
public class MainWindow {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    private Peter peter;

    /**
     * Prepares the window once the loader has injected the controls.
     *
     * <p>This work cannot happen in the constructor, where every {@code @FXML}
     * field is still null.
     */
    @FXML
    public void initialize() {
        // Following the container's height keeps the newest message in view
        // without any manual scrolling.
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    public void setPeter(Peter peter) {
        this.peter = peter;
    }

    /**
     * Adds the user's command and the chatbot's reply to the transcript, then
     * clears the input field ready for the next command.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank()) {
            return;
        }

        addMessage("You: " + input);
        addMessage("Peter: " + peter.getResponse(input));
        userInput.clear();
    }

    /** Appends one wrapped line of text to the transcript. */
    private void addMessage(String text) {
        Label message = new Label(text);
        message.setWrapText(true);
        dialogContainer.getChildren().add(message);
    }
}
