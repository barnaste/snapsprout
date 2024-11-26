package view.panel_factory;

import data_access.MongoImageDataAccessObject;
import data_access.MongoPlantDataAccessObject;
import data_access.MongoUserDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.delete_user.DeleteUserController;
import interface_adapter.delete_user.DeleteUserPresenter;
import interface_adapter.delete_user.DeleteUserViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.main.MainViewModel;
import interface_adapter.upload.UploadController;
import use_case.delete_user.DeleteUserInputBoundary;
import use_case.delete_user.DeleteUserInputData;
import use_case.delete_user.DeleteUserInteractor;
import use_case.delete_user.DeleteUserOutputBoundary;
import use_case.upload.UploadInputBoundary;
import use_case.upload.UploadInteractor;
import view.DeleteUserView;

import javax.swing.*;
import java.awt.*;

public class DeleteUserPanelFactory {
    public static void createDeleteUserPanel(Container parentPanel, JPanel deleteUserPanel, Runnable escapeMap, ViewManagerModel viewManagerModel, MainViewModel mainViewModel, LoginViewModel loginViewModel) {
        parentPanel.revalidate();
        parentPanel.repaint();

        DeleteUserViewModel deleteUserViewModel = new DeleteUserViewModel();
        DeleteUserView deleteUserView = new DeleteUserView(deleteUserViewModel);
        deleteUserPanel.add(deleteUserView, deleteUserView.getViewName());

        MongoPlantDataAccessObject mongoPlantDataAccessObject = MongoPlantDataAccessObject.getInstance();
        MongoUserDataAccessObject mongoUserDataAccessObject = MongoUserDataAccessObject.getInstance();
        MongoImageDataAccessObject mongoImageDataAccessObject = MongoImageDataAccessObject.getInstance();

        DeleteUserOutputBoundary deleteUserPresentor = new DeleteUserPresenter(viewManagerModel, mainViewModel, loginViewModel );
        DeleteUserInputBoundary deleteuserInteractor = new DeleteUserInteractor(mongoPlantDataAccessObject, mongoImageDataAccessObject, mongoUserDataAccessObject, deleteUserPresentor);

        deleteuserInteractor.setEscapeMap(escapeMap);
        DeleteUserController controller = new DeleteUserController(deleteuserInteractor);

        deleteUserView.setController(controller);
    }
}
