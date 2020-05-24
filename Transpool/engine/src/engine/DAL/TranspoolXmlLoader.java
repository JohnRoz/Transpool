package engine.DAL;

import engine.DAL.transpoolXMLSchema.*;
import engine.TranspoolManager;
import model.CustomExceptions.FormattedMessageException;
import model.CustomExceptions.StationDoesNotExistException;
import model.CustomExceptions.TranspoolXmlValidationException;
import model.CustomExceptions.UnsupportedFileTypeException;
import model.*;
import model.util.collections.Graph;

import javax.naming.OperationNotSupportedException;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static model.Extensions.FileExtensions.getFileExtension;

public class TranspoolXmlLoader {
    private static final String SUPPORTED_FILE_TYPE = "xml";
    private static final String ROUTE_PATH_SEPARATOR = ",";

    /**
     * The main function that loads a transpool xml file to the system.
     * This is the public entry point to be called from the Engine class to take care of the
     * ReadXmlFile UserAction
     *
     * @param path The path to the file that the user wishes to load.
     * @throws FileNotFoundException           In case the file in the specified path doesn't exist.
     * @throws UnsupportedFileTypeException    In case the file in the specified path isn't a xml file.
     * @throws JAXBException                   In case the JAXB lib can't load the file correctly
     *                                         (file structure not matching schema etc.)
     * @throws TranspoolXmlValidationException In case the content of the xml file is
     *                                         invalid according to the system's requirements
     */
    public static TranspoolManager Load(String path) throws FileNotFoundException, UnsupportedFileTypeException, JAXBException, TranspoolXmlValidationException {
        File file = assertFilePath(path);
        TransPool xmlRoot = TranspoolXmlSerializer.deserialize(file);

        return PopulateModels(xmlRoot);
    }

    /**
     * Runs validation tests for the content of the xml file.
     * If all tests pass, instantiates the models of the system.
     *
     * @param xmlRoot The xmlRoot object of the xml DOM that was loaded.
     * @throws TranspoolXmlValidationException In case at least one of the validation tests fail
     *                                         , specifying the cause in the message field.
     */
    private static TranspoolManager PopulateModels(TransPool xmlRoot) throws TranspoolXmlValidationException {
        MapBoundries boundaries = xmlRoot.getMapDescriptor().getMapBoundries();

        // The boundaries of the map must be checked first, because the other objects
        // depend on the map (validation of stations coordinates etc.)
        assertMapBoundaries(boundaries);

        List<Stop> stops = xmlRoot.getMapDescriptor().getStops().getStop();

        assertStationsInMapRange(boundaries, stops);
        Graph<Station, Road> stationsGraph = createStationsGraph(stops);

        List<Path> paths = xmlRoot.getMapDescriptor().getPaths().getPath();
        createRoads(paths, stationsGraph);

        List<TransPoolTrip> xmlTrips = xmlRoot.getPlannedTrips().getTransPoolTrip();
        Set<TripOffer> tripOffers = createTripOffers(stationsGraph, xmlTrips);

        // In case the transpoolManager is being recreated (This is not the first file loaded).
        TranspoolManager.reset();
        try {
            return TranspoolManager.init(boundaries, stationsGraph, tripOffers);
        } catch (OperationNotSupportedException e) {
            // This shouldn't ever happen since a called reset before calling init
            e.printStackTrace();
            return TranspoolManager.getInstance();
        }
    }

    //region Technical File Structure Validation

    /**
     * Receives a path and checks if the file exists and if it's of a supported type.
     *
     * @param path The path of the fiel to check if it exists and if it's an XML file.
     * @return In case the function didn't throw an exception - returns the files described by the given path.
     * @throws FileNotFoundException        - Self explanatory.
     * @throws UnsupportedFileTypeException In case the type of the file that was asked to be loaded is not supported.
     */
    private static File assertFilePath(String path)
            throws FileNotFoundException, UnsupportedFileTypeException {
        File file = new File(path);

        if (!file.exists())
            throw new FileNotFoundException("The specified file does not exist.");

        String extension = getFileExtension(file);
        if (!extension.equals(SUPPORTED_FILE_TYPE))
            throw new UnsupportedFileTypeException(
                    "The System can only load %s files.\nYou tried to load a %s file.",
                    SUPPORTED_FILE_TYPE, extension
            );
        return file;
    }
    //endregion

    /**
     * Checks the given {@link MapBoundries} if they're valid according to the requirements of the system.
     *
     * @param boundaries The boundaries loaded from the file to check against the system's
     *                   requirements (Max-Min height & width)
     * @return True if the loaded boundaries are within valid range. False otherwise.
     */
    private static void assertMapBoundaries(MapBoundries boundaries)
            throws TranspoolXmlValidationException {
        int length = boundaries.getLength();
        int width = boundaries.getWidth();

        final String expMsgPrefix =
                "The given boundaries of the map do not meet the requirements of the system.\n";

        if (length > Map.MAX_LENGTH)
            throw new TranspoolXmlValidationException(
                    expMsgPrefix + "The length of the map exceeds the max value of %d.",
                    Map.MAX_LENGTH
            );
        if (length < Map.MIN_LENGTH)
            throw new TranspoolXmlValidationException(
                    expMsgPrefix + "The length of the map falls short of the min value of %d.",
                    Map.MIN_LENGTH
            );
        if (width > Map.MAX_WIDTH)
            throw new TranspoolXmlValidationException(
                    expMsgPrefix + "The width of the map exceeds the max value of %d.",
                    Map.MAX_WIDTH
            );
        if (width < Map.MIN_WIDTH)
            throw new TranspoolXmlValidationException(
                    expMsgPrefix + "The width of the map falls short of the min value of %d.",
                    Map.MIN_WIDTH
            );
    }

    /**
     * Checks that the coordinate of each stop is located within the defined range of the map.
     *
     * @param boundaries The boundaries of the defined map.
     * @param stops      The list of stops to to assert that all of them are within teh map surface.
     * @throws TranspoolXmlValidationException If one of the stops is discovered outside the boundaries of the map.
     */
    private static void assertStationsInMapRange(MapBoundries boundaries, List<Stop> stops)
            throws TranspoolXmlValidationException {
        int mapLength = boundaries.getLength();
        int mapWidth = boundaries.getWidth();

        for (Stop stop : stops) {
            int stopWidth = stop.getX();
            int stopLength = stop.getY();
            if (!Map.isOnMap(mapLength, mapWidth, new Point(stopWidth, stopLength)))
                throw new TranspoolXmlValidationException(
                        "The station named %s is not within the boundaries of the map.",
                        stop.getName().trim()
                );
        }

    }

    private static Graph<Station, Road> createStationsGraph(List<Stop> stops)
            throws TranspoolXmlValidationException {
        Graph<Station, Road> stationsGraph = new Graph<>();

        for (Stop stop : stops) {
            String name = stop.getName().trim();
            int x = stop.getX();
            int y = stop.getY();

            assertOverlappingStations(stationsGraph.getVertices(), stop);

            // If the set already contained a station that's equal to the
            // current station (has equal name or equal coordinate)
            if (!stationsGraph.addVertexIfAbsent(new Station(name, x, y))) {
                throw new TranspoolXmlValidationException(
                        "A station with the name %s already exist.",
                        name, x, y
                );
            }
        }

        return stationsGraph;
    }

    /**
     * Checks if there is already a station in the stations set with the same coordinates
     * as the specified stop.
     *
     * @param stations The stations that exist in the map.
     * @param stop     The station to check if it is not overlapping any other station and can
     *                 be added to the set.
     * @throws TranspoolXmlValidationException In case there is already a station in the set
     *                                         that occupies the coordinate of the checked stop
     */
    private static void assertOverlappingStations(Set<Station> stations, Stop stop) throws TranspoolXmlValidationException {
        int x = stop.getX(), y = stop.getY();
        String stopName = stop.getName();

        if (Station.isStationOverlappingAnother(stations, x, y))
            throw new TranspoolXmlValidationException(
                    "Cannot add station %s to coordinate (%d, %d) since this coordinate is occupied by station %s.",
                    stopName, x, y, Station.getStationByCoordinate(stations, x, y).getName()
            );
    }


    /**
     * Checks the list of paths given in the file is valid, and returns a Set containing the
     * roads of the map.
     *
     * @param paths         The {@link List} of paths given from the file.
     * @param stationsGraph The data structure ({@link Graph>}) that contains the stations in the map.
     *                      the file specifies in case data in it is valid.
     * @throws TranspoolXmlValidationException In case the source or destination of at least one path do not exist in
     *                                         the stations Set, or, if two paths in the list have the same source and destination.
     */
    private static void createRoads(List<Path> paths, Graph<Station, Road> stationsGraph)
            throws TranspoolXmlValidationException {
        for (Path path : paths) {
            String srcStationName = path.getFrom().trim();
            String dstStationName = path.getTo().trim();

            Road newRoad = createRoadFromPath(path, stationsGraph.getVertices());

            // Check if the set already contains the new road (considers cases of two-way roads)
            if (stationsGraph.doesEdgeExist(newRoad)) {
                throw new TranspoolXmlValidationException(
                        "A road between these two stations, %s and %s, already exist in the system.",
                        srcStationName, dstStationName
                );
            }

            try {
                Station srcStation = Station.getStationByName(stationsGraph.getVertices(), srcStationName);
                Station dstStation = Station.getStationByName(stationsGraph.getVertices(), dstStationName);
                stationsGraph.addEdge(srcStation, dstStation, newRoad, !newRoad.isOneWay());
            } catch (StationDoesNotExistException e) {
                e.printStackTrace();
                // Won't happen, since I checked they exist in assertPathSrcAndDst
            }
        }
    }

    private static Road createRoadFromPath(Path path, Collection<Station> existingStations) throws TranspoolXmlValidationException {
        String srcStationName = path.getFrom().trim();
        String dstStationName = path.getTo().trim();

        assertPathSrcAndDst(existingStations, srcStationName, dstStationName);


        boolean isOneWay = path.isOneWay();
        int pathLen = path.getLength();
        int fuelCons = path.getFuelConsumption();
        int speedLim = path.getSpeedLimit();

        return new Road(srcStationName, dstStationName, isOneWay, pathLen, fuelCons, speedLim);
    }

    /**
     * Checks for a given path that its source and destination Stations exist in the given
     * stations Set.
     *
     * @param stations   The set of existing Stations in the system.
     * @param srcStation Path's source station to check that exist.
     * @param dstStation Path's destination station to check that exist.
     * @throws TranspoolXmlValidationException In case one of the source or destination stations
     *                                         of the path do not exist.
     */
    private static void assertPathSrcAndDst(Collection<Station> stations, String srcStation, String dstStation)
            throws TranspoolXmlValidationException {
        // If the stations Set doesn't contain the source or dest stations
        // - throw an exception with a matching description for each case
        if (!Station.containsStationByName(stations, srcStation))
            throw new TranspoolXmlValidationException(
                    "The source station on the path from %s to %s does not exist.",
                    srcStation, dstStation
            );
        if (!Station.containsStationByName(stations, dstStation))
            throw new TranspoolXmlValidationException(
                    "The destination station on the path from %s to %s does not exist.",
                    srcStation, dstStation
            );
    }

    /**
     * Checks that the list of trip offers from the file is valid, and returns a {@link Set} of the {@link TripOffer}s
     *
     * @param stationsGraph The {@link Graph} containing the stations and roads on the map.
     * @param xmlTrips      The list of trip offers given from the xml file.
     * @return A {@code Set<TripOffer>} containing the trip offers given in the file.
     * @throws TranspoolXmlValidationException In case the list of trip offers in the file in
     *                                         invalid i.e contains non-existent stations or roads etc.
     */
    private static Set<TripOffer> createTripOffers(Graph<Station, Road> stationsGraph, List<TransPoolTrip> xmlTrips)
            throws TranspoolXmlValidationException {
        Set<TripOffer> tripOffers = new HashSet<>();

        for (TransPoolTrip trip : xmlTrips) {
            Route tripRoute = trip.getRoute();
            Set<Station> stations = stationsGraph.getVertices();
            Set<Road> roads = stationsGraph.getEdges();

            assertTripRouteStations(stations, tripRoute);
            assertPlannedTripRoads(roads, tripRoute);

            TripOffer tripOffer = transPoolTripToTripOffer(trip, stations, roads);

            tripOffers.add(tripOffer);
        }

        return tripOffers;
    }

    /**
     * Receives the {@link Set} of stations that were loaded from the file,
     * and the {@link Route} of a trip from the file, and validates that all the stations it specifies actually exist.
     *
     * @param stations  The pool of {@link Station}s that were loaded from the file
     * @param tripRoute The route object that specifies the stations of the trip.
     * @throws TranspoolXmlValidationException In case the {@link Route}'s path contains a no-existent {@link Station}.
     */
    private static void assertTripRouteStations(Set<Station> stations, Route tripRoute)
            throws TranspoolXmlValidationException {
        String routePath = tripRoute.getPath();
        for (String stationName : routePath.split(ROUTE_PATH_SEPARATOR)) {
            if (!Station.containsStationByName(stations, stationName.trim()))
                throw new TranspoolXmlValidationException(
                        "Trip with Route %s contains a non-existent station %s.",
                        routePath, stationName
                );
        }
    }

    /**
     * Receives the {@link Set} of roads that were loaded from the file,
     * and the {@link Route} of a trip from the file, and validates that all the roads it specifies actually exist.
     *
     * @param roads     The pool of {@link Road}s that were loaded from the file
     * @param tripRoute The route object that specifies the stations of the trip in the road's order.
     * @throws TranspoolXmlValidationException In case the {@link Route}'s path contains a no-existent {@link Road}.
     */
    private static void assertPlannedTripRoads(Set<Road> roads, Route tripRoute)
            throws TranspoolXmlValidationException {
        String routePath = tripRoute.getPath();
        String[] routeStationNames = routePath.split(ROUTE_PATH_SEPARATOR);
        for (int i = 0; i < routeStationNames.length - 1; i++) {
            String srcStation = routeStationNames[i].trim();
            String dstStation = routeStationNames[i + 1].trim();

            if (!Road.containsRoadBySrcAndDstNames(roads, srcStation, dstStation))
                throw new TranspoolXmlValidationException(
                        "Trip with Route %s contains a non-existent road %s.",
                        routePath, srcStation + "-" + dstStation
                );
        }

    }

    private static TripOffer transPoolTripToTripOffer(TransPoolTrip trip, Set<Station> stations, Set<Road> roads) {
        // Save trip members in local vars for ease of access
        String owner = trip.getOwner(), routePath = trip.getRoute().getPath();
        int capacity = trip.getCapacity(), PPK = trip.getPPK();

        List<Station> tripStations = null;
        List<Road> tripRoads = null;
        String[] stationNames = routePath.split(ROUTE_PATH_SEPARATOR);
        try {
            tripStations = Station.getStationsFromStrArr(stations, stationNames);
            tripRoads = Road.getRoadListFromStationsPath(roads, stationNames);
        } catch (FormattedMessageException e) {
            // Would never happen here since the route path was already checked
            e.printStackTrace();
        }

        int day = trip.getScheduling().getDayStart();
        int hour = trip.getScheduling().getHourStart();
        int minute = 0; //trip.getScheduling().getMinuteStart(); TODO: Uncomment in next schema version.
        String repetition = trip.getScheduling().getRecurrences();

        TripTiming timing = new TripTiming(day, hour, minute, repetition);

        return new TripOffer(owner, capacity, PPK, timing, tripStations, tripRoads);
    }
    //endregion
}
