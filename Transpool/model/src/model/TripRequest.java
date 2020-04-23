package model;

import java.time.LocalTime;

public class TripRequest {
    private static int ID = 0;

    private final int id;
    private User requestingUser;
    private Station wantedSourceStation;
    private Station wantedDestStation;
    private int wantedTripStartDay;
    private LocalTime wantedTripStartTime;
    private int wantedTripEndDay;
    private LocalTime wantedTripEndTime;
    boolean doesUserAgreeToStationExchange;
    boolean isMatched;

    public TripRequest(User requestingUser, String wantedSourceStationName, String wantedDestStationName, LocalTime wantedTripStartTime) {
        this.id = ++ID;
        this.requestingUser = requestingUser;
        this.wantedSourceStation = Map.getInstance().getStation(wantedSourceStationName);
        this.wantedDestStation = Map.getInstance().getStation(wantedDestStationName);
        this.wantedTripStartTime = wantedTripStartTime;
        this.isMatched = false;
    }

    //region Getters & Setters
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

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        this.isMatched = matched;
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
}
