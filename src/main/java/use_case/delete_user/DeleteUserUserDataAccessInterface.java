package use_case.delete_user;

import entity.Plant;
import entity.User;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * DAO for the Delete User Use Case.
 */
public interface DeleteUserUserDataAccessInterface {

    /**
     * Returns the username of the curren user of the application.
     *
     * @return the username of the current user; null indicates that no one is logged into the application.
     */
    String getCurrentUsername();

    /**
     * Sets the username indicating who is the current user of the application.
     *
     * @param username the new current username; null to indicate that no one is currently logged into the application.
     */
    void setCurrentUsername(String username);

    /**
     * Deletes a user from the application.
     *
     * @param username the username of the user to be deleted.
     *                 Must not be null or empty. If the username does not exist in the system,
     *                 the method should handle this gracefully.
     * @return true if the user was successfully deleted; false if the user does not exist
     * or the operation could not be completed.
     */
    boolean deleteUser(String username);

    List<Plant> getUserPlants(String username);

    boolean deleteImage(String Id);

    boolean deletePlant(ObjectId fileID);
}
