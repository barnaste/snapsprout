package use_case.login;

import entity.User;
import use_case.UserDataAccessInterface;


/**
 * The Login Interactor.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final UserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(UserDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();

        if (!userDataAccessObject.existsByUsername(username)) {
            // user does not exist
            loginPresenter.prepareFailView(username + ": Account does not exist.");
        } else {
            // user exists
            loginPresenter.prepareFailView();
        }
        else {
            final String pwd = userDataAccessObject.getUser(username).getPassword();
            if (!password.equals(pwd)) {
                loginPresenter.prepareFailView();
            }
            else {
                // user exists but password is incorrect
                loginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
            }  else {
                // user exists and password is correct
                final User user = userDataAccessObject.getUser(loginInputData.getUsername());

                userDataAccessObject.setCurrentUsername(user.getUsername());
                final LoginOutputData loginOutputData = new LoginOutputData(user.getUsername(), false);
                loginPresenter.prepareSuccessView(loginOutputData);
            }
        }
    }

    @Override
    public void switchToStartView() {
        loginPresenter.switchToStartView();
    }
}
