package ui;

import model.CustomExceptions.FormattedMessageException;
import model.CustomExceptions.UnsupportedFileTypeException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class is a util that holds all the static handlers for the actions the user asks for
 */
public class UserActions {

    public static void readXmlFileDialog() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the path for the Transpool file:");
        String path = scanner.nextLine();

        try {
            TranspoolConsole.engine.ReadXmlFile(path);
            System.out.println("The file was loaded successfully!");
        } catch (FileNotFoundException | FormattedMessageException e) {
            System.out.println(e.getMessage());
        } catch (JAXBException e) {
            System.out.println(e.getMessage() + "\nSomething about the schema might be off.");
        }
    }
}
