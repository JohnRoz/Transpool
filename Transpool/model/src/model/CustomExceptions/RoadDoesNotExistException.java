package model.CustomExceptions;

public class RoadDoesNotExistException extends FormattedMessageException {
    public RoadDoesNotExistException(String message) {
        super(message);
    }

    public RoadDoesNotExistException(String message, Object... args) {
        super(message, args);
    }
}
