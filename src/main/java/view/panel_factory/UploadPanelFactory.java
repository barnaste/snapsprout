package view.panel_factory;

import data_access.MongoImageDataAccessObject;
import data_access.MongoPlantDataAccessObject;
import data_access.MongoUserDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.main.MainViewModel;
import interface_adapter.upload.UploadController;
import interface_adapter.upload.UploadPresenter;
import interface_adapter.upload.confirm.UploadConfirmViewModel;
import interface_adapter.upload.result.UploadResultViewModel;
import interface_adapter.upload.select.UploadSelectViewModel;
import use_case.upload.UploadInputBoundary;
import use_case.upload.UploadInteractor;
import use_case.upload.UploadOutputBoundary;
import view.ViewManager;
import view.upload.UploadConfirmView;
import view.upload.UploadResultView;
import view.upload.UploadSelectView;

import javax.swing.*;
import java.awt.*;

public class UploadPanelFactory {
    /**
     * Creates and configures the upload panel for the application, which includes multiple views
     * (selection, confirmation, result) managed by a `CardLayout`. It sets up the layout,
     * initializes the views and controllers, and binds the interactor to manage upload functionality.
     *
     * @param parentPanel The parent container that holds the upload panel.
     * @param cardPanel The `JPanel` that contains the views managed by `CardLayout`.
     * @param escapeMap A `Runnable` to handle escape actions, such as canceling the upload process.
     * @param mainViewModel The `MainViewModel` used to manage the application's state during the upload process.
     */
    public static void createUploadPanel(Container parentPanel, JPanel cardPanel, Runnable escapeMap, MainViewModel mainViewModel) {
        // NOTE: We extend CardLayout so the entire view is redrawn when the card is swapped.
        // This avoids artefacts from previous cards, especially if they have variant dimensions.
        CardLayout cardLayout = new CardLayout() {
            @Override
            public void show(Container parent, String name) {
                super.show(parent, name);
                parentPanel.revalidate();  // Revalidate the parent container
                parentPanel.repaint();     // Repaint to avoid artefacts
            }
        };
        cardPanel.setLayout(cardLayout);

        // Initialize and add the UploadSelectView to the card panel
        UploadSelectViewModel selectorViewModel = new UploadSelectViewModel();
        UploadSelectView selectorView = new UploadSelectView(selectorViewModel);
        cardPanel.add(selectorView, selectorView.getViewName());

        // Initialize and add the UploadConfirmView to the card panel
        UploadConfirmViewModel confirmViewModel = new UploadConfirmViewModel();
        UploadConfirmView confirmView = new UploadConfirmView(confirmViewModel);
        cardPanel.add(confirmView, confirmView.getViewName());

        // Initialize and add the UploadResultView to the card panel
        UploadResultViewModel resultViewModel = new UploadResultViewModel();
        UploadResultView resultView = new UploadResultView(resultViewModel);
        cardPanel.add(resultView, resultView.getViewName());

        // Initialize the ViewManager for handling view transitions
        ViewManagerModel uploadManagerModel = new ViewManagerModel();
        new ViewManager(cardPanel, cardLayout, uploadManagerModel);

        // Set up the UploadOutputBoundary and Interactor
        UploadOutputBoundary uploadOutputBoundary = new UploadPresenter(
                uploadManagerModel, selectorViewModel, confirmViewModel, resultViewModel, mainViewModel
        );
        UploadInputBoundary uploadInteractor = new UploadInteractor(
                uploadOutputBoundary,
                MongoImageDataAccessObject.getInstance(),
                MongoPlantDataAccessObject.getInstance(),
                MongoUserDataAccessObject.getInstance()
        );
        uploadInteractor.setEscapeMap(escapeMap);

        // Initialize the UploadController and link it to all views
        UploadController controller = new UploadController(uploadInteractor);
        selectorView.setController(controller);
        confirmView.setController(controller);
        resultView.setController(controller);

        // Set initial state and notify the view manager
        uploadManagerModel.setState(selectorView.getViewName());
        uploadManagerModel.firePropertyChanged();
    }
}
