package use_case.upload;

import java.awt.image.BufferedImage;

/**
 * The Input Data for the Upload use case, when saving details about a plant
 * found within the uploaded image to storage.
 */
public class UploadSaveInputData {

    private final BufferedImage image;
    private final String plantName;
    private final String family;
    private final String plantSpecies;
    private final String userNotes;
    private final boolean isPublic;

    public UploadSaveInputData(BufferedImage image, String plantName, String family, String plantSpecies, String userNotes, boolean isPublic) {
        this.image = image;
        this.plantName = plantName;
        this.family = family;
        this.plantSpecies = plantSpecies;
        this.userNotes = userNotes;
        this.isPublic = isPublic;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getPlantSpecies() {
        return plantSpecies;
    }

    public String getUserNotes() {
        return userNotes;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public String getPlantName() {
        return plantName;
    }

    public String getFamily() {
        return family;
    }
}
