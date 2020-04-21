package model;

import model.CustomExceptions.StationDoesNotExistException;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Station {
    private String name;
    private Point coordinate;

    //region Ctor
    public Station(String name, int x, int y) {
        this.name = name;
        this.coordinate = new Point(x, y);
    }
    //endregion

    //region Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Point coordinate) {
        this.coordinate = coordinate;
    }
    //endregion




//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Station)) return false;
//
//        Station station = (Station) o;
//
//        return (getCoordinate() != null ? getCoordinate().equals(station.getCoordinate()) : station.getCoordinate() == null) ||
//                (getName() != null ? getName().equals(station.getName()) : station.getName() == null);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getName(), getCoordinate());
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Station)) return false;

        Station station = (Station) o;

        return getName() != null ? getName().equals(station.getName()) : station.getName() == null;
    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }


    //endregion

    //region Public static methods

    /**
     * Extension function for {@code Collection<Station>} to check if a collection contains a station by its name.
     *
     * @param stations      The {@code Collection<Station>} to search if it contains the specified station.
     * @param stationToFind the unique name of the station to be found.
     * @return True if the collection contains a Station with the same name as the name specified. False otherwise.
     */
    public static boolean containsStationByName(Collection<Station> stations, String stationToFind) {
        return stations.contains(new Station(stationToFind, 0, 0));
    }

    /**
     * Extension function for {@code Collection<Station>} to check if a collection
     * contains a station with the specified coordinates.
     *
     * @param stations The {@code Collection<Station>} to search if it contains a station with the specified coordinate.
     * @param x        The x coordinate.
     * @param y        The y coordinate.
     * @return True if the collection contains a Station with the same coordinates as the
     * coordinates specified. False otherwise.
     */
    public static boolean isStationOverlappingAnother(Collection<Station> stations, int x, int y) {
        for (Station station : stations)
            if (station.coordinate.equals(new Point(x, y)))
                return true;

        return false;
    }

    /**
     * Extension function for {@code Collection<Station>} to get a station by its name.
     *
     * @param stations     The {@code Collection<Station>} to get the specified station from.
     * @param stationToGet the unique name of the station to get.
     * @return The wanted station if found, or null if no such station exist in the collection.
     */
    public static Station getStationByName(Collection<Station> stations, String stationToGet) {
        if (containsStationByName(stations, stationToGet)) {
            for (Station station : stations) {
                if (station.getName().equals(stationToGet)) {
                    return station;
                }
            }
        }

        return null;
    }

    public static Station getStationByCoordinate(Collection<Station> stations, int x, int y) {
        for (Station station : stations) {
            if (station.getCoordinate().equals(new Point(x, y)))
                return station;
        }

        return null;
    }

    /**
     * Iterated over a {@link Set} of stations and created a
     * returns a list of the stations mentioned in the array.
     * @param stations     The pool of available stations.
     * @param stationNames An array of strings. each cell contains the name of the Station to get from the stations Set.
     * @return A list containing the {@link Station}s mentioned in the names array in order.
     */
    public static List<Station> createStationsFromStrArr(Collection<Station> stations, String[] stationNames) throws StationDoesNotExistException{
        List<Station> stationsList = new ArrayList<>();

        for (String stationName : stationNames) {
            Station station = Station.getStationByName(stations, stationName.trim());

            if (station == null)
                throw new StationDoesNotExistException(
                        "Station named %s does not exist in collection %s.",
                        stationName, getStationsString(stations)
                );
            stationsList.add(station);
        }

        return stationsList;
    }

    @Deprecated
    public static String getStationsString(Collection<Station> stations) {
        List<String> stationNames = new ArrayList<>();

        for (Station station :
                stations) {
            stationNames.add(station.getName());
        }

        return stations.stream().map(Station::getName).collect(Collectors.joining(","));
    }

    //endregion
}
