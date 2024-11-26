package use_case.load_public_gallery;

import data_access.InMemoryImageDataAccessObject;
import data_access.InMemoryPlantDataAccessObject;
import entity.Plant;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PublicGalleryInteractorTest {
    private InMemoryPlantDataAccessObject plantDataAccess;
    private InMemoryImageDataAccessObject imageDataAccess;
    private static final int IMAGES_PER_PAGE = 15;

    @Before
    public void setUp() {
        plantDataAccess = InMemoryPlantDataAccessObject.getInstance();
        plantDataAccess.deleteAll();
        imageDataAccess = InMemoryImageDataAccessObject.getInstance();
        imageDataAccess.deleteAll();
    }

    @After
    public void tearDown() {
        // Clear state after each test
        plantDataAccess.deleteAll();
        imageDataAccess.deleteAll();
    }

    @Test
    public void successTest() {
        // Add 20 public plants with images
        for (int i = 0; i < 20; i++) {
            String imageID = "image" + i;
            String species = "Species " + i;
            String family = "Family " + i;
            String scientificName = "ScientificName " + i;
            String owner = "Owner " + i;
            String comments = "Comments " + i;
            boolean isPublic = true;

            Plant plant = new Plant(imageID, species, family, scientificName, owner, comments, isPublic);
            plantDataAccess.addPlant(plant);

            BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            imageDataAccess.addImage(image);
        }

        // Create a successPresenter that tests the output
        PublicGalleryOutputBoundary successPresenter = new PublicGalleryOutputBoundary() {
            @Override
            public void prepareSuccessView(PublicGalleryOutputData outputData) {
                // Assertions
                assertEquals(IMAGES_PER_PAGE, outputData.getImages().size()); // First page should have 15 images
                assertEquals(0, outputData.getPage()); // Current page should be 0
                assertEquals((int) Math.ceil(20.0/IMAGES_PER_PAGE), outputData.getTotalPages()); // 20 plants, so 2 pages of 15 plants each

                // Verify that the images and IDs belong to the first 15 plants
                List<ObjectId> expectedIds = new ArrayList<>();
                for (Plant plant : plantDataAccess.getPublicPlants(0, IMAGES_PER_PAGE)) {
                    ObjectId fileID = plant.getFileID();
                    expectedIds.add(fileID);
                }
                assertEquals(expectedIds, outputData.getIds());
            }
        };

        // Initialize the interactor with the successPresenter
        PublicGalleryInputBoundary interactor =
                new PublicGalleryInteractor(plantDataAccess, successPresenter, imageDataAccess);

        // Request the first page
        interactor.execute(new PublicGalleryInputData(0));
    }

    @Test
    public void nextPageTest() {
        // Add 20 public plants with images
        for (int i = 0; i < 20; i++) {
            String imageID = "image" + i;
            String species = "Species " + i;
            String family = "Family " + i;
            String scientificName = "ScientificName " + i;
            String owner = "Owner " + i;
            String comments = "Comments " + i;
            boolean isPublic = true;

            Plant plant = new Plant(imageID, species, family, scientificName, owner, comments, isPublic);
            plantDataAccess.addPlant(plant);

            BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            imageDataAccess.addImage(image);
        }

        // Create a successPresenter to check the next page
        PublicGalleryOutputBoundary successPresenter = new PublicGalleryOutputBoundary() {
            @Override
            public void prepareSuccessView(PublicGalleryOutputData outputData) {
                // Assert that the second page is retrieved correctly
                assertEquals(5, outputData.getImages().size()); // Second page should have 5 images (20 total, 15 per page)
                assertEquals(1, outputData.getPage()); // Current page should be 1
                assertEquals(2, outputData.getTotalPages()); // 20 plants, 2 pages

                // Verify that the images and IDs belong to the second set of plants
                List<ObjectId> expectedIds = new ArrayList<>();
                for (Plant plant : plantDataAccess.getPublicPlants(15, 5)) {
                    ObjectId fileID = plant.getFileID();
                    expectedIds.add(fileID);
                }
                assertEquals(expectedIds, outputData.getIds());
            }
        };

        // Initialize the interactor
        PublicGalleryInteractor interactor =
                new PublicGalleryInteractor(plantDataAccess, successPresenter, imageDataAccess);

        // Move to the second page
        interactor.nextPage();
    }

    @Test
    public void previousPageTest() {
        // Add 20 public plants with images
        for (int i = 0; i < 20; i++) {
            String imageID = "image" + i;
            String species = "Species " + i;
            String family = "Family " + i;
            String scientificName = "ScientificName " + i;
            String owner = "Owner " + i;
            String comments = "Comments " + i;
            boolean isPublic = true;

            Plant plant = new Plant(imageID, species, family, scientificName, owner, comments, isPublic);
            plantDataAccess.addPlant(plant);

            BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            imageDataAccess.addImage(image);
        }

        // Create a successPresenter to check the previous page
        PublicGalleryOutputBoundary successPresenter = new PublicGalleryOutputBoundary() {
            private int callCount = 0;

            @Override
            public void prepareSuccessView(PublicGalleryOutputData outputData) {
                if (callCount == 0) {
                    // Assert we're on the second page
                    assertEquals(5, outputData.getImages().size()); // Second page should have 5 images
                    assertEquals(1, outputData.getPage()); // Current page should be 1
                } else if (callCount == 1) {
                    // Assert we're back on the first page
                    assertEquals(15, outputData.getImages().size()); // First page should have 15 images
                    assertEquals(0, outputData.getPage()); // Current page should be 0
                } else {
                    fail("prepareSuccessView was called more times than expected");
                }
                callCount++;
            }
        };

        // Initialize the interactor
        PublicGalleryInteractor interactor =
                new PublicGalleryInteractor(plantDataAccess, successPresenter, imageDataAccess);

        // Simulate starting on the second page by manually invoking the nextPage() method
        interactor.nextPage(); // This moves to the second page

        // Move back to the first page
        interactor.previousPage();
    }
}
