package engine;

import engine.DAL.transpoolXMLSchema.MapBoundries;
import engine.DAL.transpoolXMLSchema.MapDescriptor;
import engine.DAL.transpoolXMLSchema.PlannedTrips;
import model.Map;
import model.Road;
import model.Station;
import model.TripOffer;

import javax.naming.OperationNotSupportedException;
import java.util.Set;

public class TranspoolManager {

    private static TranspoolManager instance;
    private Map map;
    private TripsManager tripsManager;

    public TranspoolManager(MapBoundries mapBoundries, Set<Station> stations, Set<Road> roads, Set<TripOffer> tripOffers) {
        int mapLength = mapBoundries.getLength();
        int mapWidth = mapBoundries.getWidth();

        try {
            map = Map.init(mapLength, mapWidth, stations, roads);
        } catch (OperationNotSupportedException e) {
            e.printStackTrace();
        }

        tripsManager = new TripsManager(tripOffers);
    }

    public Map getMap() {
        return map;
    }

    public TripsManager getTripsManager() {
        return tripsManager;
    }
}
