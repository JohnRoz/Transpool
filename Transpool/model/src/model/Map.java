package model;

import model.CustomExceptions.RoadDoesNotExistException;

import javax.naming.OperationNotSupportedException;
import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

/**
 * This is the object that represents the projection of the world
 * It is a Singleton
 */
public class Map {

    //region Public Constants
    public static final int MAX_MAP_SCALE = 100;
    public static final int MIN_MAP_SCALE = 6;
    public static final int MAX_LENGTH = MAX_MAP_SCALE;
    public static final int MAX_WIDTH = MAX_MAP_SCALE;
    public static final int MIN_LENGTH = MIN_MAP_SCALE;
    public static final int MIN_WIDTH = MIN_MAP_SCALE;
    //endregion

    //region Non-static constants
    public final int LENGTH;
    public final int WIDTH;
    //endregion

    //region Members

    // Singleton instance
    private static Map instance;

    private static Set<Road> roads;
    private static Set<Station> stations;
    //endregion

    //region Ctor
    private Map(int length, int width, Set<Station> stations, Set<Road> roads) {
        LENGTH = length;
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
     * @param stations The list of stations to instantiate the map with.
     * @param roads    The list of roads to instantiate the map with.
     * @return The instantiated reference to the new model.Map Singleton object
     * @throws OperationNotSupportedException If the model.Map Singleton has been already initialized.
     */
    public static Map init(int length, int width, Set<Station> stations, Set<Road> roads)
            throws OperationNotSupportedException {
        if (instance != null)
            throw new OperationNotSupportedException(
                    "Map cannot be initialized more than once.\nUse getInstance() to get a reference to the Map instead."
            );

        return instance = new Map(length, width, stations, roads);
    }

    public static Set<Road> getRoads() {
        return roads;
    }

    public static Set<Station> getStations() {
        return stations;
    }

    public boolean isOnMap(Point point) {
        int pntWidth = point.x;
        int pntLength = point.y;

        return pntLength >= 0 && pntLength <= LENGTH &&
                pntWidth >= 0 && pntWidth <= WIDTH;
    }

    public static boolean isOnMap(int mapLength, int mapWidth, Point point) {
        int pntWidth = point.x;
        int pntLength = point.y;

        return pntLength >= 0 && pntLength <= mapLength &&
                pntWidth >= 0 && pntWidth <= mapWidth;
    }

}
