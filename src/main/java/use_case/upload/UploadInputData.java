package use_case.upload;

/**
 * The Input Data for the Upload use case. Contains information about the image to upload.
 */
public class UploadInputData {

    private final String imagePath;

    public UploadInputData(String image) {
        this.imagePath = image;
    }

    public String getImage() {
        return imagePath;
    }
}
