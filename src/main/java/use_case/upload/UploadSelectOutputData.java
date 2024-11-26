package use_case.upload;

/**
 * Output Data for the Upload use case when transitioning to the selection stage.
 * Contains information about failure that may have led to this transition.
 */
public class UploadSelectOutputData {

    private final String error;

    public UploadSelectOutputData() {
        this.error = null;
    }

    public UploadSelectOutputData(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
