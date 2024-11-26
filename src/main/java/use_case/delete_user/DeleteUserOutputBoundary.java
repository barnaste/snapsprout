package use_case.delete_user;

import use_case.login.LoginOutputData;

/**
 * The output boundary for the Delete User Use Case.
 */
public interface DeleteUserOutputBoundary {
    /**
     * Prepares the success view for the Delete User Use Case.
     */
    void prepareSuccessView();

    /**
     * Prepares the failure view for the Delete User Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

}
