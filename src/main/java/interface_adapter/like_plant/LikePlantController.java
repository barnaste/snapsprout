package interface_adapter.like_plant;

import entity.Plant;
import org.bson.types.ObjectId;
import use_case.like_plant.LikePlantInputBoundary;
import use_case.like_plant.LikePlantInputData;

public class LikePlantController {
    private final LikePlantInputBoundary likePlantInteractor;

    public LikePlantController(LikePlantInputBoundary likePlantInteractor) {
        this.likePlantInteractor = likePlantInteractor;
    }

    public void execute(Plant plant) {
        LikePlantInputData likePlantInputData = new LikePlantInputData(plant);
        likePlantInteractor.execute(likePlantInputData);
    }
}
