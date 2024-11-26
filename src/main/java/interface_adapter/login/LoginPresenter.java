package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.main.MainState;
import interface_adapter.main.MainViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final MainViewModel mainViewModel;
    private final ViewManagerModel viewManagerModel;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          MainViewModel mainViewModel,
                          LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.mainViewModel = mainViewModel;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        // On success, switch to the logged in view.

        final MainState mainState = mainViewModel.getState();
        mainState.setUsername(response.getUsername());
        this.mainViewModel.setState(mainState);
        this.mainViewModel.firePropertyChanged("logged_in");

        this.viewManagerModel.setState(mainViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView() {
        loginViewModel.firePropertyChanged("error");
    }

    @Override
    public void switchToStartView() {
        viewManagerModel.setState("StartView");
        viewManagerModel.firePropertyChanged();
    }
}