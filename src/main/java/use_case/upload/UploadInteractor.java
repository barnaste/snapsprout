package use_case.upload;

import java.io.File;
import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import use_case.ImageDataAccessInterface;
import use_case.PlantDataAccessInterface;
import entity.Plant;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.UserDataAccessInterface;

/**
 * The Upload Interactor.
 */
public class UploadInteractor implements UploadInputBoundary {

    private final UploadOutputBoundary presenter;
    private final ImageDataAccessInterface imageDataBase;
    private final PlantDataAccessInterface plantDataBase;
    private final UserDataAccessInterface userDataBase;

    private Runnable escapeMap;

    // plantnet-API-specific information
    private static final String PROJECT = "all";
    private static final String API_URL = "https://my-api.plantnet.org/v2/identify/" + PROJECT + "?api-key=";
    private static final String API_PRIVATE_KEY = "2b1015rSKP2VVP2UzoDaqbYI"; // this is the group key used -- private

    public UploadInteractor(UploadOutputBoundary uploadOutputBoundary, ImageDataAccessInterface imageDataBase,
                            PlantDataAccessInterface plantDataBase, UserDataAccessInterface userDataBase) {
        this.presenter = uploadOutputBoundary;
        this.imageDataBase = imageDataBase;
        this.plantDataBase = plantDataBase;
        this.userDataBase = userDataBase;
    }

    @Override
    public void switchToConfirmView(UploadInputData inputData) {
        UploadConfirmOutputData outputData = new UploadConfirmOutputData(inputData.getImage());
        this.presenter.switchToConfirmView(outputData);
    }

    @Override
    public void switchToSelectView() {
        this.presenter.switchToSelectView(new UploadSelectOutputData());
    }

    @Override
    public void uploadImageData(UploadInputData uploadInputData) {
        File image = new File(uploadInputData.getImage());
        HttpEntity entity = MultipartEntityBuilder.create()
                .addPart("images", new FileBody(image)).addTextBody("organs", "auto")
                .build();
        HttpPost request = new HttpPost(API_URL + API_PRIVATE_KEY);
        request.setEntity(entity);

        // fetch plant information from uploaded image using plantnet api
        try (CloseableHttpClient client = HttpClientBuilder.create().build()){
            CloseableHttpResponse httpResponse = client.execute(request);
            String jsonString = EntityUtils.toString(httpResponse.getEntity());

            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.has("error")) {
                UploadSelectOutputData outputData = new UploadSelectOutputData(jsonObject.getString("message"));
                this.presenter.switchToSelectView(outputData);
            } else {
                // find relevant information in the fetched jsonObject
                String name = jsonObject
                        .getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("species")
                        .getJSONArray("commonNames")
                        .getString(0);
                String scientific = jsonObject
                        .getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("species")
                        .getString("scientificNameWithoutAuthor");
                String family = jsonObject
                        .getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("species")
                        .getJSONObject("family")
                        .getString("scientificNameWithoutAuthor");
                double score = jsonObject
                        .getJSONArray("results")
                        .getJSONObject(0)
                        .getDouble("score");

                UploadResultOutputData outputData = new UploadResultOutputData(
                        uploadInputData.getImage(),
                        name,
                        scientific,
                        family,
                        score
                );

                this.presenter.switchToResultView(outputData);
            }
        } catch (JSONException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveUpload(UploadSaveInputData inputData) {
        String imageID = imageDataBase.addImage(inputData.getImage());
        Plant plant = new Plant(
                imageID,
                inputData.getPlantName(),
                inputData.getFamily(),
                inputData.getPlantSpecies(),
                userDataBase.getCurrentUsername(),
                inputData.getUserNotes(),
                inputData.isPublic()
        );
        plantDataBase.addPlant(plant);

        this.escapeMap.run();
        presenter.notifyUploadComplete();
    }

    @Override
    public void setEscapeMap(Runnable escapeMap) {
        this.escapeMap = escapeMap;
    }

    @Override
    public void escape() {
        this.escapeMap.run();
    }
}

