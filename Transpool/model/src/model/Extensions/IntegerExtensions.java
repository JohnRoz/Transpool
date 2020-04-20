package model.Extensions;

public class IntegerExtensions {

    /**
     * Receives a string and checks if it represents an integer.
     * @param strNum The String to check if it represents an integer.
     * @return True if represents an integer. False otherwise.
     */
    public static boolean isIntegerStr(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum.trim());
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Checks if a string represents an integer and if so, returns its numeric value.
     * @param strNum The String to try to parse to an int.
     * @return If {@code strNum} is an integer, return its value. Otherwise null.
     */
    public static Integer tryParseInt(String strNum) {
        String trimmedStr = strNum.trim();

        if (isIntegerStr(trimmedStr))
            return Integer.parseInt(trimmedStr);
        return null;
    }
}
