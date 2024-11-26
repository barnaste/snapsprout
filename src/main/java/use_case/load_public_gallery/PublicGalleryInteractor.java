package use_case.load_public_gallery;

import use_case.ImageDataAccessInterface;
import use_case.PlantDataAccessInterface;
import entity.Plant;
import org.bson.types.ObjectId;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PublicGalleryInteractor implements PublicGalleryInputBoundary {
    private final PlantDataAccessInterface plantDataAccessInterface;
    private final PublicGalleryOutputBoundary galleryPresenter;
    private final ImageDataAccessInterface imageDataAccessObject;
    private static final int IMAGES_PER_PAGE = 15;

    private int currentPage;

    public PublicGalleryInteractor(PlantDataAccessInterface galleryDataAccessObject,
                                   PublicGalleryOutputBoundary galleryPresenter, ImageDataAccessInterface imageDataAccessObject) {
        this.plantDataAccessInterface = galleryDataAccessObject;
        this.galleryPresenter = galleryPresenter;
        this.imageDataAccessObject = imageDataAccessObject;
        this.currentPage = 0;
    }

    public void nextPage() {
        int totalPages = getNumberOfPublicPlants();
        if (currentPage < totalPages - 1) {
            currentPage++;
            execute(new PublicGalleryInputData(currentPage));
        }
    }

    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            execute(new PublicGalleryInputData(currentPage));
        }
    }

    public int getNumberOfPublicPlants(){
        return (int) Math.ceil((double) plantDataAccessInterface.getNumberOfPublicPlants() / IMAGES_PER_PAGE);
    }

    @Override
    public void execute(PublicGalleryInputData galleryInputData) {
        int page = galleryInputData.getPage();
        currentPage = page;
        int skip = page * IMAGES_PER_PAGE;  // Calculate the offset based on the page

        // Retrieve the correct slice of Plant objects from database
        List<Plant> plants = plantDataAccessInterface.getPublicPlants(skip, IMAGES_PER_PAGE);

        // Get images from Plant objects
        List<BufferedImage> images = new ArrayList<>();
        List<ObjectId> ids = new ArrayList<>();
        for (Plant plant : plants) {
            ids.add(plant.getFileID());
            images.add(imageDataAccessObject.getImageFromID(plant.getImageID()));
        }

        // Prepare output data and send to presenter
        int totalPages = getNumberOfPublicPlants();
        PublicGalleryOutputData outputData = new PublicGalleryOutputData(images, ids, currentPage, totalPages);
        galleryPresenter.prepareSuccessView(outputData);
    }
}
