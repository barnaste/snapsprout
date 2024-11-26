package data_access;

import entity.User;

import java.util.HashMap;
import java.util.Map;


/**
 * In-memory implementation of the DAO for storing user data. This implementation does
 * NOT persist data between runs of the program.
 */

public class InMemoryUserDataAccessObject implements use_case.UserDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();

    private String currentUsername;

    private static InMemoryUserDataAccessObject instance;

    /**
     * The private constructor -- if a new instance of this class is to be requested, it should be done
     * by calling the getInstance() public method.
     */
    public InMemoryUserDataAccessObject() {}

    /**
     * The method used to retrieve an instance of this class. This way, the DAO is maintained as a singleton.
     */
    public static InMemoryUserDataAccessObject getInstance() {
        if (instance == null) {
            instance = new InMemoryUserDataAccessObject();
        }
        return instance;
    }

    @Override
    public boolean existsByUsername(String username) {
        return users.containsKey(username);
    }

    @Override
    public void addUser(User user) { users.put(user.getUsername(), user);}

    @Override
    public String getCurrentUsername() {
        return this.currentUsername;
    }

    @Override
    public void setCurrentUsername(String username) {this.currentUsername = username;}

    @Override
    public User getUser(String username) {return users.get(username);}

    @Override
    public boolean deleteUser(String username) {return users.remove(username) != null;}

    public void deleteAll() {users.clear();}
}
