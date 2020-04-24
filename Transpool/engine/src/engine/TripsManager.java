package engine;

import model.TripOffer;
import model.TripRequest;

import java.util.HashSet;
import java.util.Set;

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

    public boolean addRequest(TripRequest req) {
        return requests.add(req);
    }
}
