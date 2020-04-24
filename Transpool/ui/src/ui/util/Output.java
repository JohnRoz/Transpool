package ui.util;

import model.Enums.UserAction;
import model.Interfaces.IdentifiableTranspoolEntity;
import model.Interfaces.NamedTranspoolEntity;
import model.Interfaces.TranspoolEntity;
import model.Station;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    public static void printfln(String fmt, Object... args) {
        System.out.printf(fmt + "%n", args);
    }

    public static void printActionInputError() {
        System.out.printf(
                "Invalid input.\nInput can only contain numbers between 0 and %d.\n\n",
                UserAction.getValuesCount());
    }

    public static void printNamedEntities(Collection<? extends NamedTranspoolEntity> namedEntities) {
        for (NamedTranspoolEntity namedEntity : namedEntities) {
            System.out.println(namedEntity.getName());
        }
    }

    public static void printIdentifiableEntities(Collection<? extends IdentifiableTranspoolEntity> idEntities) {
        for (IdentifiableTranspoolEntity idEntity : idEntities) {
            System.out.println(idEntity.getId());
        }
    }

    public static void printStationsPath(List<Station> stations) {
        String path =
                stations.stream().
                        map(Station::getName)
                        .collect(Collectors.joining(" -> "));

        System.out.println(path);
    }
}
