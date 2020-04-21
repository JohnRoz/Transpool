package ui.util;

import model.Enums.UserAction;

public class Output {
    public static void greetUser() {
        System.out.println("Welcome user!\n");
        System.out.println("What would you like to do?");
    }

    public static void printMenu() {
        System.out.println("1.\tUpload map, stations and trip offers data (XML).");
        System.out.println("2.\tPost a trip request.");
        System.out.println("3.\tGet all trip offers.");
        System.out.println("4.\tGet all trip requests.");
        System.out.println("5.\tMatch an unmatched trip request to an offer..");
        System.out.println("6.\tExit.");
    }

    public static void printActionInputError() {
        System.out.printf(
                "Invalid input.\nInput can only contain numbers between 0 and %d.\n\n",
                UserAction.getValuesCount());
    }
}
