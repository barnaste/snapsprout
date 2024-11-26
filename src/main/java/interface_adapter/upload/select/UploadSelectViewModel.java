package interface_adapter.upload.select;

import interface_adapter.ViewModel;

/**
 * The View Model for the selection stage of the Upload use case.
 * Contains style details for the view in this stage.
 */
public class UploadSelectViewModel extends ViewModel<UploadSelectState> {

    public static final int TRANSPARENT = 0x00000000;
    public static final int TOP_PANEL_COLOR = 0xfff8f5e4;
    public static final int MAIN_PANEL_COLOR = 0xfffffbef;

    public static final int TOP_PANEL_HEIGHT = 25;
    public static final int MAIN_PANEL_HEIGHT = 500;
    public static final int PANEL_WIDTH = 500;

    public static final String CANCEL_BUTTON_LABEL = "× Cancel";
    public static final String UPLOAD_BUTTON_LABEL = "Select from Computer";

    public UploadSelectViewModel() {
        super("upload select");
        setState(new UploadSelectState());
    }
}
