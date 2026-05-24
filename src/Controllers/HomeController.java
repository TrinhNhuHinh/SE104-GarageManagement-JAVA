package Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane; // thêm import này
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

    private void setActive(Button clicked) {
        if (currentActive != null) {
            currentActive.getStyleClass().remove("activeBtn");
        }
        clicked.getStyleClass().add("activeBtn");
        currentActive = clicked;
    }

    private void loadPage(String fxmlFile) {
        try {
            javafx.scene.Parent page = javafx.fxml.FXMLLoader.load(
                getClass().getResource("/Views/" + fxmlFile)
            );
            AnchorPane.setTopAnchor(page, 0.0);
            AnchorPane.setBottomAnchor(page, 0.0);
            AnchorPane.setLeftAnchor(page, 0.0);
            AnchorPane.setRightAnchor(page, 0.0);
            mainContent.getChildren().setAll(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override //Chỉ có 1 initialize duy nhất
    public void initialize(URL url, ResourceBundle rb) {
        loadPage("dashboard.fxml"); // trang mặc định

        logoIcon.setOnMouseClicked(e -> {
            if (currentActive != null) currentActive.getStyleClass().remove("activeBtn");
            currentActive = null;
            loadPage("dashboard.fxml");
        });
        btnIntake.setOnAction(e -> { setActive(btnIntake); loadPage("intakeservice.fxml"); });
        btnRepairOrders.setOnAction(e -> { setActive(btnRepairOrders); loadPage("repairOrders.fxml"); });
        btnBilling.setOnAction(e -> { setActive(btnBilling); loadPage("billing.fxml"); });
        btnQuickLookup.setOnAction(e -> { setActive(btnQuickLookup); loadPage("quickLookup.fxml"); });
        btnReports.setOnAction(e -> { setActive(btnReports); loadPage("reports.fxml"); });
        btnParts.setOnAction(e -> { setActive(btnParts); loadPage("parts.fxml"); });
        btnProfile.setOnAction(e -> { setActive(btnProfile); loadPage("profile.fxml"); });
    }
}