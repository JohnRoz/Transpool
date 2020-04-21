package model.CustomExceptions;

public class RoadDoesNotExistException extends FormattedMessageException {
    protected RoadDoesNotExistException(String message) {
        super(message);
    }

    protected RoadDoesNotExistException(String message, Object... args) {
        super(message, args);
    }
}
