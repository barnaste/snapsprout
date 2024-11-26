package interface_adapter.upload.confirm;

/**
 * State information for the Upload use case in regard to the confirmation stage.
 * Contains information about the image shown in this stage.
 */
public class UploadConfirmState {

    private String imagePath = "";

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
