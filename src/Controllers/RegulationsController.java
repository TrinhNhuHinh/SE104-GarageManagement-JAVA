package Controllers;

import MODEL.ThongTinGarage;
import Service.ThongTinGarageService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class RegulationsController implements Initializable {

    @FXML private TextField txtMaxCarsPerDay;

    @FXML private Button btnUpdateRegulations;
    @FXML private Button btnResetRegulations;
    @FXML private Button btnRefreshRegulations;

    private final ThongTinGarageService thongTinGarageService = new ThongTinGarageService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadSettings();
        setupEvents();
    }

    private void setupEvents() {
        btnUpdateRegulations.setOnAction(e -> handleUpdate());
        btnResetRegulations.setOnAction(e -> handleReset());
        btnRefreshRegulations.setOnAction(e -> loadSettings());
    }

    private void loadSettings() {
        ThongTinGarage settings = thongTinGarageService.getSettings();
        txtMaxCarsPerDay.setText(String.valueOf(settings.getSoLuongXeToiDa()));
    }

    private void handleUpdate() {
        Integer maxCars = getPositiveInt(txtMaxCarsPerDay, "Số xe tối đa mỗi ngày phải là số nguyên dương.");

        if (maxCars == null) {
            return;
        }

        boolean result = thongTinGarageService.updateMaxCarsPerDay(maxCars);

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật quy định thành công.");
            loadSettings();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật quy định.");
        }
    }

    private void handleReset() {
        boolean result = thongTinGarageService.updateMaxCarsPerDay(30);

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã đưa số xe tối đa mỗi ngày về mặc định.");
            loadSettings();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể đưa quy định về mặc định.");
        }
    }

    private Integer getPositiveInt(TextField field, String message) {
        try {
            int value = Integer.parseInt(field.getText().trim());

            if (value <= 0) {
                showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", message);
                return null;
            }

            return value;
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", message);
            return null;
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
