package model;

import java.util.Collection;
import java.util.Objects;

public class Road {
    private String sourceStationName;
    private String destStationName;
    private boolean isOneWay;
    private double length; // TODO: Check - Could be an int?
    private double gasNeededPerKm;
    private double maxDrivingSpeed;

    public Road(String sourceStationName, String destStationName, boolean isOneWay, double length, double gasPerKm, double maxDrivingSpeed) {
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

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getGasNeededPerKm() {
        return gasNeededPerKm;
    }

    public void setGasNeededPerKm(double gasNeededPerKm) {
        this.gasNeededPerKm = gasNeededPerKm;
    }

    public double getMaxDrivingSpeed() {
        return maxDrivingSpeed;
    }

    public void setMaxDrivingSpeed(double maxDrivingSpeed) {
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
     * Receives a road and calculates the time it would take to drive across it (Hours).
     *
     * @param road The model.Road to calculate travel duration of.
     * @return Amount of hours it would take to drive across the given road.
     */
    public static double calcRoadTravelDuration(Road road) {
        return road.getLength() / road.getMaxDrivingSpeed();
    }

    public static double sumRoadsLength(Collection<? extends Road> roads) {
        double lengthSum = 0;
        for (Road road : roads) {
            lengthSum += road.getLength();
        }

        return lengthSum;
    }

    public static double sumRoadsNeededGas(Collection<? extends Road> roads) {
        double gasSum = 0;
        for (Road road : roads) {
            gasSum += road.getGasNeededPerKm() * road.getLength();
        }

        return gasSum;
    }

    /**
     * Extension function for {@code Collection<Road>} to check if a collection
     * contains a {@link Road} by its source and destination stations.
     *
     * @param roads   The {@code Collection<Road>} to search if it contains the specified road.
     * @param roadSrc The name of the source station of the {@link Road} to be found.
     * @param roadDst The name of the destination station of the {@link Road} to be found.
     * @return True if the collection contains a {@link Road} with the same source and destination
     * as the ones specified. False otherwise.
     */
    public static boolean containsRoadBySrcAndDstNames(Collection<Road> roads, String roadSrc, String roadDst) {
        return roads.contains(new Road(roadSrc, roadDst));
    }

    //endregion
}
