package model.Interfaces;

import model.CustomExceptions.RoadDoesNotExistException;
import model.CustomExceptions.StationDoesNotExistException;
import model.CustomExceptions.TranspoolXmlValidationException;
import model.CustomExceptions.UnsupportedFileTypeException;
import model.Enums.RepetitionRate;
import model.Road;
import model.Station;
import model.TripOffer;
import model.TripRequest;

import javax.naming.OperationNotSupportedException;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.time.DateTimeException;
import java.util.Collection;
import java.util.List;

public interface IEngine {
    void readXmlFile(String path)
            throws FileNotFoundException, UnsupportedFileTypeException, JAXBException, TranspoolXmlValidationException;

    Collection<Station> getAllStations() throws OperationNotSupportedException;

    Collection<Road> getAllRoads() throws OperationNotSupportedException;

    void postTripRequest(String user, String srcStation, String dstStation, int hour, int minutes) throws OperationNotSupportedException, StationDoesNotExistException, DateTimeException;

    void postTripOffer(String user, List<String> stationNames, int day, int hour, int minutes, String repetitionRate, int ppk, int capacity) throws OperationNotSupportedException, StationDoesNotExistException, DateTimeException, RoadDoesNotExistException;

    Collection<TripOffer> getAllTripOffers() throws OperationNotSupportedException;

    Collection<TripRequest> getAllTripRequests() throws OperationNotSupportedException;

    Collection<TripRequest> getUnmatchedTripRequests() throws OperationNotSupportedException;

    List<TripOffer> getAllMatchedToRequest(TripRequest request) throws OperationNotSupportedException;

    void matchTripRequestToOffer(TripRequest request, TripOffer SelectedMatch) throws OperationNotSupportedException;
}
