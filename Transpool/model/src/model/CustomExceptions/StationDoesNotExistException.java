package model.CustomExceptions;

public class StationDoesNotExistException extends FormattedMessageException{
    private static final String EXCEPTION_MSG_SPECIFYING_COLL = "Station named %s does not exist in collection %s.";
    private static final String EXCEPTION_MSG = "There is no station named %s.";

    public StationDoesNotExistException(String nonExistentStation) {
        super(EXCEPTION_MSG, nonExistentStation);
    }

    public StationDoesNotExistException(String nonExistentStation, String stationsCollection) {
        super(EXCEPTION_MSG_SPECIFYING_COLL, nonExistentStation, stationsCollection);
    }

    public StationDoesNotExistException(String message, Object... args) {
        super(message, args);
    }
}
