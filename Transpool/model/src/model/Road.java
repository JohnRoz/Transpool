package model;

import model.CustomExceptions.RoadDoesNotExistException;
import model.Interfaces.TranspoolEntity;

import java.security.InvalidParameterException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

public class Road implements TranspoolEntity {
    private String sourceStationName;
    private String destStationName;
    private boolean isOneWay;
    private int length;
    private int kmPerGasLiter;
    private int maxDrivingSpeed;

    public Road(String sourceStationName, String destStationName, boolean isOneWay, int length, int kmPerGasLiter, int maxDrivingSpeed) {
        this(sourceStationName, destStationName);
        this.isOneWay = isOneWay;
        this.length = length;
        this.kmPerGasLiter = kmPerGasLiter;
        this.maxDrivingSpeed = maxDrivingSpeed;
    }

    private Road(String sourceStationName, String destStationName) {
        this.sourceStationName = sourceStationName;
        this.destStationName = destStationName;
    }

    //region Getters & Setters
    public String getSourceStationName() {
        return sourceStationName;
    }

    public String getDestStationName() {
        return destStationName;
    }

    public boolean isOneWay() {
        return isOneWay;
    }

    public int getLength() {
        return length;
    }

    public int getKmPerGasLiter() {
        return kmPerGasLiter;
    }

    public int getMaxDrivingSpeed() {
        return maxDrivingSpeed;
    }
    //endregion

    //region Equals & hashCode overrides

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Road)) return false;

        Road road = (Road) o;

        if (getSourceStationName() != null ? !getSourceStationName().equals(road.getSourceStationName()) : road.getSourceStationName() != null)
            return false;
        return getDestStationName() != null ? getDestStationName().equals(road.getDestStationName()) : road.getDestStationName() == null;
    }

    @Override
    public int hashCode() {
        int result = getSourceStationName() != null ? getSourceStationName().hashCode() : 0;
        result = 31 * result + (getDestStationName() != null ? getDestStationName().hashCode() : 0);
        return result;
    }

    //endregion

    //region Public methods

    /**
     * Receives a road and calculates the time it would take to drive across it (in minutes).
     *
     * @param road The {@link Road} to calculate travel duration of.
     * @return Amount of minutes it would take to drive across the given road.
     */
    public static long calcRoadTravelDuration(Road road) {
        double timeInHours = (double) road.getLength() / road.getMaxDrivingSpeed();
        int fullHours = (int) Math.floor(timeInHours);
        int minutes = (int) Math.round((timeInHours - fullHours) * 60);

        return MINUTES.between(LocalTime.MIDNIGHT, LocalTime.of(fullHours, minutes));
    }

    public static int sumRoadsLength(Collection<? extends Road> roads) {
        int lengthSum = 0;
        for (Road road : roads) {
            lengthSum += road.getLength();
        }

        return lengthSum;
    }

    public static int sumRoadsKmPerGasLiter(Collection<? extends Road> roads) {
        int gasSum = 0;
        for (Road road : roads) {
            gasSum += road.getKmPerGasLiter();
        }

        return gasSum;
    }

    /**
     * Extension function for {@code Collection<Road>} to check if a collection
     * contains a {@link Road} by its source and destination stations.
     *
     * @param roads      The {@code Collection<Road>} to search if it contains the specified road.
     * @param srcStation The name of the source station of the {@link Road} to be found.
     * @param dstStation The name of the destination station of the {@link Road} to be found.
     * @return True if the collection contains a {@link Road} with the same source and destination
     * as the ones specified. False otherwise.
     */
    public static boolean containsRoadBySrcAndDstNames(Collection<? extends Road> roads, String srcStation, String dstStation) {
        for (Road road : roads) {

            // If the current road has the same src and dst, or if it's not one-way which means
            // That the given src and dst stations can represent the road's other driving direction
            if (road.equals(new Road(srcStation, dstStation)) ||
                    (!road.isOneWay() && road.equals(new Road(dstStation, srcStation)))) {
                return true;
            }
        }

        return false;

    }

    public static boolean containsRoad(Collection<? extends Road> roads, Road toCheck) {
        return containsRoadBySrcAndDstNames(roads, toCheck.sourceStationName, toCheck.destStationName);
    }

    /**
     * Iterated over an array of station names and returns a {@link List} of the underlying roads.
     *
     * @param roads        The pool of available roads.
     * @param stationNames An array of strings. Each cell contains the name of a {@link Station}
     *                     that's a part of the full path of travel.
     * @return A list containing the {@link Road}s mentioned in the path of stations names.
     */
    public static List<Road> getRoadListFromStationsPath(Collection<Road> roads, String[] stationNames)
            throws RoadDoesNotExistException {
        List<Road> roadsList = new ArrayList<>();

        for (int i = 0; i < stationNames.length - 1; i++) {
            String srcStationName = stationNames[i].trim();
            String dstStationName = stationNames[i + 1].trim();

            Road road = Road.getRoadBySrcDst(roads, srcStationName, dstStationName);

            if (road == null)
                throw new RoadDoesNotExistException(
                        "Road with source station %s and destination station %s does not exist in collection %s.",
                        srcStationName, dstStationName, String.join(",", stationNames)
                );
            roadsList.add(road);
        }

        return roadsList;
    }

    public static Road getRoadBySrcDst(Collection<Road> roads, String srcStation, String dstStation) throws RoadDoesNotExistException {
        for (Road road : roads) {

            // If the current road has the same src and dst, or if it's not one-way which means
            // That the given src and dst stations can represent the road's other driving direction
            if (road.equals(new Road(srcStation, dstStation)) ||
                    (!road.isOneWay() && road.equals(new Road(dstStation, srcStation)))) {
                return road;
            }
        }

        throw new RoadDoesNotExistException(srcStation, dstStation);
    }

    private static void assertContainsStation(List<Road> path, String wantedStation) {
        if (!containsStation(path, wantedStation))
            throw new InvalidParameterException(
                    "Specified path does not contain station " + wantedStation + ".");
    }
    public static boolean containsStation(Collection<Road> roads, String stationName) {
        for (Road road : roads) {
            String srcStation = road.sourceStationName;
            String dstStation = road.destStationName;

            if (stationName.equals(srcStation) ||
                    stationName.equals(dstStation))
                return true;
        }

        return false;
    }

    public static List<Road> getSubPath(List<Road> path, String wantedSrcStation, String wantedDstStation) {
        assertContainsStation(path, wantedSrcStation);
        assertContainsStation(path, wantedDstStation);

        List<Road> subPath = new ArrayList<>();
        boolean isInSubPathRange = false;

        // Populate sub-path
        for (Road road : path) {
            String src = road.sourceStationName;
            String dst = road.destStationName;

            if (isInSubPathRange) {
                if (src.equals(wantedDstStation))
                    break;
                subPath.add(road);
            }

            if (src.equals(wantedSrcStation)) {
                subPath.add(road);
                isInSubPathRange = true;
            }
        }

        return subPath;
    }

    //endregion
}
