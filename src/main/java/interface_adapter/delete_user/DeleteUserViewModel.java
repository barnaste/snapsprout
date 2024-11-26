package interface_adapter.delete_user;

import interface_adapter.ViewModel;

public class DeleteUserViewModel extends ViewModel<Object> {

    public static final String LOGIN_ERROR_MESSAGE = "Invalid combination. Try again!";

    public DeleteUserViewModel() {
        super("delete_user");
    }
}