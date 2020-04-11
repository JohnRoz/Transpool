package Model;

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
        this.sourceStationName = sourceStationName;
        this.destStationName = destStationName;
        this.isOneWay = isOneWay;
        this.length = length;
        this.gasNeededPerKm = gasPerKm;
        this.maxDrivingSpeed = maxDrivingSpeed;
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

    //region Public methods
    /**
     * Receives a road and calculates the time it would take to drive across it (Hours).
     * @param road The Road to calculate travel duration of.
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

    //region Equals & hashCode overrides
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Road)) return false;
        Road road = (Road) o;
        return Objects.equals(getSourceStationName(), road.getSourceStationName()) &&
                Objects.equals(getDestStationName(), road.getDestStationName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSourceStationName(), getDestStationName());
    }
    //endregion

    //endregion
}
