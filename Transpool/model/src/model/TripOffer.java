package model;

import java.util.List;
import java.util.Objects;

public class TripOffer {
    private String offeringUserName;
    private int passengersQuota;
    private List<Station> stationsInTrip;
    private List<Road> roadsInTrip;
    private double pricePerKm;
    private TripTiming timing;

    public TripOffer(String offeringUserName, int passengersQuota, double pricePreKm, TripTiming timing, List<Station> stations, List<Road> roads) {
        this.offeringUserName = offeringUserName;
        this.passengersQuota = passengersQuota;
        this.pricePerKm = pricePreKm;
        this.timing = timing;
        this.stationsInTrip = stations;
        this.roadsInTrip = roads;
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

    public List<Station> getStationsInTrip() {
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
     *
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TripOffer)) return false;

        TripOffer tripOffer = (TripOffer) o;

        if (getPassengersQuota() != tripOffer.getPassengersQuota()) return false;
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
        result = 31 * result + getPassengersQuota();
        result = 31 * result + (getStationsInTrip() != null ? getStationsInTrip().hashCode() : 0);
        result = 31 * result + (roadsInTrip != null ? roadsInTrip.hashCode() : 0);
        temp = Double.doubleToLongBits(getPricePerKm());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getTiming() != null ? getTiming().hashCode() : 0);
        return result;
    }
}
