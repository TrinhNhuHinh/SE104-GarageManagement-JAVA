package Controllers;

import Service.AuthorizationService;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

public class HomeController implements Initializable {

    @FXML private AnchorPane mainContent;
    @FXML private Region logoIcon;

    @FXML private Button btnIntake;
    @FXML private Button btnRepairOrders;
    @FXML private Button btnBilling;
    @FXML private Button btnQuickLookup;
    @FXML private Button btnReports;
    @FXML private Button btnParts;
    @FXML private Button btnSettings;
    @FXML private Button btnRegulations;

    private Button currentActive = null;
    private final Map<String, Parent> pageCache = new HashMap<>();
    private final Map<String, Object> controllerCache = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        applyRolePermissions();
        loadPage("Dashboard.fxml");

        logoIcon.setOnMouseClicked(e -> {
            clearActiveButton();
            loadPage("Dashboard.fxml");
        });

        btnIntake.setOnAction(e -> {
            setActive(btnIntake);
            loadPage("IntakeService.fxml");
        });

        btnRepairOrders.setOnAction(e -> {
            setActive(btnRepairOrders);
            loadPage("RepairOrders.fxml");
        });

        btnBilling.setOnAction(e -> {
            setActive(btnBilling);
            loadPage("BillingCollections.fxml");
        });

        btnQuickLookup.setOnAction(e -> {
            setActive(btnQuickLookup);
            loadPage("QuickLookup.fxml");
        });

        btnReports.setOnAction(e -> {
            setActive(btnReports);
            loadPage("StatisticalReports.fxml");
        });

        btnParts.setOnAction(e -> {
            setActive(btnParts);
            loadPage("PartsManagement.fxml");
        });

        btnSettings.setOnAction(e -> {
            setActive(btnSettings);
            loadPage("Settings.fxml");
        });

        btnRegulations.setOnAction(e -> {
            setActive(btnRegulations);
            loadPage("Regulations.fxml");
        });
    }

    private void loadPage(String fxmlFile) {
        if (!AuthorizationService.canAccessPage(fxmlFile)) {
            showAccessDenied();
            return;
        }

        try {
            Parent page = pageCache.get(fxmlFile);
            boolean cached = page != null;

            if (page == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/" + fxmlFile));
                page = loader.load();

                AnchorPane.setTopAnchor(page, 0.0);
                AnchorPane.setBottomAnchor(page, 0.0);
                AnchorPane.setLeftAnchor(page, 0.0);
                AnchorPane.setRightAnchor(page, 0.0);

                pageCache.put(fxmlFile, page);
                controllerCache.put(fxmlFile, loader.getController());
            }

            if (cached && "Dashboard.fxml".equals(fxmlFile)) {
                refreshPage(fxmlFile);
            }

            mainContent.getChildren().setAll(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshPage(String fxmlFile) {
        Object controller = controllerCache.get(fxmlFile);

        if (controller instanceof Refreshable refreshable) {
            refreshable.refreshData();
        }
    }

    private void applyRolePermissions() {
        boolean admin = AuthorizationService.isAdmin();

        setButtonVisible(btnReports, admin);
        setButtonVisible(btnParts, admin);
        setButtonVisible(btnRegulations, admin);
    }

    private void setButtonVisible(Button button, boolean visible) {
        if (button == null) {
            return;
        }

        button.setVisible(visible);
        button.setManaged(visible);
    }

    private void showAccessDenied() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Không có quyền truy cập");
        alert.setHeaderText(null);
        alert.setContentText("Tài khoản hiện tại không có quyền mở chức năng này.");
        alert.showAndWait();
    }

    private void setActive(Button clicked) {
        clearActiveButton();

        if (!clicked.getStyleClass().contains("activeBtn")) {
            clicked.getStyleClass().add("activeBtn");
        }

        currentActive = clicked;
    }

    private void clearActiveButton() {
        if (currentActive != null) {
            currentActive.getStyleClass().remove("activeBtn");
            currentActive = null;
        }
    }
}
