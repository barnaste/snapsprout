package use_case.mode_switch;

import interface_adapter.mode_switch.ModeSwitchState;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ModeSwitchInteractorTest {

    private ModeSwitchState modeSwitchState;
    private ModeSwitchInteractor interactor;

    @Before
    public void setUp() {
        // Initialize the ModeSwitchState
        modeSwitchState = new ModeSwitchState();

        // Initialize the interactor with a simple output presenter that updates the state
        interactor = new ModeSwitchInteractor(() -> {
            ModeSwitchState.Mode currentMode = modeSwitchState.getCurrentMode();
            if (currentMode == ModeSwitchState.Mode.MY_PLANTS) {
                modeSwitchState.setCurrentMode(ModeSwitchState.Mode.DISCOVER);
            } else {
                modeSwitchState.setCurrentMode(ModeSwitchState.Mode.MY_PLANTS);
            }
        });
    }

    @Test
    public void testSwitchModeFromMyPlantsToDiscover() {
        // Ensure the initial mode is MY_PLANTS
        assertEquals(ModeSwitchState.Mode.MY_PLANTS, modeSwitchState.getCurrentMode());

        interactor.switchMode();

        // Verify the mode changed to DISCOVER
        assertEquals(ModeSwitchState.Mode.DISCOVER, modeSwitchState.getCurrentMode());
    }

    @Test
    public void testSwitchModeFromDiscoverToMyPlants() {
        // Set the initial mode to DISCOVER and verify
        modeSwitchState.setCurrentMode(ModeSwitchState.Mode.DISCOVER);
        assertEquals(ModeSwitchState.Mode.DISCOVER, modeSwitchState.getCurrentMode());

        interactor.switchMode();

        // Verify the mode changed to MY_PLANTS
        assertEquals(ModeSwitchState.Mode.MY_PLANTS, modeSwitchState.getCurrentMode());
    }
}
