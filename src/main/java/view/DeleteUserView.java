package view;

import interface_adapter.delete_user.DeleteUserController;
import interface_adapter.delete_user.DeleteUserViewModel;
import interface_adapter.upload.select.UploadSelectViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class DeleteUserView extends JPanel implements PropertyChangeListener {

    private final String viewName = "delete user";
    private DeleteUserController controller;

    JTextField usernameInputField = new JTextField(15);
    JPasswordField passwordInputField = new JPasswordField(15);
    JLabel usernameLabel = new JLabel("Enter Username:");
    JLabel passwordLabel = new JLabel("Enter Password:");

    JButton confirmButton = ViewComponentFactory.buildButton("Confirm");

    public DeleteUserView(DeleteUserViewModel deleteUserViewModel) {
        deleteUserViewModel.addPropertyChangeListener(this);

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

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(400, 25));

        JButton cancelBtn = ViewComponentFactory.buildButton("× Cancel");
        cancelBtn.setBorderPainted(false);

        cancelBtn.addActionListener((e) -> controller.escape());
        topPanel.add(cancelBtn, BorderLayout.WEST);
        return topPanel;
    }

    private JPanel createMainPanel() {
        JLabel warningMsg = new JLabel("Confirm Account Deletion");

        Font warningFont = new Font("Arial", Font.PLAIN, 16);
        warningMsg.setFont(warningFont);
        warningMsg.setForeground(Color.RED);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setPreferredSize(new Dimension(400, 250));

        JPanel usernameFields = ViewComponentFactory.buildHorizontalPanel(List.of(usernameLabel, usernameInputField));
        JPanel passwordFields = ViewComponentFactory.buildHorizontalPanel(List.of(passwordLabel, passwordInputField));

        // Add warning message, username fields, and password fields using GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around the components
        mainPanel.add(warningMsg, gbc);

        gbc.gridy = 1; // Next row for username fields
        mainPanel.add(usernameFields, gbc);

        gbc.gridy = 2; // Next row for password fields
        mainPanel.add(passwordFields, gbc);

        // Now add the confirm button (centered in the next row)
        gbc.gridy = 3; // Next row for the confirm button
        gbc.gridwidth = 1; // It will occupy one cell horizontally
        gbc.anchor = GridBagConstraints.CENTER; // Center the button in the panel
        mainPanel.add(confirmButton, gbc);

        // Add action listener for the confirm button
        confirmButton.addActionListener((e) -> {
            controller.execute(usernameInputField.getText(), new String(passwordInputField.getPassword()));
        });

        return mainPanel;
    }

    public void setController(DeleteUserController controller) {
        this.controller = controller;
    }

    public String getViewName() {
        return viewName;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("error")) {
            JOptionPane.showMessageDialog(this, DeleteUserViewModel.LOGIN_ERROR_MESSAGE);
        }
    }
}