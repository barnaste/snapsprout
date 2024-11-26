package interface_adapter.upload;

import interface_adapter.ViewManagerModel;
import interface_adapter.main.MainViewModel;
import interface_adapter.upload.confirm.UploadConfirmState;
import interface_adapter.upload.confirm.UploadConfirmViewModel;
import interface_adapter.upload.result.UploadResultState;
import interface_adapter.upload.result.UploadResultViewModel;
import interface_adapter.upload.select.UploadSelectState;
import interface_adapter.upload.select.UploadSelectViewModel;
import use_case.upload.UploadOutputBoundary;
import use_case.upload.UploadConfirmOutputData;
import use_case.upload.UploadResultOutputData;
import use_case.upload.UploadSelectOutputData;

/**
 * The presenter for the Upload use case. This class unpacks interactor information
 * and sends change details to the view.
 */
public class UploadPresenter implements UploadOutputBoundary {

    private final UploadSelectViewModel selectViewModel;
    private final UploadConfirmViewModel confirmViewModel;
    private final UploadResultViewModel resultViewModel;
    private final ViewManagerModel viewManagerModel;
    private final MainViewModel mainViewModel;

    public UploadPresenter(ViewManagerModel viewManagerModel,
                           UploadSelectViewModel selectViewModel,
                           UploadConfirmViewModel confirmViewModel,
                           UploadResultViewModel resultViewModel,
                           MainViewModel mainViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.selectViewModel = selectViewModel;
        this.confirmViewModel = confirmViewModel;
        this.resultViewModel = resultViewModel;
        this.mainViewModel = mainViewModel;
    }

    @Override
    public void switchToConfirmView(UploadConfirmOutputData outputData) {
        final UploadConfirmState state = confirmViewModel.getState();
        state.setImagePath(outputData.getImage());
        confirmViewModel.setState(state);
        confirmViewModel.firePropertyChanged();

        viewManagerModel.setState(confirmViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToSelectView(UploadSelectOutputData outputData) {
        final UploadSelectState state = selectViewModel.getState();
        state.setError(outputData.getError());
        selectViewModel.setState(state);
        selectViewModel.firePropertyChanged();

        viewManagerModel.setState(selectViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToResultView(UploadResultOutputData outputData) {
        final UploadResultState state = resultViewModel.getState();
        state.setImagePath(outputData.getImage());
        state.setName(outputData.getName());
        state.setScientificName(outputData.getScientificName());
        state.setFamily(outputData.getFamily());
        state.setCertainty(outputData.getCertainty());
        resultViewModel.setState(state);
        resultViewModel.firePropertyChanged();

        viewManagerModel.setState(resultViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void notifyUploadComplete() {
        mainViewModel.firePropertyChanged("refresh");
    }
}