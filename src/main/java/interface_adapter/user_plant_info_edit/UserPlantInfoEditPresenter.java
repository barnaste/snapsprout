package interface_adapter.user_plant_info_edit;

import interface_adapter.main.MainViewModel;
import use_case.user_plant_info_edit.UserPlantInfoEditOutputBoundary;

/**
 * The presenter for the Plant Edit use case.
 * This class unpacks interactor information and sends change details to the view.
 */
public class UserPlantInfoEditPresenter implements UserPlantInfoEditOutputBoundary {

    private final MainViewModel mainViewModel;

    public UserPlantInfoEditPresenter(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }

    @Override
    public void prepareSuccessView() {
        mainViewModel.firePropertyChanged("refresh");
    }
}
