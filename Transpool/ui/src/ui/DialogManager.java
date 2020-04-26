package ui;

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

    private static List<TripRequest> getAllTripRequests() throws OperationNotSupportedException {
        List<TripRequest> tripRequests = engine.getAllTripRequests().stream()
                .sorted(Comparator.comparingInt(TripRequest::getId))
                .collect(Collectors.toList());

        return tripRequests;
    }

    private static void printFormattedTripOffers(TripOffer offer) {
        printfln("Offer number %d:", offer.getId());
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
                sb.append("\tUser number ").append(user.getId())
                        .append(", named ").append(user.getName())
                        .append(" is ").append(user2status.get(user)
                        .name().toLowerCase()).append("\n");
            }
        }

        System.out.println(sb.toString());
    }

    private static void printFormattedTripRequests(TripRequest request) {
        printFormattedUnmatchedTripRequest(request);

        if (!request.isMatched()) {
            System.out.println("This trip request is not matched to any offer.");
            return;
        }

        printfln("The request is matched to trip offer number %d.", request.getMatchedTo().getId());
        printfln("The driver's name is %s.", request.getMatchedTo().getOfferingUserName());
        printfln("The price of the trip is %d ILS.", request.getTripPrice());
        printfln("Arrival time: %1$tH:%1$tM O'clock.", request.getArrivalTime());
        printfln("Average gas usage: %1.3f Km per Liter.", request.getAvgGasUsage());
    }

    private static void printFormattedUnmatchedTripRequest(TripRequest request) {
        printfln("Request number %d.", request.getId());
        printfln("The requesting user's name is %s.", request.getRequestingUser().getName());
        printfln("Origin: %s", request.getWantedSourceStation().getName());
        printfln("Destination: %s", request.getWantedDestStation().getName());
        printfln("Wanted departure time: %1$tH:%1$tM O'clock.", request.getWantedTripStartTime());
    }

    private static List<TripRequest> getUnmatchedTripRequests() {
        List<TripRequest> allRequests = null;
        try {
            allRequests = getAllTripRequests();
        } catch (OperationNotSupportedException e) {
            System.out.println(e.getMessage());
            return null;
        }

        if (allRequests == null || allRequests.isEmpty()) {
            printfln("There are no trip requests in the system.");
            return null;
        }

        return allRequests.stream()
                .filter(request -> !request.isMatched())
                .collect(Collectors.toList());
    }

    private static void printMatchesDetails(TripRequest request, List<TripOffer> matches, int maxMatchesToPresent) {
        for (int i = 0; i < maxMatchesToPresent && i < matches.size(); i++) {
            TripOffer match = matches.get(i);
            printfln("Trip offer number %d:", match.getId());
            printfln("\tThe trip organizer's name is %s.", match.getOfferingUserName());
            printfln("\tThe price of the trip is %d ILS.", TripRequest.getTripPrice(request, match));
            printfln("\tArrival time: %1$tH:%1$tM O'clock.", TripRequest.getArrivalTime(request, match));
            printfln("\tAverage gas usage: %1.3f Km per Liter.", TripRequest.getAvgGasUsage(request, match));
            System.out.println();
        }
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
            String owner = getNonEmptyStringInput("\nPlease enter your name:");
            String srcStation = getNonEmptyStringInput(
                    "Please enter the name of the station you would like to depart from:"
            );
            String dstStation = getNonEmptyStringInput(
                    "Please enter the name of your destination station:"
            );

            int deptHour = getIntInput("When would you like to depart?\nHour (0-23):\t");
            int deptMinute = getIntInput("Minute (Would be rounded to the closest product of 5):\t");

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

        if (tripOffers.isEmpty())
            printfln("There are no trip offers in the system.");

        for (TripOffer tripOffer : tripOffers) {
            printFormattedTripOffers(tripOffer);
            System.out.println();
        }
    }

    public static void printAllTripRequestsDialog() {
        List<TripRequest> tripRequests = null;
        try {
            tripRequests = getAllTripRequests();
        } catch (OperationNotSupportedException e) {
            System.out.println(e.getMessage());
        }

        if (tripRequests == null)
            return;

        if (tripRequests.isEmpty())
            printfln("There are no trip requests in the system.");

        for (TripRequest tripRequest : tripRequests) {
            printFormattedTripRequests(tripRequest);
            System.out.println();
        }
    }

    public static void matchTripRequestToOfferDialog() {
        List<TripRequest> requests = getUnmatchedTripRequests();
        if (requests == null)
            return;

        // Show the user all the unmatched trip requests.
        for (TripRequest request : requests) {
            printFormattedUnmatchedTripRequest(request);
            System.out.println();
        }

        // Select trip request
        int selectedRequestId;
        int maxMatchesWanted;
        List<TripOffer> matches;
        List<TripRequest> chosenRequest;

        try {
            selectedRequestId = getIntInput("Select the request you want to match to an offer:");
            maxMatchesWanted = getIntInput("Enter the max number of matches you would like to get for this request.");
            chosenRequest =
                    requests.stream()
                            .filter(request -> request.getId() == selectedRequestId)
                            .collect(Collectors.toList());
            if (chosenRequest.isEmpty()) {
                System.out.println("There are no requests with that id.");
                return;
            }
            matches = engine.getAllMatchedToRequest(chosenRequest.get(0));
        } catch (InvalidInputException | OperationNotSupportedException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (matches.isEmpty()) {
            System.out.println("There are no matches for that request.");
            return;
        }

        printMatchesDetails(chosenRequest.get(0), matches, maxMatchesWanted);
        List<Integer> matchesIds = matches.stream().map(TripOffer::getId).collect(Collectors.toList());

        int selectedMatchId;
        try {
            selectedMatchId = getIntInput("Choose your preferred match by their ID or enter 0 to cancel:");
        } catch (InvalidInputException e) {
            System.out.println("There are no requests with that id.");
            return;
        }

        if (selectedMatchId == 0)
            return;

        if (!matchesIds.contains(selectedMatchId)) {
            System.out.println("There are no matches with that id.");
            return;
        }

        TripOffer match = matches.stream().filter(offer -> offer.getId() == selectedMatchId).collect(Collectors.toList()).get(0);
        try {
            engine.matchTripRequestToOffer(chosenRequest.get(0), match);
        } catch (OperationNotSupportedException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Match has been committed!");
    }
}
