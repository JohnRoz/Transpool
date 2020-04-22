package ui.util;

import model.CustomExceptions.InvalidInputException;
import model.Enums.UserAction;
import model.Extensions.IntegerExtensions;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Input {

    private static final Scanner scanner = new Scanner(System.in);

    public static String getUserInput() {
        return scanner.nextLine();
    }

    /**
     * Reads from the console, check the input to be a valid {@link UserAction}
     *
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

    public static int getIntInput() throws InvalidInputException {
        String userInput = getUserInput();
        Integer parsedToInt = IntegerExtensions.tryParseInt(userInput);

        if (parsedToInt == null)
            throw new InvalidInputException(
                    "Cannot parse input '%s' to a number.",
                    userInput
            );

        return parsedToInt;
    }

    public static String getNonEmptyStringInput() throws InvalidInputException {
        String userInput = getUserInput();

        if (userInput == null || userInput.isEmpty())
            throw new InvalidInputException("Cannot receive empty input.");

        return userInput;
    }
}
