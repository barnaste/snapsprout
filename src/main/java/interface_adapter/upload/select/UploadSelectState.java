package interface_adapter.upload.select;

/**
 * State information for the Upload use case in regard to the selection stage.
 * Contains information about any error the user is notified of in this stage.
 */
public class UploadSelectState {

    private String error;

    UploadSelectState() {
        error = null;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
