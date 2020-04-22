package model.CustomExceptions;

public class UserAlreadyExistsException extends FormattedMessageException {
    private static final String EXCEPTION_MSG = "A user with the name %s already exists.";

    public UserAlreadyExistsException(String username) {
        super(EXCEPTION_MSG, username);
    }

    public UserAlreadyExistsException(String message, Object... args) {
        super(message, args);
    }
}
