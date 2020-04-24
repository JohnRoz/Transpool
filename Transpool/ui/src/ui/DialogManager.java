package ui;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import engine.Engine;
import model.CustomExceptions.FormattedMessageException;
import model.CustomExceptions.InvalidInputException;
import model.CustomExceptions.StationDoesNotExistException;
import model.Enums.UserTransitionType;
import model.Interfaces.IEngine;
import model.Interfaces.NamedTranspoolEntity;
import model.Station;
import model.TripOffer;
import model.TripRequest;
import model.User;
import ui.util.Output;

import javax.naming.OperationNotSupportedException;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.time.DateTimeException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ui.util.Input.*;
import static ui.util.Output.*;

/**
 * This class is a util that holds all the static handlers for the actions the user asks for
 */
public class DialogManager {

    private static final IEngine engine = new Engine();

    //region Private Methods
    private static boolean printStationNames() {
        Collection<? extends NamedTranspoolEntity> stations;
        try {
            stations = engine.getAllStations();
        } catch (OperationNotSupportedException e) {
            System.out.println(e.getMessage());
            return false;
        }

        System.out.println("Available stations:");
        printNamedEntities(stations);
        return true;
    }

    private static List<TripOffer> getAllTripOffers() {
        List<TripOffer> tripOffers;
        try {
            tripOffers = engine.getAllTripOffers().stream()
                    .sorted(Comparator.comparingInt(TripOffer::getId))
                    .collect(Collectors.toList());
        } catch (OperationNotSupportedException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return tripOffers;
    }

    private static List<TripRequest> getAllTripRequests() {
        List<TripRequest> tripRequests;
        try {
            tripRequests = engine.getAllTripRequests().stream()
                    .sorted(Comparator.comparingInt(TripRequest::getId))
                    .collect(Collectors.toList());
        } catch (OperationNotSupportedException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return tripRequests;
    }

    private static void printFormattedTripOffers(TripOffer offer) {
        printfln("Offer number %d.", offer.getId());
        printfln("The trip organizer's name is %s.", offer.getOfferingUserName());
        System.out.print("The path of the trip is: ");
        printStationsPath(offer.getStationsInTrip());
        printfln("The price of the trip is %d ILS.", offer.getTripPrice());
        printfln("Departure time: %1$tH:%1$tM O'clock.", offer.getTiming().getTime());
        printfln("Arrival time: %1$tH:%1$tM O'clock.", offer.getArrivalTime());
        printfln("Remaining passengers capacity: %d", offer.getRemainingPassengersCapacity());
        printUserSwitchesInTrip(offer);
        printfln("Average gas usage: %1.3f Km per Liter.", offer.getAvgGasUsage());
    }

    // TODO: CHECK THAT THIS ACTUALLY WORKS WHEN I HAVE SUFFICIENT DATA TO POPULATE THE SYSTEM WITH
    private static void printUserSwitchesInTrip(TripOffer offer) {
        if (offer.getStationsToStopInWithUsersAndStatus().keySet().isEmpty()) {
            printfln("No passengers are registered to this trip.");
            return;
        }

        System.out.println("The stations to switch passengers in:");

        // DEAR GOD, WHY THE HELL JAVA 8 DOESN'T HAVE var KEYWORD?!
        Map<Station, Map<User, UserTransitionType>> switches =
                offer.getStationsToStopInWithUsersAndStatus();
        StringBuilder sb = new StringBuilder();
        for (Station station : switches.keySet()) {
            sb.append(station.getName()).append(":\n");

            Map<User, UserTransitionType> user2status = switches.get(station);
            for (User user : user2status.keySet()) {
                sb.append("\tUser number").append(user.getId())
                        .append(", named ").append(user.getName())
                        .append(" is ").append(user2status.get(user)
                        .name().toLowerCase()).append("\n");
            }

            sb.append("\n");
        }

        System.out.println(sb.toString());
    }

    private static void printFormattedTripRequests(TripRequest request) {
        printfln("Request number %d.", request.getId());
        printfln("The requesting user's name is %s.", request.getRequestingUser().getName());
        printfln("Origin: %s", request.getWantedSourceStation().getName());
        printfln("Destination: %s", request.getWantedDestStation().getName());
        printfln("Wanted departure time: %1$tH:%1$tM O'clock.", request.getWantedTripStartTime());

        if (!request.isMatched()) {
            System.out.println("This trip request is not matched to any offer.");
            return;
        }

        printfln("The request is matched to trip offer number %d.", request.getMatchedTo().getId());
        printfln("The driver's name is %s.", request.getMatchedTo().getOfferingUserName());
        printfln("The price of the trip is %d ILS.", request.getTripPrice());
        printfln("Arrival time: %1$tH:%1$tM O'clock.", request.getArrivalTime());

        // TODO: CHECK THIS IS ACTUALLY WHAT WE WERE ASKED FOR
        printfln("Average gas usage: %1.3f Km per Liter.", request.getAvgGasUsage());
    }
    //endregion

    public static void readXmlFileDialog() {
        System.out.println("Enter the path for the Transpool file:");
        String path = getUserInput();

        try {
            engine.readXmlFile(path);
            System.out.println("\nThe file was loaded successfully!\n");
        } catch (FileNotFoundException | FormattedMessageException e) {
            System.out.println(e.getMessage());
        } catch (JAXBException e) {
            System.out.println(e.getMessage() + "\nSomething about the schema might be off.");
        }
    }

    public static void postTripRequestDialog() {
        if (!printStationNames())
            return;

        final String EXCEPTION_MSG = "\nThe post process failed.\n";
        try {
            System.out.println("\nPlease enter your name:");
            String owner = getNonEmptyStringInput();

            System.out.println("Please enter the name of the station you would like to depart from:");
            String srcStation = getNonEmptyStringInput();

            System.out.println("Please enter the name of your destination station:");
            String dstStation = getNonEmptyStringInput();

            System.out.println("When would you like to depart?");
            System.out.print("Hour (0-23):\t");
            int deptHour = getIntInput();

            System.out.print("Minute (Would be rounded to the closest product of 5):\t");
            int deptMinute = getIntInput();

            engine.postTripRequest(owner, srcStation, dstStation, deptHour, deptMinute);
            System.out.println("Your Trip Request has been posted!\n");

        } catch (InvalidInputException | OperationNotSupportedException | StationDoesNotExistException e) {
            System.out.println(EXCEPTION_MSG + e.getMessage() + '\n');
        } catch (DateTimeException e) {
            System.out.println(
                    EXCEPTION_MSG +
                            "Invalid input.\n" +
                            "Hours must be in the range of 0 to 23 and minutes must be in the range of 0 to 59.\n"
            );
        }
    }

    public static void printAllTripOffersDialog() {
        List<TripOffer> tripOffers = getAllTripOffers();

        if (tripOffers == null)
            return;

        for (TripOffer tripOffer : tripOffers) {
            printFormattedTripOffers(tripOffer);
            System.out.println();
        }
    }

    public static void printAllTripRequestsDialog() {
        List<TripRequest> tripRequests = getAllTripRequests();

        if (tripRequests == null)
            return;

        for (TripRequest tripRequest : tripRequests) {
            printFormattedTripRequests(tripRequest);
            System.out.println();
        }
    }
}
