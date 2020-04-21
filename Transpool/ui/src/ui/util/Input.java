package ui.util;

import model.Enums.UserAction;
import model.Extensions.IntegerExtensions;

import java.util.Scanner;

public class Input {
    public static String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * Reads from the console, check the input to be a valid {@link UserAction}
     * @return The UserAction if input is valid. Null otherwise.
     */
    public static UserAction getUserActionInput() {
        Scanner scanner = new Scanner(System.in);
        String userActionNum = getUserInput();
        Integer parsedInt = IntegerExtensions.tryParseInt(userActionNum);
        if (parsedInt != null) {
            int shiftedToZeroBased = parsedInt - 1;

            if (UserAction.isValueInRange(shiftedToZeroBased))
                return UserAction.values()[shiftedToZeroBased];
        }

        return null;
    }
}
