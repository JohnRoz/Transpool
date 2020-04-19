package UI;

import Engine.Engine;
import model.Enums.UserAction;
import model.Interfaces.IEngine;

import static UI.Input.getUserActionInput;
import static UI.Output.*;

public class TranspoolConsole {

    private static IEngine TranspoolEngine = new Engine();

    public static void start() {
        greetUser();
        printMenu();
        UserAction action = getUserActionInput();

        userDialogLoop(action);

        System.out.println("Goodbye!");
    }

    private static void userDialogLoop(UserAction action) {
        while (action != UserAction.EXIT) {
            if (action == null) {
                printActionInputError();
            } else {
                switch (action) {
                    case READ_XML_FILE:
                        System.out.println("Good1");
                        break;
                    case POST_TRIP_REQUEST:
                        System.out.println("Good2");
                        break;
                    case GET_ALL_TRIP_OFFERS:
                        System.out.println("Good3");
                        break;
                    case GET_ALL_TRIP_REQUESTS:
                        System.out.println("Good4");
                        break;
                    case MATCH_TRIP_REQUEST_TO_OFFER:
                        System.out.println("Good5");
                        break;
                }
            }
            printMenu();
            action = getUserActionInput();
        }
    }

    /**
     * Prints the continuation of the dialog with the user according to the specific action
     */
    private static void actionDialog() {
        // TODO Transfer to Output (?)
        // TODO Create such a method for each possible action
    }
}
