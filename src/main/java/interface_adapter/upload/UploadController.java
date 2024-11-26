package interface_adapter.upload;

import use_case.upload.UploadInputBoundary;
import use_case.upload.UploadInputData;
import use_case.upload.UploadSaveInputData;

import java.awt.image.BufferedImage;

/**
 * The Controller for the Upload use case. This class packages user input and
 * sends it to the appropriate interactor method.
 */
public class UploadController {

    private final UploadInputBoundary uploadUseCaseInteractor;

    public UploadController(UploadInputBoundary uploadInteractor) {
        this.uploadUseCaseInteractor = uploadInteractor;
    }

    /**
     * Change the display to the confirm view, displaying the image at the given location.
     * @param filePath the file path of the image to be displayed
     */
    public void switchToConfirmView(String filePath) {
        final UploadInputData uploadInputData = new UploadInputData(filePath);
        uploadUseCaseInteractor.switchToConfirmView(uploadInputData);
    }

    /**
     * Change the display to the select view, where the user may choose an image.
     */
    public void switchToSelectView() {
        uploadUseCaseInteractor.switchToSelectView();
    }

    /**
     * Change the display to the results view, where the user sees details about the
     * plant found at the specified location.
     * @param filePath the file path of the image to be uploaded
     */
    public void switchToResultView(String filePath) {
        final UploadInputData uploadInputData = new UploadInputData(filePath);
        uploadUseCaseInteractor.uploadImageData(uploadInputData);
    }

    /**
     * Save the information given in the input data as a new Plant within storage.
     * @param image the image of the plant
     * @param plantName the (common) name of the plant
     * @param family the scientific family of the plant
     * @param plantSpecies the scientific name of the plant
     * @param userNotes the notes taken by the user about the plant
     * @param isPublic the privacy settings for the plant
     */
    public void saveUpload(BufferedImage image, String plantName, String family, String plantSpecies,
                           String userNotes, boolean isPublic) {
        final UploadSaveInputData inputData = new UploadSaveInputData(image, plantName, family, plantSpecies,
                userNotes, isPublic);
        uploadUseCaseInteractor.saveUpload(inputData);
    }

    /**
     * Exits the upload use case.
     */
    public void escape() {
        uploadUseCaseInteractor.escape();
    }
}
