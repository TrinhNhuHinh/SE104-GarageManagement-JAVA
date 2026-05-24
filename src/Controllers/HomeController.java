package Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
    @FXML private Button btnProfile;

    private Button currentActive = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

        btnProfile.setOnAction(e -> {
            setActive(btnProfile);
            loadPage("Profile.fxml");
        });
    }

    private void loadPage(String fxmlFile) {
        try {
            Parent page = FXMLLoader.load(getClass().getResource("/Views/" + fxmlFile));

            AnchorPane.setTopAnchor(page, 0.0);
            AnchorPane.setBottomAnchor(page, 0.0);
            AnchorPane.setLeftAnchor(page, 0.0);
            AnchorPane.setRightAnchor(page, 0.0);

            mainContent.getChildren().setAll(page);

        } catch (Exception e) {
            e.printStackTrace();
        }
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