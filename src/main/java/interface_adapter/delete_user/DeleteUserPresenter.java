package interface_adapter.delete_user;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.main.MainState;
import interface_adapter.main.MainViewModel;
import use_case.delete_user.DeleteUserOutputBoundary;

import javax.swing.*;

/**
 * The Presenter for the DeleteUser Use Case.
 */
public class DeleteUserPresenter implements DeleteUserOutputBoundary {

    private final MainViewModel mainViewModel;
    private final ViewManagerModel viewManagerModel;
    private final LoginViewModel loginViewModel;

    public DeleteUserPresenter(ViewManagerModel viewManagerModel,
                               MainViewModel mainViewModel,
                               LoginViewModel loginViewModel) {
        this.mainViewModel = mainViewModel;
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView() {
        // On success, switch to the start view and logs user out
        // Update the logged in state
        final MainState mainState = mainViewModel.getState();
        mainState.setUsername("");
        mainViewModel.setState(mainState);
        mainViewModel.firePropertyChanged("state");

        // Update the login state
        final LoginState loginState = loginViewModel.getState();
        loginState.setUsername("");
        loginState.setPassword("");
        loginViewModel.setState(loginState);
        loginViewModel.firePropertyChanged("state");

        // Switch to the start view
        this.viewManagerModel.setState("StartView");
        this.viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        JOptionPane.showMessageDialog(null, error);
    }
}