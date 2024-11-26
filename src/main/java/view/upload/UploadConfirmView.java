package view.upload;

import interface_adapter.upload.UploadController;
import interface_adapter.upload.confirm.UploadConfirmState;
import interface_adapter.upload.confirm.UploadConfirmViewModel;
import view.ViewComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the confirmation stage of the Upload use case.
 */
public class UploadConfirmView extends JPanel implements PropertyChangeListener {

    private UploadController controller;

    private String imagePath = "";
    private BufferedImage image;

    public UploadConfirmView(UploadConfirmViewModel viewModel) {
        viewModel.addPropertyChangeListener(this);

        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(UploadConfirmViewModel.TRANSPARENT, true));

        // position each component nicely within the view area using GridBagLayout
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(0, 10, 0, 10);
        this.add(createTopPanel(), constraints);

        constraints.gridy = 1;
        constraints.gridwidth = 2;
        this.add(createImagePanel(), constraints);
    }

    /**
     * Create the panel seen at the top of the result view, containing the return and confirm buttons.
     * @return a reference to the created panel
     */
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(new Color(UploadConfirmViewModel.TOP_PANEL_COLOR, true));
        topPanel.setPreferredSize(new Dimension(
                UploadConfirmViewModel.PANEL_WIDTH,
                UploadConfirmViewModel.TOP_PANEL_HEIGHT
        ));

        JButton returnBtn = ViewComponentFactory.buildButton(UploadConfirmViewModel.RETURN_BUTTON_LABEL);
        returnBtn.setBorderPainted(false);

        returnBtn.addActionListener((e) -> controller.switchToSelectView() );
        topPanel.add(returnBtn, BorderLayout.WEST);

        JButton confirmBtn = ViewComponentFactory.buildButton(UploadConfirmViewModel.CONFIRM_BUTTON_LABEL);
        confirmBtn.setBorderPainted(false);

        confirmBtn.addActionListener((e) -> controller.switchToResultView(this.imagePath) );
        topPanel.add(confirmBtn, BorderLayout.EAST);
        return topPanel;
    }

    /**
     * Create the image panel for this view. This is the panel encompassing all but the top region,
     * displaying the image stored within this class.
     * @return a reference to the created panel
     */
    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (image != null) {
                    g.drawImage(image, 0, 0,
                            UploadConfirmViewModel.IMAGE_WIDTH,
                            UploadConfirmViewModel.IMAGE_HEIGHT,
                            this
                    );
                }
            }
        };
        imagePanel.setPreferredSize(new Dimension(
                UploadConfirmViewModel.IMAGE_WIDTH,
                UploadConfirmViewModel.IMAGE_HEIGHT
        ));
        return imagePanel;
    }

    /**
     * Set variable fields within this view, based on the state information given as parameter.
     * @param state the state the view should represent
     */
    private void setFields(UploadConfirmState state) {
        this.imagePath = state.getImagePath();
        this.image = ViewComponentFactory.buildCroppedImage(this.imagePath);

        this.revalidate();
        this.repaint();
    }

    /**
     * A getter for the view name.
     * @return the view name to be used by view models
     */
    public String getViewName() {
        return "upload confirm";
    }

    public void setController(UploadController controller) {
        this.controller = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final UploadConfirmState state = (UploadConfirmState) evt.getNewValue();
        this.setFields(state);
    }
}
