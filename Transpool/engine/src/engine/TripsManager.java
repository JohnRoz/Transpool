package engine;

import model.TripOffer;
import model.TripRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * engine.TripsManager is a Util class, that managers the offers and requests of the trips
 */
public class TripsManager {
    private Set<TripOffer> offers;
    private Set<TripRequest> requests;

    public TripsManager(Set<TripOffer> offers) {
        this.offers = offers;
        this.requests = new HashSet<>();
    }

    public Set<TripOffer> getOffers() {
        return offers;
    }

    public boolean addOffer(TripOffer offer) {
        return offers.add(offer);
    }

    public Set<TripRequest> getRequests() {
        return requests;
    }

    public Set<TripRequest> getUnmatchedRequests() {
        return getRequests().stream().filter(TripRequest::isMatched).collect(Collectors.toSet());
    }

    public boolean addRequest(TripRequest req) {
        return requests.add(req);
    }

    public void matchRequestToOffer(TripRequest req, TripOffer off) {
        req.setMatchedTo(off);
        off.registerRequest(req);
    }

    public List<TripOffer> getPossibleMatches(TripRequest request) {
        return getOffers().stream()
                .filter(offer ->
                        offer.getRemainingPassengersCapacity() > 0 &&
                                offer.getStationsInTrip().contains(request.getWantedSourceStation()) &&
                                offer.getStationsInTrip().contains(request.getWantedDestStation()) &&
                                offer.getStationsInTrip().indexOf(request.getWantedSourceStation()) <
                                        offer.getStationsInTrip().indexOf(request.getWantedDestStation()) &&
                                offer.getWhenAtStation(request.getWantedSourceStation()).equals(request.getWantedTripStartTime())
                )
                .collect(Collectors.toList());
    }
}
