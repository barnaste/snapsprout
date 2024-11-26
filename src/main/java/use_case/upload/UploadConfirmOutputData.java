package use_case.upload;

/**
 * Output Data for the Upload use case when transitioning to the confirmation stage.
 * Contains information about the image to confirm the upload of.
 */
public class UploadConfirmOutputData {

    private final String imagePath;

    public UploadConfirmOutputData(String image) {
        this.imagePath = image;
    }

    public String getImage() {
        return imagePath;
    }
}
