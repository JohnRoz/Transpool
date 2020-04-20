package model;

import java.time.LocalTime;

public class TripRequest {
    private String requestingUserName;
    private String wantedSourceStationName;
    private String wantedDestStationName;
    private int wantedTripStartDay;
    private LocalTime wantedTripStartTime;
    private int wantedTripEndDay;
    private LocalTime wantedTripEndTime;
    boolean doesUserAgreeToStationExchange;
}
