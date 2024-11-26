package interface_adapter.login;

import interface_adapter.ViewModel;

/**
 * The View Model for the Login View.
 */
public class LoginViewModel extends ViewModel<LoginState> {

    public static final String LOGIN_ERROR_MESSAGE = "Username and password do not match. Try again!";

    public LoginViewModel() {
        super("log in");
        setState(new LoginState());
    }

}
