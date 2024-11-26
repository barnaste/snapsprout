package use_case.load_user_gallery;

import data_access.*;
import entity.Plant;
import entity.User;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserGalleryInteractorTest {
    private InMemoryPlantDataAccessObject plantDataAccess;
    private InMemoryImageDataAccessObject imageDataAccess;
    private InMemoryUserDataAccessObject userDataAccess;
    private static final int IMAGES_PER_PAGE = 15;

    @Before
    public void setUp() {
        plantDataAccess = InMemoryPlantDataAccessObject.getInstance();
        plantDataAccess.deleteAll();
        imageDataAccess = InMemoryImageDataAccessObject.getInstance();
        imageDataAccess.deleteAll();
        userDataAccess = InMemoryUserDataAccessObject.getInstance();
        userDataAccess.deleteAll();

        // Add a test user
        User user = new User("testUser", "testPassword"); // Assuming User constructor takes a username and password
        userDataAccess.addUser(user);
        userDataAccess.setCurrentUsername("testUser");
    }

    @After
    public void tearDown() {
        // Clear state after each test
        plantDataAccess.deleteAll();
        imageDataAccess.deleteAll();
        userDataAccess.deleteAll();
    }

    @Test
    public void successTest() {
        // Add 20 plants with images for the test user
        for (int i = 0; i < 20; i++) {
            String imageID = "image" + i;
            String species = "Species " + i;
            String family = "Family " + i;
            String scientificName = "ScientificName " + i;
            String owner = "testUser";
            String comments = "Comments " + i;
            boolean isPublic = false;

            Plant plant = new Plant(imageID, species, family, scientificName, owner, comments, isPublic);
            plantDataAccess.addPlant(plant);

            BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            imageDataAccess.addImage(image);
        }

        // Create a successPresenter to test the output
        UserGalleryOutputBoundary successPresenter = new UserGalleryOutputBoundary() {
            @Override
            public void prepareSuccessView(UserGalleryOutputData outputData) {
                // Assertions
                assertEquals(IMAGES_PER_PAGE, outputData.getImages().size()); // First page should have 15 images
                assertEquals(0, outputData.getPage()); // Current page should be 0
                assertEquals((int) Math.ceil(20.0 / IMAGES_PER_PAGE), outputData.getTotalPages()); // 20 plants, so 2 pages of 15 plants each

                // Verify that the images and IDs belong to the first 15 plants
                List<ObjectId> expectedIds = new ArrayList<>();
                for (Plant plant : plantDataAccess.getUserPlants("testUser", 0, IMAGES_PER_PAGE)) {
                    ObjectId fileID = plant.getFileID();
                    expectedIds.add(fileID);
                }
                assertEquals(expectedIds, outputData.getIds());
            }
        };

        // Initialize the interactor with the successPresenter
        UserGalleryInputBoundary interactor = new UserGalleryInteractor(plantDataAccess, successPresenter, imageDataAccess, userDataAccess);

        // Request the first page
        interactor.execute(new UserGalleryInputData(0));
    }

    @Test
    public void nextPageTest() {
        // Add 20 plants with images for the test user
        for (int i = 0; i < 20; i++) {
            String imageID = "image" + i;
            String species = "Species " + i;
            String family = "Family " + i;
            String scientificName = "ScientificName " + i;
            String owner = "testUser";
            String comments = "Comments " + i;
            boolean isPublic = false;

            Plant plant = new Plant(imageID, species, family, scientificName, owner, comments, isPublic);
            plantDataAccess.addPlant(plant);

            BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            imageDataAccess.addImage(image);
        }

        // Create a successPresenter to check the next page
        UserGalleryOutputBoundary successPresenter = new UserGalleryOutputBoundary() {
            @Override
            public void prepareSuccessView(UserGalleryOutputData outputData) {
                // Assert that the second page is retrieved correctly
                assertEquals(5, outputData.getImages().size()); // Second page should have 5 images (20 total, 15 per page)
                assertEquals(1, outputData.getPage()); // Current page should be 1
                assertEquals(2, outputData.getTotalPages()); // 20 plants, 2 pages

                // Verify that the images and IDs belong to the second set of plants
                List<ObjectId> expectedIds = new ArrayList<>();
                for (Plant plant : plantDataAccess.getUserPlants("testUser", 15, 5)) {
                    ObjectId fileID = plant.getFileID();
                    expectedIds.add(fileID);
                }
                assertEquals(expectedIds, outputData.getIds());
            }
        };

        // Initialize the interactor
        UserGalleryInteractor interactor = new UserGalleryInteractor(plantDataAccess, successPresenter, imageDataAccess, userDataAccess);

        // Move to the second page
        interactor.nextPage();
    }

    @Test
    public void previousPageTest() {
        // Add 20 plants with images for the test user
        for (int i = 0; i < 20; i++) {
            String imageID = "image" + i;
            String species = "Species " + i;
            String family = "Family " + i;
            String scientificName = "ScientificName " + i;
            String owner = "testUser";
            String comments = "Comments " + i;
            boolean isPublic = false;

            Plant plant = new Plant(imageID, species, family, scientificName, owner, comments, isPublic);
            plantDataAccess.addPlant(plant);

            BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            imageDataAccess.addImage(image);
        }

        // Create a successPresenter to check the previous page
        UserGalleryOutputBoundary successPresenter = new UserGalleryOutputBoundary() {
            private int callCount = 0;

            @Override
            public void prepareSuccessView(UserGalleryOutputData outputData) {
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
        UserGalleryInteractor interactor = new UserGalleryInteractor(plantDataAccess, successPresenter, imageDataAccess, userDataAccess);

        // Simulate starting on the second page by manually invoking the nextPage() method
        interactor.nextPage(); // This moves to the second page

        // Move back to the first page
        interactor.previousPage();
    }
}
