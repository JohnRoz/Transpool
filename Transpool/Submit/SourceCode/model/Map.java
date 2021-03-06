package model;

import model.CustomExceptions.RoadDoesNotExistException;
import model.CustomExceptions.StationDoesNotExistException;

import javax.naming.OperationNotSupportedException;
import java.awt.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;

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

    private final Set<Road> roads;
    private final Set<Station> stations;
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
                    "Map cannot be initialized more than once.\n" +
                            "Use getInstance() to get a reference to the Map instead.\n" +
                            "In Order to recreate a Map, you have to call reset() first."
            );

        return instance = new Map(length, width, stations, roads);
    }

    public static void reset() {
        instance = null;
    }

    public Set<Road> getRoads() {
        return roads;
    }

    public Set<Station> getStations() {
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

    public boolean hasStation(String stationName) {
        return Station.containsStationByName(this.stations, stationName);
    }

    public Station getStation(String stationName) throws StationDoesNotExistException {
        return Station.getStationByName(this.stations, stationName);
    }

    public Station getStationIfExists(String stationName) {
        if (hasStation(stationName)) {
            try {
                return getStation(stationName);
            } catch (StationDoesNotExistException e) {
                // Never happens Since i checked it exist
                e.printStackTrace();
            }
        }

        return null;
    }

    public List<Station> getStationsByNames(List<String> stationNames) throws StationDoesNotExistException {
        List<Station> stations = new ArrayList<>();
        for (String stationName : stationNames) {
            stations.add(getStation(stationName));
        }

        return stations;
    }

    public boolean hasRoad(String srcStation, String dstStation) {
        return Road.containsRoadBySrcAndDstNames(this.roads, srcStation, dstStation);
    }

    public Road getRoad(String srcStation, String dstStation) throws RoadDoesNotExistException{
        return Road.getRoadBySrcDst(this.roads, srcStation, dstStation);
    }

    public List<Road> getRoadsByStationsList(List<Station> stations) throws RoadDoesNotExistException {
        List<Road> roads = new ArrayList<>();
        for (int i = 0; i < stations.size() - 1; i++) {
            Station srcStation = stations.get(i);
            Station dstStation = stations.get(i + 1);

            roads.add(getRoad(srcStation.getName(), dstStation.getName()));
        }

        return roads;
    }
}
