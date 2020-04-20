package ui;

import model.Enums.UserAction;
import model.Extensions.IntegerExtensions;

import java.util.Scanner;

public class Input {
    private static Scanner scanner = new Scanner(System.in);

    public static String getUserInput() {
        return scanner.nextLine();
    }

    /**
     * Reads from the console, check the input to be a valid {@link UserAction}
     * @return The UserAction if input is valid. Null otherwise.
     */
    public static UserAction getUserActionInput() {
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
