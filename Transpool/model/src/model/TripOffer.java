package model;

import model.Enums.UserTransitionType;

import java.time.LocalTime;
import java.util.*;
import java.util.Map;

public class TripOffer {

    //region Static members
    private static int ID = 0;
    //endregion

    //region Members
    private final int id;
    private String offeringUserName;
    private int basePassengersCapacity;
    private int pricePerKm;
    private TripTiming timing;
    private final List<Station> stationsInTrip;
    private final List<Road> roadsInTrip;
    private final Set<TripRequest> registeredRequests;
    //endregion

    //region Ctor
    public TripOffer(String offeringUserName, int basePassengersCapacity, int pricePreKm, TripTiming timing, List<Station> stations, List<Road> roads) {
        this.id = ++ID;
        this.offeringUserName = offeringUserName;
        this.basePassengersCapacity = basePassengersCapacity;
        this.pricePerKm = pricePreKm;
        this.timing = timing;
        this.stationsInTrip = stations;
        this.roadsInTrip = roads;
        this.registeredRequests = new HashSet<>();
    }
    //endregion

    //region Getters & Setters

    public int getId() {
        return id;
    }

    public String getOfferingUserName() {
        return offeringUserName;
    }

    public void setOfferingUserName(String offeringUserName) {
        this.offeringUserName = offeringUserName;
    }

    public int getBasePassengersCapacity() {
        return basePassengersCapacity;
    }

    public void setBasePassengersCapacity(int basePassengersCapacity) {
        this.basePassengersCapacity = basePassengersCapacity;
    }

    public List<Station> getStationsInTrip() {
        return stationsInTrip;
    }

    public int getPricePerKm() {
        return pricePerKm;
    }

    public void setPricePerKm(int pricePerKm) {
        this.pricePerKm = pricePerKm;
    }

    public TripTiming getTiming() {
        return timing;
    }

    public void setTiming(TripTiming timing) {
        this.timing = timing;
    }
    //endregion

    //region Equals & HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TripOffer)) return false;

        TripOffer tripOffer = (TripOffer) o;

        if (getBasePassengersCapacity() != tripOffer.getBasePassengersCapacity()) return false;
        if (Double.compare(tripOffer.getPricePerKm(), getPricePerKm()) != 0) return false;
        if (getOfferingUserName() != null ? !getOfferingUserName().equals(tripOffer.getOfferingUserName()) : tripOffer.getOfferingUserName() != null)
            return false;
        if (getStationsInTrip() != null ? !getStationsInTrip().equals(tripOffer.getStationsInTrip()) : tripOffer.getStationsInTrip() != null)
            return false;
        if (!Objects.equals(roadsInTrip, tripOffer.roadsInTrip))
            return false;
        return getTiming() != null ? getTiming().equals(tripOffer.getTiming()) : tripOffer.getTiming() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getOfferingUserName() != null ? getOfferingUserName().hashCode() : 0;
        result = 31 * result + getBasePassengersCapacity();
        result = 31 * result + (getStationsInTrip() != null ? getStationsInTrip().hashCode() : 0);
        result = 31 * result + (roadsInTrip != null ? roadsInTrip.hashCode() : 0);
        temp = Double.doubleToLongBits(getPricePerKm());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getTiming() != null ? getTiming().hashCode() : 0);
        return result;
    }
    //endregion

    //region Public methods

    //region Public Static Methods
    public static boolean hasTripOfferById(Collection<TripOffer> tripOffers, int id) {
        return getTripOfferById(tripOffers, id) != null;
    }

    public static TripOffer getTripOfferById(Collection<TripOffer> tripOffers, int id) {
        for (TripOffer tripOffer : tripOffers) {
            if (tripOffer.id == id)
                return tripOffer;
        }

        return null;
    }
    //endregion

    public LocalTime getArrivalTime() {
        LocalTime arrivalTime = this.timing.getTime().plusMinutes(getTripDuration());
        return TripTiming.roundTime(arrivalTime);
    }

    public int getTripPrice() {
        return Road.sumRoadsLength(roadsInTrip) * pricePerKm;
    }

    public int getRemainingPassengersCapacity() {
        return basePassengersCapacity - registeredRequests.size();
    }

    public Set<User> getRegisteredUsers() {
        Set<User> registeredUsers = new HashSet<>();

        for (TripRequest request : registeredRequests) {
            registeredUsers.add(request.getRequestingUser());
        }

        return registeredUsers;
    }

    /**
     * Builds an object that describes the Stations in which Users that are registered to
     * the trip perform transition (Depart or arrive), specifying the Users, and their TransitionType.
     *
     * @return The object created.
     */
    public Map<Station, Map<User, UserTransitionType>> getStationsToStopInWithUsersAndStatus() {
        Map<Station, Map<User, UserTransitionType>> station2Users2Statuses = new HashMap<>();

        for (Station station : stationsInTrip) {
            for (TripRequest request : registeredRequests) {
                User requestingUser = request.getRequestingUser();

                if (station.equals(request.getWantedSourceStation())) {
                    // Departing
                    initStationsToUsersToStatus(
                            station2Users2Statuses, station, requestingUser, UserTransitionType.Departing
                    );

                } else if (station.equals(request.getWantedDestStation())) {
                    // Arriving
                    initStationsToUsersToStatus(
                            station2Users2Statuses, station, requestingUser, UserTransitionType.Arriving
                    );
                }
            }
        }

        return station2Users2Statuses;
    }

    public double getAvgGasUsage() {
        return (double) Road.sumRoadsNeededGas(roadsInTrip) / roadsInTrip.size();
    }
    //endregion

    //region Private methods

    /**
     * Sums the duration of travel of every road in the trip, and returns the
     * total amount of time in minutes.
     *
     * @return Duration of trip in minutes.
     */
    private long getTripDuration() {
        long totalDuration = 0;

        for (Road road : roadsInTrip) {
            totalDuration += Road.calcRoadTravelDuration(road);
        }

        return totalDuration;
    }

    /**
     * Helper method in order to shorten the {@link TripOffer#getStationsToStopInWithUsersAndStatus} since
     * it needs to initialize such a complex structure.
     *
     * @param station2Users2Statuses The object that maps each station in the trip to the users that
     *                               arrive or depart at that station, and the status indicating whether
     *                               the user is departing from or arriving to the Station.
     * @param station                A station in the path of the trip that is the source or the destination of at least
     *                               one of the users registered to this TripOffer.
     * @param requestingUser         A Users that is registered to the trip, that gets off or on the car at the
     *                               current station.
     * @param travelStatus           Whether the specified User gets on or off the car at the specified station.
     */
    private static void initStationsToUsersToStatus(Map<Station, Map<User, UserTransitionType>> station2Users2Statuses, Station station, User requestingUser, UserTransitionType travelStatus) {
        if (!station2Users2Statuses.containsKey(station)) {
            station2Users2Statuses.put(station, new HashMap<>());
        }

        station2Users2Statuses.get(station).put(requestingUser, travelStatus);
    }
    //endregion
}
