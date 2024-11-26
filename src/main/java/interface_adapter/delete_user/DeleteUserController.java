package interface_adapter.delete_user;

import use_case.delete_user.DeleteUserInputBoundary;
import use_case.delete_user.DeleteUserInputData;

public class DeleteUserController {
    private DeleteUserInputBoundary deleteUserUseCaseInteractor;

    public DeleteUserController(DeleteUserInputBoundary deleteUseCaseInteractor) {
        this.deleteUserUseCaseInteractor = deleteUseCaseInteractor;
    }

    /**
     * Executes the Logout Use Case.
     *
     * @param username the username of the user who is logged in
     */
    public void execute(String username, String password) {
        final DeleteUserInputData deleteUserInputData = new DeleteUserInputData(username, password);
        deleteUserUseCaseInteractor.execute(deleteUserInputData);
    }

    public void escape() {
        deleteUserUseCaseInteractor.escape();
    }
}
