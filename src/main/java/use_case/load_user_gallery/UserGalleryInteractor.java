package use_case.load_user_gallery;

import data_access.*;
import entity.Plant;
import org.bson.types.ObjectId;
import use_case.ImageDataAccessInterface;
import use_case.PlantDataAccessInterface;
import use_case.UserDataAccessInterface;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class UserGalleryInteractor implements UserGalleryInputBoundary {
    private final PlantDataAccessInterface plantDataAccessObject;
    private final UserGalleryOutputBoundary galleryPresenter;
    private final ImageDataAccessInterface imageDataAccessObject;
    private final UserDataAccessInterface userDataAccessObject;
    private static final int IMAGES_PER_PAGE = 15;

    private int currentPage;

    public UserGalleryInteractor(PlantDataAccessInterface galleryDataAccessObject,
                                 UserGalleryOutputBoundary galleryPresenter,
                                 ImageDataAccessInterface imageDataAccessObject,
                                 UserDataAccessInterface userDataAccessObject) {
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

        // Retrieve the correct slice of Plant objects from database
        List<Plant> plants = plantDataAccessObject.getUserPlants(userDataAccessObject.getCurrentUsername(), skip, IMAGES_PER_PAGE);

        // Get images from Plant objects
        List<BufferedImage> images = new ArrayList<>();
        List<ObjectId> ids = new ArrayList<>();
        for (Plant plant : plants) {
            images.add(imageDataAccessObject.getImageFromID(plant.getImageID()));
            ids.add(plant.getFileID());
        }

        // Prepare output data and send to presenter
        int totalPages = getNumberOfUserPlants();
        UserGalleryOutputData outputData = new UserGalleryOutputData(images, ids, currentPage, totalPages);
        galleryPresenter.prepareSuccessView(outputData);
    }
}
