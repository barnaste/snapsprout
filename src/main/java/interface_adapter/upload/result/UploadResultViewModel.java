package interface_adapter.upload.result;

import interface_adapter.ViewModel;

/**
 * The View Model for the results stage of the Upload use case.
 * Contains style details for the view in this stage.
 */
public class UploadResultViewModel extends ViewModel<UploadResultState> {

    public static final String RETURN_BUTTON_LABEL = "← Return";
    public static final String SAVE_BUTTON_LABEL = "✓ Save";
    public static final String DISCARD_BUTTON_LABEL = "× Discard";

    public UploadResultViewModel() {
        super("upload result");
        setState(new UploadResultState());
    }
}
