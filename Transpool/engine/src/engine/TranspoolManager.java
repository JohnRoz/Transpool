package engine;

import engine.DAL.transpoolXMLSchema.MapBoundries;
import model.*;
import model.CustomExceptions.UserAlreadyExistsException;

import javax.naming.OperationNotSupportedException;
import java.net.UnknownServiceException;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is a Singleton that holds a reference to the Map, and to the object that
 * manages the trip offers/requests
 */
public class TranspoolManager {

    private static TranspoolManager instance;
    private TripsManager tripsManager;
    private Set<User> users;

    private TranspoolManager(MapBoundries mapBoundries, Set<Station> stations, Set<Road> roads, Set<TripOffer> tripOffers) {
        int mapLength = mapBoundries.getLength();
        int mapWidth = mapBoundries.getWidth();

        try {
            Map.reset();
            Map.init(mapLength, mapWidth, stations, roads);
        } catch (OperationNotSupportedException e) {
            e.printStackTrace();
        }

        tripsManager = new TripsManager(tripOffers);
        users = new HashSet<>();
    }

    //region Singleton Init & Get
    public static TranspoolManager init(MapBoundries mapBoundries, Set<Station> stations, Set<Road> roads, Set<TripOffer> tripOffers) throws OperationNotSupportedException {
        if (instance != null)
            throw new OperationNotSupportedException(
                    "TranspoolManager cannot be initialized more than once.\n" +
                            "Use getInstance() to get a reference to the Map instead.\n" +
                            "In Order to recreate the TranspoolManager, you have to call reset() first."
            );
        return (instance = new TranspoolManager(mapBoundries, stations, roads, tripOffers));
    }

    public static TranspoolManager getInstance() {
        return instance;
    }

    public static void reset() {
        if (instance != null)
            instance = null;

        TripOffer.resetIds();
        TripRequest.resetIds();
        User.resetIds();
    }
    //endregion

    public Map getMap() {
        return Map.getInstance();
    }

    public TripsManager getTripsManager() {
        return tripsManager;
    }

    public Set<User> getUsers() {
        return users;
    }

    /**
     * Receives a name of a user and returns the user with that name if it exists,
     * or null if there is no such user.
     *
     * @param username The name of the user to get.
     * @return The user with the specified name.
     */
    public User getUserByName(String username) {
        for (User user : this.users) {
            if (user.equals(new User(username)))
                return user;
        }
        return null;
    }

    /**
     * Checks if a user with the specified name exist in the {@link Set} of {@link User}s
     * that the {@link TranspoolManager} holds.
     *
     * @param name The name of the user to check if it exists.
     * @return True if a user with the same name as specified exists in the system.
     * False otherwise.
     */
    public boolean hasUser(String name) {
        return users.contains(new User(name));
    }

    public User createUser(String username) throws UserAlreadyExistsException {
        return createUser(username, 0);
    }

    /**
     * Receives a username, and checks if it exists.
     * If it does, return it. Else create a new user with that name and return it.
     * @param username name of the User to search for.
     * @return If found - returns the specified user. Otherwise, Returns a newly
     * created user with the specified name.
     */
    public User getUserIfExists(String username) {
        try {
            return hasUser(username)
                    ? getUserByName(username)
                    : createUser(username);
        } catch (UserAlreadyExistsException e) {
            // Won't happen since i checked whether it exists or not before calling createUser()
            e.printStackTrace();
            return null;
        }
    }

    public User createUser(String username, double balance) throws UserAlreadyExistsException {
        User newUser = new User(username, balance);

        // If the add operation failed
        if (!users.add(newUser)) {
            throw new UserAlreadyExistsException(username);
        }

        return newUser;
    }
}
