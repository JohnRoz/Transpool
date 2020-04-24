package model.Interfaces;

import model.CustomExceptions.StationDoesNotExistException;
import model.CustomExceptions.TranspoolXmlValidationException;
import model.CustomExceptions.UnsupportedFileTypeException;
import model.Station;
import model.TripOffer;
import model.TripRequest;

import javax.naming.OperationNotSupportedException;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.time.DateTimeException;
import java.util.Collection;

public interface IEngine {
    void readXmlFile(String path)
            throws FileNotFoundException, UnsupportedFileTypeException, JAXBException, TranspoolXmlValidationException;

    Collection<Station> getAllStations() throws OperationNotSupportedException;

    void postTripRequest(String user, String srcStation, String dstStation, int hour, int minutes) throws OperationNotSupportedException, StationDoesNotExistException, DateTimeException;

    Collection<TripOffer> getAllTripOffers() throws OperationNotSupportedException;

    Collection<TripRequest> getAllTripRequests() throws OperationNotSupportedException;
}
