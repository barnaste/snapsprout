package use_case.delete_user;

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
     */
    void prepareFailView();
}
