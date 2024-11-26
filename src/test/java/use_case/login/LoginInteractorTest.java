package use_case.login;
import data_access.InMemoryUserDataAccessObject;
import entity.User;
import org.junit.Test;
import use_case.UserDataAccessInterface;

import static org.junit.Assert.*;

public class LoginInteractorTest {

    @Test
    public void successTest() {
        // create user and save to DAO
        UserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();
        User user = new User("arz", "123");
        userRepository.addUser(user);

        // create input data with correct password
        LoginInputData inputData = new LoginInputData("arz", "123");

        // construct temporary presenter
        LoginOutputBoundary successPresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                // the current user should be "arz"
                assertEquals("arz", user.getUsername());
            }

            @Override
            public void prepareFailView() {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                // the use case should not fail since the password was correct
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToStartView() {}
        };

        // execute the use case
        LoginInputBoundary interactor = new LoginInteractor(userRepository, successPresenter);
        interactor.execute(inputData);
    }


    @Test
    public void failurePasswordMismatchTest() {
        // create a new user and save to DAO
        UserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();
        User user = new User("arz", "123");
        userRepository.addUser(user);

        // create input data with incorrect password
        LoginInputData inputData = new LoginInputData("arz", "wrong");

        // construct temporary presenter
        LoginOutputBoundary failurePresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                // the use case should fail since the password was incorrect
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                // check that the error message is correct
                assertEquals("Incorrect password for \"arz\".", error);
            }

            @Override
            public void switchToStartView() {}
        };

        // execute use case
        LoginInputBoundary interactor = new LoginInteractor(userRepository, failurePresenter);
        interactor.execute(inputData);
    }

    @Test
    public void failureUserDoesNotExistTest() {
        // initialize an empty DAO (do not save a user for this test)
        UserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // create input data for any (non-existent) user
        LoginInputData inputData = new LoginInputData("arz", "123");

        // construct temporary presenter
        LoginOutputBoundary failurePresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                // use case should fail since user does not exist
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                // check that the error message is correct
                assertEquals("arz: Account does not exist.", error);
            }

            @Override
            public void switchToStartView() {}
        };

        // execute use case
        LoginInputBoundary interactor = new LoginInteractor(userRepository, failurePresenter);
        interactor.execute(inputData);
    }
}
