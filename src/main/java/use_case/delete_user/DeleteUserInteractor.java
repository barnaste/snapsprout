package use_case.delete_user;

import entity.Plant;
import use_case.ImageDataAccessInterface;
import use_case.PlantDataAccessInterface;
import use_case.UserDataAccessInterface;

import java.util.List;


/**
 * The Delete User Interactor.
 */
public class DeleteUserInteractor implements DeleteUserInputBoundary {
    private final PlantDataAccessInterface plantDataAccessObject;
    private final ImageDataAccessInterface imageDataAccessObject;
    private final UserDataAccessInterface userDataAccessObject;
    private final DeleteUserOutputBoundary deleteUserPresenter;

    private Runnable escapeMap;

    public DeleteUserInteractor(PlantDataAccessInterface plantDataAccessObject,
                                ImageDataAccessInterface imageDataAccessObject,
                                UserDataAccessInterface userDataAccessObject,
                                DeleteUserOutputBoundary deleteUserOutputBoundary) {
        this.plantDataAccessObject = plantDataAccessObject;
        this.imageDataAccessObject = imageDataAccessObject;
        this.userDataAccessObject = userDataAccessObject;
        this.deleteUserPresenter = deleteUserOutputBoundary;
    }

    @Override
    public void execute(DeleteUserInputData deleteUserInputData) {
        final String tempusername = deleteUserInputData.getUsername();
        final String temppassword = deleteUserInputData.getPassword();
        String username = userDataAccessObject.getCurrentUsername();
        String password = userDataAccessObject.getUser(username).getPassword();

        // Validate the input
        if (username.equals(tempusername) && password.equals(temppassword)) {
            // Proceed with deletion
            //GRAB THE PLANTS
            List<Plant> plants = plantDataAccessObject.getUserPlants(username);
            for (Plant plant : plants) {
                //DELETE IMAGES
                imageDataAccessObject.deleteImage(plant.getImageID());
                //THEN PLANTS
                plantDataAccessObject.deletePlant(plant.getFileID());
            }
            //THEN USER
            userDataAccessObject.deleteUser(username);
            //LOGOUT
            userDataAccessObject.setCurrentUsername(null);
            //final DeleteUserOutputData deleteUserOutputData = new DeleteUserOutputData(username);
            //GO TO WELCOME VIEW
            deleteUserPresenter.prepareSuccessView();
            escape();
        } else {
            // Show an error message if validation fails
            deleteUserPresenter.prepareFailView("Invalid credentials. Try again.");
        }
    }

    public void setEscapeMap(Runnable escapeMap) {
        this.escapeMap = escapeMap;
    }

    public void escape() {
        this.escapeMap.run();
    }
}
