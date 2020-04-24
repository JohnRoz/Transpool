package model;

import model.Interfaces.IdentifiableTranspoolEntity;

import java.time.LocalTime;
import java.util.List;

public class TripRequest implements IdentifiableTranspoolEntity {
    private static int ID = 0;

    private final int id;
    private User requestingUser;
    private Station wantedSourceStation;
    private Station wantedDestStation;
    private int wantedTripStartDay;
    private LocalTime wantedTripStartTime;
    private int wantedTripEndDay;
    private LocalTime wantedTripEndTime;
    private boolean doesUserAgreeToStationExchange;
    private TripOffer matchedTo;


    public TripRequest(User requestingUser, String wantedSourceStationName, String wantedDestStationName, LocalTime wantedTripStartTime) {
        this.id = ++ID;
        this.requestingUser = requestingUser;
        this.wantedSourceStation = Map.getInstance().getStation(wantedSourceStationName);
        this.wantedDestStation = Map.getInstance().getStation(wantedDestStationName);
        this.wantedTripStartTime = wantedTripStartTime;
        this.matchedTo = null;
    }

    //region Getters & Setters
    @Override
    public int getId() {
        return id;
    }

    public User getRequestingUser() {
        return requestingUser;
    }

    public void setRequestingUser(User requestingUser) {
        this.requestingUser = requestingUser;
    }

    public Station getWantedSourceStation() {
        return wantedSourceStation;
    }

    public void setWantedSourceStation(Station wantedSourceStation) {
        this.wantedSourceStation = wantedSourceStation;
    }

    public Station getWantedDestStation() {
        return wantedDestStation;
    }

    public void setWantedDestStation(Station wantedDestStation) {
        this.wantedDestStation = wantedDestStation;
    }

    public LocalTime getWantedTripStartTime() {
        return wantedTripStartTime;
    }

    public void setWantedTripStartTime(LocalTime wantedTripStartTime) {
        this.wantedTripStartTime = wantedTripStartTime;
    }

    public TripOffer getMatchedTo() {
        return matchedTo;
    }

    public void setMatchedTo(TripOffer tripOffer) {
        this.matchedTo = tripOffer;
    }

    //endregion

    //region Equals & hashCode Overrides
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TripRequest)) return false;

        TripRequest that = (TripRequest) o;

        if (getRequestingUser() != null ? !getRequestingUser().equals(that.getRequestingUser()) : that.getRequestingUser() != null)
            return false;
        if (getWantedSourceStation() != null ? !getWantedSourceStation().equals(that.getWantedSourceStation()) : that.getWantedSourceStation() != null)
            return false;
        if (getWantedDestStation() != null ? !getWantedDestStation().equals(that.getWantedDestStation()) : that.getWantedDestStation() != null)
            return false;
        return getWantedTripStartTime() != null ? getWantedTripStartTime().equals(that.getWantedTripStartTime()) : that.getWantedTripStartTime() == null;
    }

    @Override
    public int hashCode() {
        int result = getRequestingUser() != null ? getRequestingUser().hashCode() : 0;
        result = 31 * result + (getWantedSourceStation() != null ? getWantedSourceStation().hashCode() : 0);
        result = 31 * result + (getWantedDestStation() != null ? getWantedDestStation().hashCode() : 0);
        result = 31 * result + (getWantedTripStartTime() != null ? getWantedTripStartTime().hashCode() : 0);
        return result;
    }
    //endregion

    //region Public Methods
    public boolean isMatched() {
        return getMatchedTo() != null;
    }

    /**
     *
     * If the request is not matched this returns null.
     * @return A sub path from the path of the matched {@link TripOffer}.
     * If no {@link TripOffer} is matched, returns null
     */
    private List<Road> getTravelPath() {
        if (!isMatched())
            return null;

        return Road.getSubPath(
                getMatchedTo().getRoadsInTrip(),
                getWantedSourceStation().getName(),
                getWantedDestStation().getName()
        );
    }

    public double getTravelPathNeededGas() {
        if (!isMatched())
            return 0;

        return Road.sumRoadsKmPerGasLiter(getTravelPath());
    }

    public int getTripPrice() {
        if (!isMatched())
            return 0;

        return Road.sumRoadsLength(getTravelPath()) * getMatchedTo().getPricePerKm();
    }

    public LocalTime getArrivalTime() {
        if (!isMatched())
            return null;

        LocalTime arrivalTime = getMatchedTo().getTiming().getTime().plusMinutes(getTripDuration());
        return TripTiming.roundTime(arrivalTime);
    }

    /**
     * Sums the duration of travel of every road in the trip, and returns the
     * total amount of time in minutes.
     *
     * @return Duration of trip in minutes.
     */
    private long getTripDuration() {
        long totalDuration = 0;

        for (Road road : getTravelPath()) {
            totalDuration += Road.calcRoadTravelDuration(road);
        }

        return totalDuration;
    }

    public double getAvgGasUsage() {
        List<Road> travelPath = getTravelPath();

        if (travelPath == null)
            return 0;

        return (double) Road.sumRoadsKmPerGasLiter(travelPath) / travelPath.size();
    }
    //endregion
}
