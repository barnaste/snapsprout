    package view;

    import java.awt.*;
    import java.beans.PropertyChangeEvent;
    import java.beans.PropertyChangeListener;
    import java.util.List;

    import javax.swing.*;

    import data_access.MongoUserDataAccessObject;
    import entity.Plant;
    import interface_adapter.ViewManagerModel;
    import interface_adapter.delete_user.DeleteUserController;
    import interface_adapter.delete_user.DeleteUserViewModel;
    import interface_adapter.login.LoginViewModel;
    import interface_adapter.logout.LogoutController;
    import interface_adapter.logout.LogoutPresenter;
    import interface_adapter.main.MainState;
    import interface_adapter.main.MainViewModel;
    import interface_adapter.mode_switch.ModeSwitchController;
    import interface_adapter.mode_switch.ModeSwitchPresenter;
    import interface_adapter.mode_switch.ModeSwitchState;
    import interface_adapter.mode_switch.ModeSwitchViewModel;
    import use_case.logout.LogoutInputBoundary;
    import use_case.logout.LogoutInteractor;
    import use_case.logout.LogoutOutputBoundary;
    import use_case.mode_switch.ModeSwitchInteractor;
    import view.gallery.PublicGalleryView;
    import view.gallery.UserGalleryView;
    import view.panel_factory.*;

    /**
     * The Main View, for when the user is logged into the program.
     */
    public class MainView extends JLayeredPane implements PropertyChangeListener {
        private static final int OVERLAY_COLOR = 0x40829181;
        private static final int DISPLAY_WIDTH = 1080;
        private static final int DISPLAY_HEIGHT = 720;
        final Dimension buttonSize = new Dimension(200, 50);

        private final String viewName = "main view";
        private PublicGalleryView publicGalleryView;
        private UserGalleryView userGalleryView;

        private LogoutController logoutController;
        private ModeSwitchController modeSwitchController;

        private String currentUser = "";
        private String currentGalleryMode = "";
        private JButton currentGalleryBtn;
        private final JPanel currentGalleryPanel;
        private final JLabel userLabel = new JLabel();
        private final JLabel title = new JLabel();

        private final JButton logOut;
        private final JButton upload;
        private final JButton myPlantsButton;
        private final JButton discoverButton;
        private final JButton deleteUserButton;

        private final ViewManagerModel viewManagerModel;
        private final LoginViewModel loginViewModel;
        private final MainViewModel mainViewModel;

        public MainView(MainViewModel mainViewModel, ViewManagerModel viewManagerModel, LoginViewModel loginViewModel) {
            this.viewManagerModel = viewManagerModel;
            this.loginViewModel = loginViewModel;
            this.mainViewModel = mainViewModel;

            mainViewModel.addPropertyChangeListener(this);

            enableModeSwitch();
            enableLogout();

            this.publicGalleryView = PublicGalleryFactory.createPublicGallery(this::overlayPublicPlantView, mainViewModel);
            this.userGalleryView = UserGalleryFactory.createUserGallery(plant -> {
                overlayEditPlantView(plant, mainViewModel);
            });

            this.setLayout(new OverlayLayout(this));
            this.setPreferredSize(new Dimension(DISPLAY_WIDTH, DISPLAY_HEIGHT));

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());

            currentGalleryMode = "My Plants Gallery";

            title.setText(currentGalleryMode);
            title.setFont(new Font("Arial", Font.BOLD, 18));
            title.setForeground(new Color(0x3C7339));

            final JPanel header = ViewComponentFactory.buildVerticalPanel(List.of(title, userLabel));
            header.setOpaque(false);

            upload = ViewComponentFactory.buildButton("Upload");
            logOut = ViewComponentFactory.buildButton("Log Out");
            myPlantsButton = ViewComponentFactory.buildButton("My Plants");
            discoverButton = ViewComponentFactory.buildButton("Discover");
            deleteUserButton = ViewComponentFactory.buildButton("Delete My Account");

            upload.addActionListener(evt -> overlayUploadView(mainViewModel));
            logOut.addActionListener(e -> logoutController.execute(mainViewModel.getState().getUsername()));
            deleteUserButton.addActionListener(e -> overlayDeleteUserView());

            myPlantsButton.addActionListener(e -> modeSwitchController.switchMode());
            myPlantsButton.setEnabled(false);
            discoverButton.addActionListener(e -> modeSwitchController.switchMode());

            ViewComponentFactory.setButtonSize(myPlantsButton, buttonSize);
            ViewComponentFactory.setButtonSize(logOut, buttonSize);
            ViewComponentFactory.setButtonSize(upload, buttonSize);
            ViewComponentFactory.setButtonSize(discoverButton, buttonSize);
            ViewComponentFactory.setButtonSize(deleteUserButton, buttonSize);

            deleteUserButton.setForeground(new Color(150, 0, 0));
            logOut.setForeground(new Color(150, 32, 32));

            // Make the panel on the left of the screen (Upload, mode toggle, and Log Out)
            JPanel spacer1 = new JPanel();
            spacer1.setOpaque(false);
            spacer1.setPreferredSize(new Dimension(10, 130));

            JPanel spacer2 = new JPanel();
            spacer2.setOpaque(false);
            spacer2.setPreferredSize(new Dimension(10, 20));

            final JPanel actionPanel = ViewComponentFactory.buildVerticalPanel(List.of(title, header, spacer2, upload,
                    myPlantsButton, discoverButton, spacer1, logOut, deleteUserButton));

            currentGalleryPanel = new JPanel();

            mainPanel.add(ViewComponentFactory.buildHorizontalPanel(List.of(actionPanel, currentGalleryPanel)));

            this.add(mainPanel, JLayeredPane.DEFAULT_LAYER);
        }

        /**
         * Disable all interactions within the main view. That is, all components and subcomponents are
         * no longer active after this method is called.
         */
        private void disableInteraction() {
            // if the discover button is currently enabled, the user is currently in the discovery gallery
            currentGalleryBtn = !this.discoverButton.isEnabled() ? this.discoverButton : this.myPlantsButton;
            setComponentsEnabled(this, false);
        }

        /**
         * Enables all interactions within the main view. That is, all components and subcomponents are
         * restored after this method is called. The current gallery's corresponding button will be disabled
         * to disallow the user to "activate" an already active gallery.
         */
        private void enableInteraction() {
            setComponentsEnabled(this, true);
            currentGalleryBtn.setEnabled(false);
        }

        /**
         * Enables or disables all buttons within the input panel. This includes buttons
         * directly within the input panel and any buttons within components of the input
         * panel, recursively.
         * @param panel the panel to modify the enablement of buttons in
         * @param isEnabled whether buttons should be enabled or disabled
         */
        private void setComponentsEnabled(Container panel, boolean isEnabled) {
            for (Component cp : panel.getComponents()) {
                if (cp instanceof JPanel) {
                    setComponentsEnabled((JPanel) cp, isEnabled);
                }
                cp.setEnabled(isEnabled);
            }
        }

        /**
         * Create an overlay on the main view that displays the upload use case dialog.
         */
        public void overlayUploadView(MainViewModel mainViewModel) {
            // create an overlay with the created cardPanel as the popup
            JPanel overlayPanel = new JPanel();
            UploadPanelFactory.createUploadPanel(this, overlayPanel, overlay(overlayPanel), mainViewModel);
        }

        public void overlayDeleteUserView() {
            // create an overlay with the created cardPanel as the popup
            JPanel overlayPanel = new JPanel();
            DeleteUserPanelFactory.createDeleteUserPanel(this, overlayPanel, overlay(overlayPanel), viewManagerModel, mainViewModel, loginViewModel);
        }

        /**
         * Create an overlay on the main view that displays the edit plant use case dialog.
         * @param plant the plant object to be displayed in the panel
         */
        public void overlayEditPlantView(Plant plant, MainViewModel mainViewModel) {
            // create an overlay with the created cardPanel as the popup
            JPanel overlayPanel = new JPanel();
            UserPlantInfoEditPanelFactory.createEditPlantPanel(plant, overlayPanel, overlay(overlayPanel), mainViewModel);
        }

        /**
         * Create an overlay on the main view that displays the view plant use case dialog.
         * @param plant the plant object to be displayed in the panel
         */
        public void overlayPublicPlantView(Plant plant) {
            // create an overlay with the created cardPanel as the popup
            JPanel overlayPanel = new JPanel();
            PublicPlantInfoPanelFactory.createPublicPlantPanel(plant, overlayPanel, overlay(overlayPanel));
        }

        /**
         * Create an overlay environment that displays the input panel. This involves invalidating all entities
         * currently on the main screen, and creating a semi-transparent layer between the main area and the newly
         * overlain panel.
         * @param overlayPanel the panel to create an overlay for
         * @return a runnable escape map -- a method to be called when the overlay is to be closed
         */
        public Runnable overlay(JPanel overlayPanel) {
            // disable any interaction outside the overlay
            this.disableInteraction();

            // create a semi-transparent overlay
            // override painting functionality so that our overlay is semi-transparent
            JPanel backgroundPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(new Color(OVERLAY_COLOR, true)); // overlay color with transparency
                    g.fillRect(0, 0, getWidth(), getHeight()); // fill the panel area with color
                }
            };
            backgroundPanel.setOpaque(false);
            // use a GridBagLayout to keep overlay content centered on the screen
            backgroundPanel.setLayout(new GridBagLayout());

            backgroundPanel.add(overlayPanel);
            this.add(backgroundPanel, JLayeredPane.PALETTE_LAYER);
            this.revalidate();
            this.repaint();

            return () -> {
                // enable interaction outside the overlay once the overlay is removed
                this.enableInteraction();

                this.remove(backgroundPanel);
                this.revalidate();
                this.repaint();
            };
        }

        private void enableModeSwitch() {
            ModeSwitchViewModel modeSwitchViewModel = new ModeSwitchViewModel();
            modeSwitchViewModel.addPropertyChangeListener(this);

            final ModeSwitchPresenter modeSwitchPresenter = new ModeSwitchPresenter(modeSwitchViewModel);

            ModeSwitchInteractor modeSwitchInteractor = new ModeSwitchInteractor(modeSwitchPresenter);

            modeSwitchController = new ModeSwitchController(modeSwitchInteractor);
        }

        private void enableLogout() {
            final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel, mainViewModel, loginViewModel);

            final LogoutInputBoundary logoutInteractor = new LogoutInteractor(MongoUserDataAccessObject.getInstance(), logoutOutputBoundary);

            logoutController = new LogoutController(logoutInteractor);
        }

        public String getViewName() {
            return viewName;
        }

        private void updateModeUI(ModeSwitchState.Mode mode) {
            if (currentGalleryPanel != null)
                this.remove(currentGalleryPanel);
            if (mode == ModeSwitchState.Mode.DISCOVER) {
                // Update UI for "Discover" mode
                setDiscoverPanel();
                currentGalleryMode = "Discover Gallery";
                title.setText(currentGalleryMode);
                myPlantsButton.setEnabled(true);
                discoverButton.setEnabled(false);
            } else if (mode == ModeSwitchState.Mode.MY_PLANTS) {
                // Update UI for "My Plants" mode
                setMyPlantsPanel();
                currentGalleryMode = "My Plants Gallery";
                title.setText(currentGalleryMode);
                myPlantsButton.setEnabled(false);
                discoverButton.setEnabled(true);
            }
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("logged_in")) {
                final MainState state = (MainState) evt.getNewValue();
                currentUser = state.getUsername();
                userLabel.setText("Hello " + this.currentUser + "!");

                userGalleryView.refresh();
                publicGalleryView.refresh();

                updateModeUI(ModeSwitchState.Mode.MY_PLANTS);
            } else if (evt.getPropertyName().equals("mode_switch")) {
                final ModeSwitchState modeSwitchState = (ModeSwitchState) evt.getNewValue();
                updateModeUI(modeSwitchState.getCurrentMode());
            } else if (evt.getPropertyName().equals("refresh")) {
                userGalleryView.refresh();
                publicGalleryView.refresh();
            }
        }

        // Create "My Plants" panel
        private void setMyPlantsPanel() {
            currentGalleryPanel.removeAll();

            currentGalleryPanel.setPreferredSize(new Dimension(840, 700));

            currentGalleryPanel.add(userGalleryView, userGalleryView.getViewName());

            currentGalleryPanel.revalidate();
            currentGalleryPanel.repaint();
        }

        // Create "Discover" panel
        private void setDiscoverPanel() {
            currentGalleryPanel.removeAll();

            currentGalleryPanel.setPreferredSize(new Dimension(840, 700));

            currentGalleryPanel.add(publicGalleryView, publicGalleryView.getViewName());

            currentGalleryPanel.revalidate();
            currentGalleryPanel.repaint();
        }
    }