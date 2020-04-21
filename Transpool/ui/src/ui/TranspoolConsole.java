package ui;

import engine.Engine;
import model.Enums.UserAction;
import model.Interfaces.IEngine;

import static ui.UserActions.readXmlFileDialog;
import static ui.util.Input.getUserActionInput;
import static ui.util.Output.*;

public class TranspoolConsole {

    public static IEngine engine = new Engine();

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
                        readXmlFileDialog();
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

}
