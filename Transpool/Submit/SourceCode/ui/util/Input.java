package ui.util;

import com.sun.org.apache.bcel.internal.generic.ATHROW;
import model.CustomExceptions.InvalidInputException;
import model.Enums.RepetitionRate;
import model.Enums.UserAction;
import model.Extensions.IntegerExtensions;
import model.Interfaces.NamedTranspoolEntity;
import model.Station;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
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

    /**
     * Reads from the console, check the input to be a valid {@link RepetitionRate}
     *
     * @return The {@link RepetitionRate} if input is valid. Null otherwise.
     */
    public static RepetitionRate getRepetitionRateInput() throws InvalidInputException {
        String repetitionRateNum = getUserInput();
        Integer parsedInt = IntegerExtensions.tryParseInt(repetitionRateNum);
        if (parsedInt != null) {
            int shiftedToZeroBased = parsedInt - 1;

            if (RepetitionRate.isValueInRange(shiftedToZeroBased))
                return RepetitionRate.values()[shiftedToZeroBased];
        }

        throw new InvalidInputException(
                "Invalid input.\nInput can only contain numbers between 0 and %d.\n",
                RepetitionRate.getValuesCount()
        );
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

    public static int getIntInput(String prompt) throws InvalidInputException {
        System.out.println(prompt);
        return getIntInput();
    }

    public static String getNonEmptyStringInput() throws InvalidInputException {
        String userInput = getUserInput();

        if (userInput == null || userInput.isEmpty())
            throw new InvalidInputException("Cannot receive empty input.");

        return userInput;
    }

    public static String getNonEmptyStringInput(String prompt) throws InvalidInputException {
        System.out.println(prompt);
        return getNonEmptyStringInput();
    }

    public static List<String> enterStationNames(String promptBefore) throws InvalidInputException {
        System.out.println(promptBefore);
        List<String> stationNames = new ArrayList<>();
        String station = getNonEmptyStringInput("Enter station:");

        while (!station.equals("0")) {
            stationNames.add(station);
            station = getNonEmptyStringInput("Enter station:");
        }

        return stationNames;
    }
}
