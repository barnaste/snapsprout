package interface_adapter.upload.result;

/**
 * State information for the Upload use case in regard to the results stage.
 * Contains information about the plant displayed in this stage.
 */
public class UploadResultState {

    private String imagePath = "";
    private String name = "";
    private String scientificName = "";
    private String family = "";
    private double certainty = 0;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public double getCertainty() {
        return certainty;
    }

    public void setCertainty(double certainty) {
        this.certainty = certainty;
    }
}
