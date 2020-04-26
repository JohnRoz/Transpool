package model.Enums;

public enum RepetitionRate {
    OneTime,
    Daily,
    BiDaily,
    Weekly,
    Monthly;

    public static boolean isValueInRange(int val) {
        return val >= 0 && val < UserAction.values().length;
    }

    public static int getValuesCount() {
        return UserAction.values().length;
    }
}
