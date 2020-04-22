package ui;

import engine.Engine;
import model.Enums.UserAction;
import model.Interfaces.IEngine;

import static ui.CommandExecutor.postTripRequestDialog;
import static ui.CommandExecutor.readXmlFileDialog;
import static ui.util.Input.getUserActionInput;
import static ui.util.Output.*;

public class TranspoolConsole {

    public static void start() {
        greetUser();
        printMenu();
        UserAction action = getUserActionInput();

        try {
            userDialogLoop(action);
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

}
