package model.CustomExceptions;

public class UnsupportedFileTypeException extends FormattedMessageException{

    public UnsupportedFileTypeException(String message) {
        super(message);
    }

    public UnsupportedFileTypeException(String message, Object... args) {
        super(message, args);
    }
}
