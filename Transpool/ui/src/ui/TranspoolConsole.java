package ui;

import model.Enums.UserAction;

import static ui.DialogManager.*;
import static ui.util.Input.getUserActionInput;
import static ui.util.Output.*;

public class TranspoolConsole {

    public static void start() {
        greetUser();
        printMenu();

        try {
            userDialogLoop(getUserActionInput());
        } catch (Exception e) {
            System.out.println("Something unexpected happened...\nHere's the Stack Trace:\n\n");
            e.printStackTrace();
        }

        System.out.println("Goodbye!");
    }

    private static void userDialogLoop(UserAction action) {
        while (action != UserAction.EXIT) {
            if (action == null) {
                printActionInputError();
            } else {
                switch (action) {
                    case READ_XML_FILE:
                        readXmlFileDialog();
                        break;
                    case POST_TRIP_REQUEST:
                        postTripRequestDialog();
                        break;
                    case GET_ALL_TRIP_OFFERS:
                        printAllTripOffersDialog();
                        break;
                    case GET_ALL_TRIP_REQUESTS:
                        printAllTripRequestsDialog();
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

}
