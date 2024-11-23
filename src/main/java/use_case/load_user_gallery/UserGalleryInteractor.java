package use_case.load_user_gallery;

import data_access.*;
import entity.Plant;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class UserGalleryInteractor implements UserGalleryInputBoundary {
    private final PlantDataAccessObject plantDataAccessObject;
    private final UserGalleryOutputBoundary galleryPresenter;
    private final ImageDataAccessObject imageDataAccessObject;
    private final UserDataAccessObject userDataAccessObject;
    private static final int IMAGES_PER_PAGE = 15;

    private int currentPage;

    public UserGalleryInteractor(PlantDataAccessObject galleryDataAccessObject,
                                 UserGalleryOutputBoundary galleryPresenter,
                                 ImageDataAccessObject imageDataAccessObject,
                                 UserDataAccessObject userDataAccessObject) {
        this.plantDataAccessObject = galleryDataAccessObject;
        this.galleryPresenter = galleryPresenter;
        this.imageDataAccessObject = imageDataAccessObject;
        this.userDataAccessObject = userDataAccessObject;
        this.currentPage = 0;
    }

    public void nextPage() {
        int totalPages = getNumberOfUserPlants();
        if (currentPage < totalPages - 1) {
            currentPage++;
            execute(new UserGalleryInputData(currentPage));
        }
    }

    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            execute(new UserGalleryInputData(currentPage));
        }
    }

    public int getNumberOfUserPlants(){
        return (int) Math.ceil((double) plantDataAccessObject.getNumberOfUserPlants(userDataAccessObject.getCurrentUsername()) / IMAGES_PER_PAGE);
    }

    @Override
    public void execute(UserGalleryInputData galleryInputData) {
        int page = galleryInputData.getPage();
        currentPage = page;
        int skip = page * IMAGES_PER_PAGE;  // Calculate the offset based on the page

        try {
            // Retrieve the correct slice of Plant objects from database
            List<Plant> plants = plantDataAccessObject.getUserPlants(userDataAccessObject.getCurrentUsername(), skip, IMAGES_PER_PAGE);

            if (plants == null || plants.isEmpty()) {
                galleryPresenter.prepareFailView();
                return;
            }

            // Get images from Plant objects
            List<BufferedImage> images = new ArrayList<>();
            for (Plant plant : plants) {
                BufferedImage image = imageDataAccessObject.getImageFromID(plant.getImageID());
                if (image != null) {
                    images.add(image);
                }
            }

            // Prepare output data and send to presenter
            int totalPages = getNumberOfUserPlants();
            UserGalleryOutputData outputData = new UserGalleryOutputData(images, currentPage, totalPages);
            galleryPresenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            galleryPresenter.prepareFailView();
        }
    }
}
