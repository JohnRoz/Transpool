package model.CustomExceptions;

public abstract class FormattedMessageException extends Exception {

    protected FormattedMessageException(String message) {
        super(message);
    }

    protected FormattedMessageException(String message, Object... args) {
        super(String.format(message, args));
    }
}
