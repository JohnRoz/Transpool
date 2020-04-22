package ui;

import engine.Engine;
import model.CustomExceptions.FormattedMessageException;
import model.CustomExceptions.InvalidInputException;
import model.CustomExceptions.StationDoesNotExistException;
import model.Interfaces.IEngine;
import model.Station;

import javax.naming.OperationNotSupportedException;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.time.DateTimeException;
import java.util.Collection;

import static ui.util.Input.*;

/**
 * This class is a util that holds all the static handlers for the actions the user asks for
 */
public class CommandExecutor {

    private static final IEngine engine = new Engine();

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
        try {
            printStationNames(engine.getAllStations());
        } catch (OperationNotSupportedException e) {
            System.out.println(e.getMessage());
            return;
        }

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

    private static void printStationNames(Collection<Station> stations) {
        System.out.println("Available stations:");
        for (Station station : stations) {
            System.out.println(station.getName());
        }
    }
}
