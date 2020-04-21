package model.CustomExceptions;

public class StationDoesNotExistException extends FormattedMessageException{
    public StationDoesNotExistException(String message) {
        super(message);
    }

    public StationDoesNotExistException(String message, Object... args) {
        super(message, args);
    }
}
