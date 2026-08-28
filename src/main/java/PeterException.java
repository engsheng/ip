/**
 * Represents an error that can be explained clearly to the user.
 */
public class PeterException extends Exception {
    public PeterException(String message) {
        super(message);
    }

    public PeterException(String message, Throwable cause) {
        super(message, cause);
    }
}
