package model.CustomExceptions;

public class RoadDoesNotExistException extends FormattedMessageException {

    private static final String EXCEPTION_MSG = "There is no road from %s to %s.";

    public RoadDoesNotExistException(String nonExistentRoadSrc, String nonExistentRoadDst) {
        super(EXCEPTION_MSG, nonExistentRoadSrc, nonExistentRoadDst);
    }

    public RoadDoesNotExistException(String message) {
        super(message);
    }

    public RoadDoesNotExistException(String message, Object... args) {
        super(message, args);
    }
}
