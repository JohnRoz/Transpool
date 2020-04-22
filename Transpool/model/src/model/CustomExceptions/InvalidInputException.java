package model.CustomExceptions;

public class InvalidInputException extends FormattedMessageException {
    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Object... args) {
        super(message, args);
    }
}
