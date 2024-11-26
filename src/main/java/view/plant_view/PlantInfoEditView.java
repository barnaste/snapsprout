package view.plant_view;

import entity.Plant;
import interface_adapter.user_plant_info_edit.UserPlantInfoEditController;
import interface_adapter.user_plant_info_edit.UserPlantInfoEditViewModel;
import view.ViewComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the editable plant view use case.
 */
public class PlantInfoEditView extends PlantView {

    private UserPlantInfoEditController controller;

    public PlantInfoEditView(Plant plant, BufferedImage image) {
        this.setImage(image);
        this.getScientificNameLabel().setText(plant.getScientificName());
        this.getNotesField().setText(plant.getComments());
        this.getTogglePublic().setSelected(plant.getIsPublic());
        this.getFamilyLabel().setText(plant.getFamily());

        this.getNameLabel().setText(plant.getSpecies());
        this.getLikesLabel().setText("\uD83D\uDC4D " + plant.getNumOfLikes());
    }

    @Override
    protected JPanel createTopPanel() {
        JPanel topPanel = super.createTopPanel();
        JButton returnBtn = ViewComponentFactory.buildButton(UserPlantInfoEditViewModel.RETURN_BUTTON_LABEL);
        returnBtn.setBorderPainted(false);

        returnBtn.addActionListener((e) -> controller.escape());
        topPanel.add(returnBtn, BorderLayout.WEST);
        return topPanel;
    }

    /**
     * Create the panel within which user action buttons save and delete are displayed.
     * @return a reference to the created panel
     */
    @Override
    protected JPanel createActionPanel() {
        JPanel actionPanel = super.createActionPanel();

        JButton saveBtn = ViewComponentFactory.buildButton(UserPlantInfoEditViewModel.SAVE_BUTTON_LABEL);
        saveBtn.setPreferredSize(new Dimension(100, 30));

        saveBtn.addActionListener((e) -> controller.savePlant(
                getNotesField().getText(),
                getTogglePublic().isSelected()
        ));
        // NOTE: we negate privacyToggle as the controller needs to know if the image is public,
        //  not if the image is private.
        JButton discardBtn = ViewComponentFactory.buildButton(UserPlantInfoEditViewModel.DELETE_BUTTON_LABEL);
        discardBtn.setPreferredSize(new Dimension(100, 30));

        discardBtn.addActionListener((e) -> controller.deletePlant());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(20, 20, 20, 20);
        actionPanel.add(discardBtn, constraints);

        constraints.gridx = 1;
        actionPanel.add(saveBtn, constraints);

        return actionPanel;
    }

    public void setController(UserPlantInfoEditController controller) {
        this.controller = controller;
    }
}
