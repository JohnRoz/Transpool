package engine;

import engine.DAL.TranspoolXmlLoader;
import model.*;
import model.CustomExceptions.RoadDoesNotExistException;
import model.CustomExceptions.StationDoesNotExistException;
import model.CustomExceptions.TranspoolXmlValidationException;
import model.CustomExceptions.UnsupportedFileTypeException;
import model.Interfaces.IEngine;

import javax.naming.OperationNotSupportedException;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static engine.InputValidation.InputValidation.assertStationsExist;

public class Engine implements IEngine {

    public static TranspoolManager transpoolManager;

    public Engine() {

    }

    //region Private Methods
    private void assertEngineInitialized() throws OperationNotSupportedException {
        if (transpoolManager == null)
            throw new OperationNotSupportedException(
                    "The action cannot be completed since the system has no data.\n" +
                            "Try loading a Transpool™ xml file first.\n"
            );
    }
    //endregion

    @Override
    public void readXmlFile(String path)
            throws FileNotFoundException, UnsupportedFileTypeException,
            JAXBException, TranspoolXmlValidationException {
        transpoolManager = TranspoolXmlLoader.Load(path);
    }

    @Override
    public Collection<Station> getAllStations() throws OperationNotSupportedException {
        assertEngineInitialized();
        return transpoolManager.getMap().getStations();
    }

    @Override
    public Collection<Road> getAllRoads() throws OperationNotSupportedException {
        assertEngineInitialized();
        return transpoolManager.getMap().getRoads();
    }

    @Override
    public void postTripRequest(String userName, String srcStation, String dstStation, int hour, int minutes) throws OperationNotSupportedException, StationDoesNotExistException, DateTimeException {
        assertEngineInitialized();
        assertStationsExist(srcStation, dstStation);

        // Assert correct hours and minutes before creating user
        LocalTime time = LocalTime.of(hour, minutes);

        User reqOwner = transpoolManager.getUserIfExists(userName);
        TripRequest tripRequest =
                new TripRequest(reqOwner, srcStation, dstStation, time);

        transpoolManager.getTripsManager().addRequest(tripRequest);
    }

    @Override
    public void postTripOffer(String user, List<String> stationNames, int day, int hour, int minutes, String repetitionRate, int ppk, int capacity) throws OperationNotSupportedException, StationDoesNotExistException, DateTimeException, RoadDoesNotExistException {
        assertEngineInitialized();
        assertStationsExist(stationNames.toArray(new String[0]));

        // Assert correct hours and minutes before creating user
        TripTiming timing = new TripTiming(day, hour, minutes, repetitionRate);
        User offOwner = transpoolManager.getUserIfExists(user);
        List<Station> stations = Map.getInstance().getStationsByNames(stationNames);
        List<Road> roads = Map.getInstance().getRoadsByStationsList(stations);
        TripOffer tripOffer = new TripOffer(user, capacity, ppk, timing, stations, roads);

        transpoolManager.getTripsManager().addOffer(tripOffer);
    }

    @Override
    public Collection<TripOffer> getAllTripOffers() throws OperationNotSupportedException {
        assertEngineInitialized();
        return transpoolManager.getTripsManager().getOffers();
    }

    @Override
    public Collection<TripRequest> getAllTripRequests() throws OperationNotSupportedException {
        assertEngineInitialized();
        return transpoolManager.getTripsManager().getRequests();
    }

    @Override
    public Collection<TripRequest> getUnmatchedTripRequests() throws OperationNotSupportedException {
        assertEngineInitialized();
        return transpoolManager.getTripsManager().getUnmatchedRequests();
    }

    @Override
    public List<TripOffer> getAllMatchedToRequest(TripRequest request) throws OperationNotSupportedException {
        assertEngineInitialized();
        return transpoolManager.getTripsManager().getPossibleMatches(request);
    }

    @Override
    public void matchTripRequestToOffer(TripRequest request, TripOffer selectedMatch) throws OperationNotSupportedException {
        assertEngineInitialized();
        transpoolManager.getTripsManager().matchRequestToOffer(request, selectedMatch);
    }
}
