package model;

import model.CustomExceptions.RoadDoesNotExistException;

import javax.naming.OperationNotSupportedException;
import java.util.Set;
import java.util.SortedSet;

/**
 * This is the object that represents the projection of the world
 * It is a Singleton
 */
public class Map {

    //region Non-static constants
    final int HEIGHT;
    final int WIDTH;
    //endregion

    //region Members

    // Singleton instance
    private static Map instance;

    private static Set<Road> roads;
    private static Set<Station> stations;
    //endregion

    //region Ctor
    private Map(int height, int width, Set<Road> roads, Set<Station> stations) {
        HEIGHT = height;
        WIDTH = width;
        this.roads = roads;
        this.stations = stations;
    }
    //endregion

    /**
     * Singleton getter.
     *
     * @return Returns the instance of the model.Map Singleton.
     */
    public static Map getInstance() {
        return instance;
    }

    /**
     * First initialization of the model.Map object.
     * Builds the object by calling to the {@link #getInstance()} method and instantiates
     * the roads and stations sets.
     * Should be called only once at the startup of the app.
     *
     * @param roads    The list of roads to instantiate the map with.
     * @param stations The list of stations to instantiate the map with.
     * @return The instantiated reference to the new model.Map Singleton object
     * @throws OperationNotSupportedException If the model.Map Singleton has been already initialized.
     */
    public static Map init(int height, int width, Set<Road> roads, Set<Station> stations) throws OperationNotSupportedException {
        if (instance != null)
            throw new OperationNotSupportedException();

        return instance = new Map(height, width, roads, stations);
    }

    // TODO Move to TripsManager
    public static Station getStationByName(String name) {
        for (Station station :
                stations) {
            if (station.getName().equals(name))
                return station;
        }

        return null;
    }

    private static boolean doesRoadExist(Road road) {
        return roads.contains(road);
    }

    // TODO Complete the Mock
    public static SortedSet<Road> getRoadsBetweenStations(SortedSet<Station> stations) throws RoadDoesNotExistException {
        //SortedSet<model.Road> roadsBetweenStations = new
        return null;
    }

}
