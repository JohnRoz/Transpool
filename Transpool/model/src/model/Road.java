package model;

import model.CustomExceptions.RoadDoesNotExistException;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

public class Road {
    private String sourceStationName;
    private String destStationName;
    private boolean isOneWay;
    private int length;
    private int gasNeededPerKm;
    private int maxDrivingSpeed;

    public Road(String sourceStationName, String destStationName, boolean isOneWay, int length, int gasPerKm, int maxDrivingSpeed) {
        this(sourceStationName, destStationName);
        this.isOneWay = isOneWay;
        this.length = length;
        this.gasNeededPerKm = gasPerKm;
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

    public void setSourceStationName(String sourceStationName) {
        this.sourceStationName = sourceStationName;
    }

    public String getDestStationName() {
        return destStationName;
    }

    public void setDestStationName(String destStationName) {
        this.destStationName = destStationName;
    }

    public boolean isOneWay() {
        return isOneWay;
    }

    public void setOneWay(boolean oneWay) {
        isOneWay = oneWay;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getGasNeededPerKm() {
        return gasNeededPerKm;
    }

    public void setGasNeededPerKm(int gasNeededPerKm) {
        this.gasNeededPerKm = gasNeededPerKm;
    }

    public int getMaxDrivingSpeed() {
        return maxDrivingSpeed;
    }

    public void setMaxDrivingSpeed(int maxDrivingSpeed) {
        this.maxDrivingSpeed = maxDrivingSpeed;
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
        double timeInHours = (double)road.getLength() / road.getMaxDrivingSpeed();
        int fullHours = (int)Math.floor(timeInHours);
        int minutes = (int)Math.round((timeInHours - fullHours) * 60);

        return MINUTES.between(LocalTime.MIDNIGHT, LocalTime.of(fullHours, minutes));
    }

    public static int sumRoadsLength(Collection<? extends Road> roads) {
        int lengthSum = 0;
        for (Road road : roads) {
            lengthSum += road.getLength();
        }

        return lengthSum;
    }

    public static int sumRoadsNeededGas(Collection<? extends Road> roads) {
        int gasSum = 0;
        for (Road road : roads) {
            gasSum += road.getGasNeededPerKm() * road.getLength();
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
    public static boolean containsRoadBySrcAndDstNames(Collection<Road> roads, String srcStation, String dstStation) {
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

    public static Road getRoadBySrcDst(Collection<Road> roads, String srcStation, String dstStation) {
        for (Road road : roads) {

            // If the current road has the same src and dst, or if it's not one-way which means
            // That the given src and dst stations can represent the road's other driving direction
            if (road.equals(new Road(srcStation, dstStation)) ||
                    (!road.isOneWay() && road.equals(new Road(dstStation, srcStation)))) {
                return road;
            }
        }

        return null;
    }

    //endregion
}
