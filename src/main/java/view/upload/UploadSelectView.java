package view.upload;

import interface_adapter.upload.UploadController;
import interface_adapter.upload.select.UploadSelectState;
import interface_adapter.upload.select.UploadSelectViewModel;
import view.ViewComponentFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the selection stage of the Upload use case.
 */
public class UploadSelectView extends JPanel implements PropertyChangeListener {

    private UploadController controller;

    public UploadSelectView(UploadSelectViewModel uploadSelectViewModel) {
        uploadSelectViewModel.addPropertyChangeListener(this);

        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(UploadSelectViewModel.TRANSPARENT, true));

        // position each component nicely within the view area using a GridBagLayout
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(0, 10, 0, 10);
        this.add(createTopPanel(), constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        this.add(createMainPanel(), constraints);
    }

    /**
     * Create the panel seen at the top of the result view, containing the cancel button.
     * @return a reference to the created panel
     */
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(new Color(UploadSelectViewModel.TOP_PANEL_COLOR, true));
        topPanel.setPreferredSize(new Dimension(
                UploadSelectViewModel.PANEL_WIDTH,
                UploadSelectViewModel.TOP_PANEL_HEIGHT
        ));

        JButton cancelBtn = ViewComponentFactory.buildButton(UploadSelectViewModel.CANCEL_BUTTON_LABEL);
        cancelBtn.setBorderPainted(false);

        cancelBtn.addActionListener((e) -> controller.escape());
        topPanel.add(cancelBtn, BorderLayout.WEST);
        return topPanel;
    }

    /**
     * Create the main panel for this view. That is, the panel containing all but the top region.
     * @return a reference to the created panel
     */
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(UploadSelectViewModel.MAIN_PANEL_COLOR, true));
        mainPanel.setPreferredSize(new Dimension(
                UploadSelectViewModel.PANEL_WIDTH,
                UploadSelectViewModel.MAIN_PANEL_HEIGHT
        ));

        JButton selectFileBtn = ViewComponentFactory.buildButton(UploadSelectViewModel.UPLOAD_BUTTON_LABEL);

        selectFileBtn.addActionListener((e) -> openFileDialog());
        mainPanel.add(selectFileBtn, new GridBagConstraints());
        return mainPanel;
    }

    /**
     * Open a file dialog within which the user may select exclusively image files. If an image
     * is successfully selected, it is then sent to the interactor for processing toward the
     * confirmation stage of the Upload use case.
     */
    public void openFileDialog() {
        // make the JFileChooser resemble the system file manager
        // first store the current Look and Feel, and swap to the new system-based Look and Feel
        LookAndFeel defaultLNF = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
               UnsupportedLookAndFeelException e) {
            // NOTE: it is not particularly consequential if we reach this branch -- we just use
            // the look and feel that was set by default
            System.out.println(e.getMessage());
        }

        // limit file choice to image files
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG & PNG Images", "jpg", "png"
        );
        fileChooser.setFileFilter(filter);

        // prompt user to select an image
        int response = fileChooser.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            controller.switchToConfirmView(fileChooser.getSelectedFile().getAbsolutePath());
        }

        // now return to the Look and Feel that was used before making the switch
        // NOTE that if we do not do this, the button and panel styling will be changed throughout the whole app
        try {
            UIManager.setLookAndFeel(defaultLNF);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * A getter for the view name.
     * @return the view name to be used by view models
     */
    public String getViewName() {
        return "upload select";
    }

    public void setController(UploadController controller) {
        this.controller = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final UploadSelectState state = (UploadSelectState) evt.getNewValue();
        if (state.getError() != null) {
            JOptionPane.showMessageDialog(this,
                    "Please ensure the captured plant is in clear view!\n" +
                            "Your photograph should depict exactly one plant.",
                    state.getError(),
                    JOptionPane.PLAIN_MESSAGE
            );
        }
    }
}
