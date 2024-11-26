package use_case.delete_user;

/**
 * Input Boundary for actions which are related to logging in.
 */
public interface DeleteUserInputBoundary {

    /**
     * Executes the Delete User use case.
     * @param deleteUserInputData the input data
     */
    void execute(DeleteUserInputData deleteUserInputData);

    /**
     * Set the method by which the upload use case is closed -- the UI for this
     * component is owned by another component, and thus must be closed externally
     * @param escapeMap the method called to close the upload use case
     */
    void setEscapeMap(Runnable escapeMap);

    /**
     * Exits the upload use case.
     */
    void escape();
}
