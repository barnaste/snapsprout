package use_case.delete_user;

import data_access.InMemoryImageDataAccessObject;
import data_access.InMemoryPlantDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import entity.Plant;
import entity.User;
import org.bson.types.ObjectId;
import org.junit.Test;
import use_case.PlantDataAccessInterface;

import static org.junit.Assert.*;

public class DeleteUserInteractorTest {
    @Test
    public void TestDeleteUser() {
        InMemoryPlantDataAccessObject plantDAO = InMemoryPlantDataAccessObject.getInstance();
        InMemoryImageDataAccessObject imageDAO = InMemoryImageDataAccessObject.getInstance();
        InMemoryUserDataAccessObject userDAO = InMemoryUserDataAccessObject.getInstance();
        // make a new user
        User user = new User();
        user.setUsername("test");
        user.setPassword("test");

        userDAO.setCurrentUsername(user.getUsername());
        userDAO.addUser(user);

        //make a new plant
        Plant plant = new Plant();
        ObjectId plantID = new ObjectId();
        plant.setFileID(plantID);
        DeleteUserInputData inputData = new DeleteUserInputData(user.getUsername(), user.getPassword());

        //upload the plant to the database under the user
        plant.setOwner(user.getUsername());
        PlantDataAccessInterface plantRepository = InMemoryPlantDataAccessObject.getInstance();
        plantRepository.addPlant(plant);

        //delete plants
        DeleteUserOutputBoundary successPresenter = new DeleteUserOutputBoundary() {
            @Override
            public void prepareSuccessView() {
                assertFalse(userDAO.existsByUsername("test"));
                assertEquals(plantRepository.getNumberOfUserPlants("test"), 0);
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail();
            }
        };

        DeleteUserInputBoundary interactor = new DeleteUserInteractor(plantDAO, imageDAO, userDAO, successPresenter);
        interactor.execute(inputData);
    }
    @Test
    public void deleteNullUser(){

    }
    @Test
    public void deleteUserFailView(){
        InMemoryPlantDataAccessObject plantDAO = InMemoryPlantDataAccessObject.getInstance();
        InMemoryImageDataAccessObject imageDAO = InMemoryImageDataAccessObject.getInstance();
        InMemoryUserDataAccessObject userDAO = InMemoryUserDataAccessObject.getInstance();
        // make a new user
        User user = new User();
        user.setUsername("test");
        user.setPassword("test");

        userDAO.setCurrentUsername(user.getUsername());
        userDAO.addUser(user);

        //make a new plant
        Plant plant = new Plant();
        ObjectId plantID = new ObjectId();
        plant.setFileID(plantID);
        DeleteUserInputData inputData = new DeleteUserInputData(user.getUsername(), user.getPassword());

        //upload the plant to the database under the user
        plant.setOwner(user.getUsername());
        PlantDataAccessInterface plantRepository = InMemoryPlantDataAccessObject.getInstance();
        plantRepository.addPlant(plant);

        //delete plants
        DeleteUserOutputBoundary successPresenter = new DeleteUserOutputBoundary() {
            @Override
            public void prepareSuccessView() {
                assertFalse(userDAO.existsByUsername("test"));
                assertEquals(plantRepository.getNumberOfUserPlants("test"), 0);
            }

            @Override
            public void prepareFailView(String errorMessage) {
                asser
            }
        };

        DeleteUserInputBoundry interactor = new DeleteUserInteractor(plantDAO, imageDAO, userDAO, successPresenter);
        interactor.execute(inputData);
    }


}
