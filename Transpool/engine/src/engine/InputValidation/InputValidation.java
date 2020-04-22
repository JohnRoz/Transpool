package engine.InputValidation;

import model.CustomExceptions.StationDoesNotExistException;
import model.Map;
import model.Station;

public class InputValidation {
    public static void assertStationsExist(String... stationNames) throws StationDoesNotExistException {
        for (String stationName : stationNames) {
            if (!Map.getInstance().hasStation(stationName))
                throw new StationDoesNotExistException(stationName);
        }
    }
}
