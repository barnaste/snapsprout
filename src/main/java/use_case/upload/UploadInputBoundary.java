package use_case.upload;

/**
 * Input Boundary for the Upload use case, involving actions which are related to
 * loading information about, and storing, a new image.
 */
public interface UploadInputBoundary {

    /**
     * Change the display to the confirm view, showing the image selected by the user.
     * @param uploadInputData the input image data used in the confirmation display
     */
    void switchToConfirmView(UploadInputData uploadInputData);

    /**
     * Change the display to the select view, where the user may choose an image.
     */
    void switchToSelectView();

    /**
     * Upload the input image data to retrieve information about the plant
     * shown in the image. Then, display this information in the results view.
     * @param uploadInputData the input image data from which to retrieve information
     */
    void uploadImageData(UploadInputData uploadInputData);

    /**
     * Save the information given in the input data as a new Plant within storage.
     * @param uploadInputData the input data about the image and the plant within
     */
    void saveUpload(UploadSaveInputData uploadInputData);

    /**
     * Set the method by which the upload use case is closed -- the UI for this
     * component is owned by another component, and thus must be closed externally.
     * @param escapeMap the method called to close the upload use case
     */
    void setEscapeMap(Runnable escapeMap);

    /**
     * Exits the upload use case.
     */
    void escape();
}
