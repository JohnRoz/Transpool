package model.Enums;

public enum UserAction {
    READ_XML_FILE,
    POST_TRIP_REQUEST,
    GET_ALL_TRIP_OFFERS,
    GET_ALL_TRIP_REQUESTS,
    MATCH_TRIP_REQUEST_TO_OFFER,
    EXIT;

    public static boolean isValueInRange(int val) {
        return val >= 0 && val < UserAction.values().length;
    }

    public static int getValuesCount() {
        return UserAction.values().length;
    }
}
