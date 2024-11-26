package use_case.user_plant_info_edit;

/**
 * Output Boundary for actions relating to editing, saving, discarding, and deleting
 * an already-existing plant object. That is, the Plant Edit Output Boundary.
 */
public interface UserPlantInfoEditOutputBoundary {

    /**
     * Notify the display that the Upload use case terminated and processes have finished.
     */
    void prepareSuccessView();
}
