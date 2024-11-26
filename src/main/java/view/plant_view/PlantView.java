package view.plant_view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;

/**
 * A base design for views that display Plant objects. This design contains four sections:
 * the top options bar, the image panel, the plant details panel, and the user action panel.
 */
public class PlantView extends JPanel {
    private final JLabel nameLabel = new JLabel();
    private final JLabel scientificNameLabel = new JLabel();
    private final JLabel familyLabel = new JLabel();
    private final JLabel certaintyLabel = new JLabel();
    private final JLabel ownerLabel = new JLabel();

    private final JLabel likesLabel = new JLabel();
    private final JTextArea notesField = new JTextArea();
    private final JToggleButton togglePublic = new JToggleButton();

    private BufferedImage image;

    public PlantView() {
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(PlantViewData.TRANSPARENT, true));

        // position each component nicely within the view area using a GridBagLayout
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        this.add(createTopPanel(), constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        this.add(createImagePanel(), constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.ipady = 20;
        constraints.weighty = 1;
        this.add(createContentPanel(), constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.ipadx = 20;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.SOUTH;
        this.add(createActionPanel(), constraints);
    }

    /**
     * Create the panel seen at the top of the view, containing the return button.
     * @return a reference to the created panel
     */
    protected JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(new Color(PlantViewData.TOP_PANEL_COLOR, true));
        topPanel.setPreferredSize(new Dimension(
                PlantViewData.PANEL_WIDTH,
                PlantViewData.TOP_PANEL_HEIGHT
        ));

        return topPanel;
    }

    /**
     * Create the panel within which information about the plant is displayed, containing details
     * such as the plant name and family, as well as user-modifiable fields such as the privacy
     * toggle and a text area for user notes.
     * @return a reference to the created panel
     */
    protected JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        SpringLayout layout = new SpringLayout();
        contentPanel.setLayout(layout);
        contentPanel.setBackground(new Color(PlantViewData.CONTENT_PANEL_COLOR, true));

        Font font = nameLabel.getFont();
        nameLabel.setFont(font.deriveFont(Font.BOLD).deriveFont(20f));
        layout.putConstraint(SpringLayout.WEST, nameLabel, 20, SpringLayout.WEST, contentPanel);
        layout.putConstraint(SpringLayout.NORTH, nameLabel, 20, SpringLayout.NORTH, contentPanel);
        contentPanel.add(nameLabel);

        scientificNameLabel.setFont(font.deriveFont(Font.ITALIC).deriveFont(16f));
        layout.putConstraint(SpringLayout.WEST, scientificNameLabel, 20, SpringLayout.WEST, contentPanel);
        layout.putConstraint(SpringLayout.NORTH, scientificNameLabel, 5, SpringLayout.SOUTH, nameLabel);
        contentPanel.add(scientificNameLabel);

        familyLabel.setFont(font.deriveFont(Font.PLAIN).deriveFont(14f));
        layout.putConstraint(SpringLayout.WEST, familyLabel, 20, SpringLayout.WEST, contentPanel);
        layout.putConstraint(SpringLayout.NORTH, familyLabel, 5, SpringLayout.SOUTH, scientificNameLabel);
        contentPanel.add(familyLabel);

        certaintyLabel.setFont(font.deriveFont(Font.PLAIN).deriveFont(14f));
        layout.putConstraint(SpringLayout.WEST, certaintyLabel, 20, SpringLayout.WEST, contentPanel);
        layout.putConstraint(SpringLayout.NORTH, certaintyLabel, 15, SpringLayout.SOUTH, familyLabel);
        contentPanel.add(certaintyLabel);

        notesField.setRows(10);
        notesField.setFont(font.deriveFont(Font.PLAIN).deriveFont(12f));
        notesField.setLineWrap(true);
        notesField.setWrapStyleWord(true);
        notesField.setMargin(new Insets(10, 10, 10, 10));
        notesField.setBackground(new Color(PlantViewData.CONTENT_PANEL_COLOR, true));
        JScrollPane notesScrollPane = new JScrollPane(notesField);

        layout.putConstraint(SpringLayout.WEST, notesScrollPane, 20, SpringLayout.WEST, contentPanel);
        layout.putConstraint(SpringLayout.EAST, notesScrollPane, -20, SpringLayout.EAST, contentPanel);
        layout.putConstraint(SpringLayout.NORTH, notesScrollPane, 35, SpringLayout.SOUTH, familyLabel);
        contentPanel.add(notesScrollPane);

        ownerLabel.setFont(font.deriveFont(Font.PLAIN).deriveFont(12f));
        layout.putConstraint(SpringLayout.WEST, ownerLabel, 20, SpringLayout.WEST, contentPanel);
        layout.putConstraint(SpringLayout.NORTH, ownerLabel, 15, SpringLayout.SOUTH, notesScrollPane);
        ownerLabel.setVisible(false);
        contentPanel.add(ownerLabel);

        likesLabel.setFont(font.deriveFont(Font.PLAIN).deriveFont(13f));
        layout.putConstraint(SpringLayout.EAST, likesLabel, -22, SpringLayout.EAST, contentPanel);
        layout.putConstraint(SpringLayout.NORTH, likesLabel, 15, SpringLayout.SOUTH, notesScrollPane);
        likesLabel.setVisible(false);
        contentPanel.add(likesLabel);

        // default privacy to public
        togglePublic.setFont(font.deriveFont(16f));
        togglePublic.setMargin(new Insets(0, 0, 0, 0));
        togglePublic.setPreferredSize(new Dimension(30, 30));
        togglePublic.setBorder(null);
        togglePublic.setFocusPainted(false);
        togglePublic.setText("\uD83D\uDD12");
        // enable a different color when privacy is toggled
        togglePublic.setBackground(new Color(PlantViewData.PRIVATE_TOGGLE_COLOR, true));
        UIManager.put("ToggleButton.select",
                new Color(PlantViewData.PUBLIC_TOGGLE_COLOR, true));
        SwingUtilities.updateComponentTreeUI(togglePublic);
        // enable user feedback when they toggle privacy
        togglePublic.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                togglePublic.setText("\uD83D\uDD13");
            }
            else {
                togglePublic.setText("\uD83D\uDD12");
            }
        });

        layout.putConstraint(SpringLayout.EAST, togglePublic, -20, SpringLayout.EAST, contentPanel);
        layout.putConstraint(SpringLayout.NORTH, togglePublic, 20, SpringLayout.NORTH, contentPanel);
        contentPanel.add(togglePublic);

        return contentPanel;
    }

    /**
     * Create the image panel, in charge of displaying the plant image.
     * @return a reference to the created panel
     */
    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (image != null) {
                    g.drawImage(image, 0, 0,
                            PlantViewData.IMAGE_WIDTH,
                            PlantViewData.IMAGE_HEIGHT,
                            this
                    );
                }
            }
        };
        imagePanel.setPreferredSize(new Dimension(
                PlantViewData.IMAGE_WIDTH,
                PlantViewData.IMAGE_HEIGHT
        ));
        return imagePanel;
    }

    /**
     * Create the panel within which user action buttons are displayed. The available actions
     * depend on the child class for implementation.
     * @return a reference to the created panel
     */
    protected JPanel createActionPanel() {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new GridBagLayout());
        actionPanel.setBackground(new Color(PlantViewData.ACTION_PANEL_COLOR));

        return actionPanel;
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public JLabel getScientificNameLabel() {
        return scientificNameLabel;
    }

    public JLabel getFamilyLabel() {
        return familyLabel;
    }

    public JLabel getCertaintyLabel() {
        return certaintyLabel;
    }

    public JLabel getOwnerLabel() { return ownerLabel; }

    public JLabel getLikesLabel() {
        return likesLabel;
    }

    public JTextArea getNotesField() {
        return notesField;
    }

    public JToggleButton getTogglePublic() {
        return togglePublic;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
