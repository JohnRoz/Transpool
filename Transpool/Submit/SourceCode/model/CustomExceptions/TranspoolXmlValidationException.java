package model.CustomExceptions;

public final class TranspoolXmlValidationException extends FormattedMessageException{
    private static final String EXCEPTION_GENERAL_MESSAGE = "Loading of the .xml file failed.\n";

    public TranspoolXmlValidationException(String message) {
        super(EXCEPTION_GENERAL_MESSAGE + message);
    }

    public TranspoolXmlValidationException(String message, Object... args) {
        super(EXCEPTION_GENERAL_MESSAGE + message, args);
    }
}
