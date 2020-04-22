package model;

import java.time.LocalTime;

public class TripRequest {
    private User requestingUser;
    private String wantedSourceStationName;
    private String wantedDestStationName;
    private int wantedTripStartDay;
    private LocalTime wantedTripStartTime;
    private int wantedTripEndDay;
    private LocalTime wantedTripEndTime;
    boolean doesUserAgreeToStationExchange;

    public TripRequest(User requestingUser, String wantedSourceStationName, String wantedDestStationName, LocalTime wantedTripStartTime) {
        this.requestingUser = requestingUser;
        this.wantedSourceStationName = wantedSourceStationName;
        this.wantedDestStationName = wantedDestStationName;
        this.wantedTripStartTime = wantedTripStartTime;
    }

    //region Getters & Setters

    public User getRequestingUser() {
        return requestingUser;
    }

    public void setRequestingUser(User requestingUser) {
        this.requestingUser = requestingUser;
    }

    public String getWantedSourceStationName() {
        return wantedSourceStationName;
    }

    public void setWantedSourceStationName(String wantedSourceStationName) {
        this.wantedSourceStationName = wantedSourceStationName;
    }

    public String getWantedDestStationName() {
        return wantedDestStationName;
    }

    public void setWantedDestStationName(String wantedDestStationName) {
        this.wantedDestStationName = wantedDestStationName;
    }

    public LocalTime getWantedTripStartTime() {
        return wantedTripStartTime;
    }

    public void setWantedTripStartTime(LocalTime wantedTripStartTime) {
        this.wantedTripStartTime = wantedTripStartTime;
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
        if (getWantedSourceStationName() != null ? !getWantedSourceStationName().equals(that.getWantedSourceStationName()) : that.getWantedSourceStationName() != null)
            return false;
        if (getWantedDestStationName() != null ? !getWantedDestStationName().equals(that.getWantedDestStationName()) : that.getWantedDestStationName() != null)
            return false;
        return getWantedTripStartTime() != null ? getWantedTripStartTime().equals(that.getWantedTripStartTime()) : that.getWantedTripStartTime() == null;
    }

    @Override
    public int hashCode() {
        int result = getRequestingUser() != null ? getRequestingUser().hashCode() : 0;
        result = 31 * result + (getWantedSourceStationName() != null ? getWantedSourceStationName().hashCode() : 0);
        result = 31 * result + (getWantedDestStationName() != null ? getWantedDestStationName().hashCode() : 0);
        result = 31 * result + (getWantedTripStartTime() != null ? getWantedTripStartTime().hashCode() : 0);
        return result;
    }
    //endregion
}
