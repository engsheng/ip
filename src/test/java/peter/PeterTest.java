package peter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit tests for the string-returning API that a graphical interface uses.
 *
 * <p>These cover the parts the console loop never exercises: that a response
 * comes back as text, that an invalid command is reported in that text rather
 * than thrown, and that the caller can tell when the user asked to exit.
 *
 * <p>{@link TempDir} keeps each test's data file out of the user's real one.
 */
public class PeterTest {

    @TempDir
    private Path temporaryDirectory;

    private Peter peter;

    @BeforeEach
    public void createChatbot() {
        peter = new Peter(temporaryDirectory.resolve("peter.txt").toString());
    }

    @Test
    public void getGreeting_freshStart_greetingWithoutBannerReturned() {
        assertEquals("Yo! I'm Peter.\nWhat crazy adventures are we making today?",
                peter.getGreeting().replace("\r\n", "\n"));
    }

    @Test
    public void getResponse_addThenList_taskListReturned() {
        peter.getResponse("todo read book");

        assertEquals("Here are the tasks in your list:\n1.[T][ ] read book",
                peter.getResponse("list").replace("\r\n", "\n"));
    }

    @Test
    public void getResponse_unknownCommand_errorMessageReturned() {
        assertEquals("I'm sorry, but I don't understand that command. Please try again.",
                peter.getResponse("blah"));
    }

    @Test
    public void isExitRequested_byeCommand_trueReturned() {
        peter.getResponse("list");
        assertFalse(peter.isExitRequested());

        peter.getResponse("bye");
        assertTrue(peter.isExitRequested());
    }

    @Test
    public void hasLoadingError_missingDataFile_falseReturned() {
        assertFalse(peter.hasLoadingError());
        assertEquals("", peter.getLoadingErrorMessage());
    }
}
