package model;

import java.util.Set;
import java.util.SortedSet;

public class TripOffer {
    private String offeringUserName;
    private int passengersQuota;
    private SortedSet<Station> stationsInTrip;
    private SortedSet<Road> roadsInTrip;
    private double pricePerKm;
    private TripTiming timing;

    /*public model.TripOffer(String offeringUserName, int passengersQuota, Set<model.Road> roadsInPath, double pricePreKm, model.TripTiming timing) {
        this.offeringUserName = offeringUserName;
        this.passengersQuota = passengersQuota;
        this.roadsInTrip = roadsInPath;
        this.pricePerKm = pricePreKm;
        this.timing = timing;

        InitStationsInTrip();
    }

    private void InitStationsInTrip() {
        stationsInTrip = new HashSet<>();

        for (model.Road road : roadsInTrip) {
            stationsInTrip.add(model.Map.getStationByName(road.getSourceStationName()));
            stationsInTrip.add(model.Map.getStationByName(road.getDestStationName()));
        }
    }*/

    public TripOffer(String offeringUserName, int passengersQuota, SortedSet<Station> stations, double pricePreKm, TripTiming timing) {
        this.offeringUserName = offeringUserName;
        this.passengersQuota = passengersQuota;
        this.pricePerKm = pricePreKm;
        this.timing = timing;

        // TODO check that the roads between the stations exist
        this.stationsInTrip = stations;
        //this.roadsInTrip = model.Map.getRoadsBetweenStations(stations);
    }

    //region Getters & Setters
    public String getOfferingUserName() {
        return offeringUserName;
    }

    public void setOfferingUserName(String offeringUserName) {
        this.offeringUserName = offeringUserName;
    }

    public int getPassengersQuota() {
        return passengersQuota;
    }

    public void setPassengersQuota(int passengersQuota) {
        this.passengersQuota = passengersQuota;
    }

    public Set<Station> getStationsInTrip() {
        return stationsInTrip;
    }

    public double getPricePerKm() {
        return pricePerKm;
    }

    public void setPricePerKm(double pricePerKm) {
        this.pricePerKm = pricePerKm;
    }

    public TripTiming getTiming() {
        return timing;
    }

    public void setTiming(TripTiming timing) {
        this.timing = timing;
    }
    //endregion

    //region Public methods

    /**
     * Sums the duration of travel of every road in the trip, and returns the total amount of time in hours.
     * @return Duration of trip in hours.
     */
    public double getTripDuration() {
        double totalDuration = 0;

        for (Road road : roadsInTrip) {
            totalDuration += Road.calcRoadTravelDuration(road);
        }

        return totalDuration;
    }

    public double getTripPrice() {
        return Road.sumRoadsLength(roadsInTrip) * pricePerKm;
    }

    public double getGasUsage() {
        return Road.sumRoadsNeededGas(roadsInTrip) / roadsInTrip.size();
    }
    //endregion

    //region Private methods


    //endregion


}
