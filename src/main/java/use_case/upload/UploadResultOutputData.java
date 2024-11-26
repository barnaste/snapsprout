package use_case.upload;

/**
 * Output Data for the Upload use case when transitioning to the results stage.
 * Contains details about the plant within the image uploaded, as well as the image itself.
 */
public class UploadResultOutputData {

    private final String imagePath;
    private final String name;
    private final String scientificName;
    private final String family;
    private final double certainty;

    public UploadResultOutputData(String imagePath, String name, String scientificName,
                                  String family, double certainty) {
        this.imagePath = imagePath;
        this.name = name;
        this.scientificName = scientificName;
        this.family = family;
        this.certainty = certainty;
    }

    public String getImage() {
        return imagePath;
    }

    public String getName() {
        return name;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String getFamily() {
        return family;
    }

    public double getCertainty() {
        return certainty;
    }
}
