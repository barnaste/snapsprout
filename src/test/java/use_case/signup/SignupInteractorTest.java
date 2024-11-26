package use_case.signup;

import data_access.InMemoryUserDataAccessObject;
import entity.User;
import org.junit.Test;
import use_case.UserDataAccessInterface;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the SignupInteractor class.
 */
public class SignupInteractorTest {
    @Test
    public void successTest() {

        SignupInputData signupInputData = new SignupInputData("newUser", "password123", "password123");
        UserDataAccessInterface userRepository = InMemoryUserDataAccessObject.getInstance();
        SignupOutputBoundary successPresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                User addedUser = userRepository.getUser("newUser");
                assertNotNull(addedUser);
                assertEquals("newUser", addedUser.getUsername());
                assertEquals("password123", addedUser.getPassword());
                assertEquals("newUser", outputData.getUsername());
                assertFalse(outputData.isUseCaseFailed());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected.");

            }

            @Override
            public void switchToLoginView() {
            }

            @Override
            public void switchToStartView() {
            }
        };


        SignupInputBoundary interactor = new SignupInteractor(userRepository, successPresenter);
        interactor.execute(signupInputData);
    }

    @Test
    public void failureUserAlreadyExistsTest() {

        SignupInputData inputData = new SignupInputData("newUser", "password123", "password123");
        UserDataAccessInterface userRepository = InMemoryUserDataAccessObject.getInstance();
        userRepository.addUser(new User("existingUser", "password123"));
        SignupOutputBoundary failurePresenter = new SignupOutputBoundary() {

            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("User already exists.", errorMessage);

            }

            @Override
            public void switchToLoginView() {
            }

            @Override
            public void switchToStartView() {
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(userRepository, failurePresenter);
        interactor.execute(inputData);


    }

    @Test
    public void failurePasswordsDoNotMatchTest() {

        SignupInputData inputData = new SignupInputData("newUser2", "password123", "password1234");
        UserDataAccessInterface userRepository = InMemoryUserDataAccessObject.getInstance();
        SignupOutputBoundary failurenotmatchPresenter = new SignupOutputBoundary() {


            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Passwords don't match.", errorMessage);

            }

            @Override
            public void switchToLoginView() {
            }

            @Override
            public void switchToStartView() {
            }
        };
        SignupInputBoundary interactor = new SignupInteractor(userRepository, failurenotmatchPresenter);
        interactor.execute(inputData);

    }

    @Test
    public void failureEmptyUsernameAndPasswordTest() {

        SignupInputData inputData = new SignupInputData("", "", "");
        UserDataAccessInterface userRepository = InMemoryUserDataAccessObject.getInstance();
        SignupOutputBoundary failureemptyPresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Username and / or password cannot be empty.", errorMessage);

            }

            @Override
            public void switchToLoginView() {
            }

            @Override
            public void switchToStartView() {
            }
        };
        SignupInputBoundary interactor = new SignupInteractor(userRepository, failureemptyPresenter);
        interactor.execute(inputData);
    }

    @Test
    public void failureEmptyUsernameTest() {

        SignupInputData inputData = new SignupInputData("", "Hi", "Hi");
        UserDataAccessInterface userRepository = InMemoryUserDataAccessObject.getInstance();
        SignupOutputBoundary failureemptyPresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Username and / or password cannot be empty.", errorMessage);

            }

            @Override
            public void switchToLoginView() {
            }

            @Override
            public void switchToStartView() {
            }
        };
        SignupInputBoundary interactor = new SignupInteractor(userRepository, failureemptyPresenter);
        interactor.execute(inputData);
    }


    @Test
    public void failureEmptyPasswordTest() {

        SignupInputData inputData = new SignupInputData("user123", "", "");
        UserDataAccessInterface userRepository = InMemoryUserDataAccessObject.getInstance();
        SignupOutputBoundary failureemptyPresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Username and / or password cannot be empty.", errorMessage);

            }

            @Override
            public void switchToLoginView() {
            }

            @Override
            public void switchToStartView() {
            }
        };
        SignupInputBoundary interactor = new SignupInteractor(userRepository, failureemptyPresenter);
        interactor.execute(inputData);
    }

    @Test
    public void testSwitchToLoginView() {

        UserDataAccessInterface userRepository = InMemoryUserDataAccessObject.getInstance();
        SignupOutputBoundary loginViewPresenter = new SignupOutputBoundary() {

            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                fail("prepareSuccessView should not be called in this test.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("prepareFailView should not be called in this test.");
            }

            @Override
            public void switchToLoginView() {
                // If this method is called, the test should pass
                // No additional assert required here because its invocation means it works
            }

            @Override
            public void switchToStartView() {
                fail("switchToStartView should not be called in this test.");
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(userRepository, loginViewPresenter);

        interactor.switchToLoginView();
    }

    @Test
    public void testSwitchToStartView() {
        UserDataAccessInterface userRepository = InMemoryUserDataAccessObject.getInstance();
        SignupOutputBoundary startViewPresenter = new SignupOutputBoundary() {

            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                fail("prepareSuccessView should not be called in this test.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("prepareFailView should not be called in this test.");
            }

            @Override
            public void switchToLoginView() {
                fail("switchToLoginView should not be called in this test.");
            }

            @Override
            public void switchToStartView() {
                // If this method is called, the test should pass
                // No additional assert required here because the test will pass if this method is invoked
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(userRepository, startViewPresenter);

        interactor.switchToStartView();
    }
}