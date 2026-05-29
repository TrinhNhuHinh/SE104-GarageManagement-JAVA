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
    @FXML private TextField txtMaxBrands;
    @FXML private TextField txtMaxParts;
    @FXML private TextField txtMaxLabors;

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
        ThongTinGarage tt = thongTinGarageService.getSettings();

        txtMaxCarsPerDay.setText(String.valueOf(tt.getSoLuongXeToiDa()));
        txtMaxBrands.setText(String.valueOf(tt.getTongSoHieuXe()));
        txtMaxParts.setText(String.valueOf(tt.getSoLuongVatTuToiDa()));
        txtMaxLabors.setText(String.valueOf(tt.getSoLuongTienCongToiDa()));
    }

    private void handleUpdate() {
        Integer maxCars = getPositiveInt(txtMaxCarsPerDay, "Số xe tối đa mỗi ngày phải là số nguyên dương!");
        Integer maxBrands = getPositiveInt(txtMaxBrands, "Số hiệu xe tối đa phải là số nguyên dương!");
        Integer maxParts = getPositiveInt(txtMaxParts, "Số loại vật tư tối đa phải là số nguyên dương!");
        Integer maxLabors = getPositiveInt(txtMaxLabors, "Số loại tiền công tối đa phải là số nguyên dương!");

        if (maxCars == null || maxBrands == null || maxParts == null || maxLabors == null) {
            return;
        }

        boolean result = thongTinGarageService.updateSettings(
                maxCars,
                maxBrands,
                maxParts,
                maxLabors
        );

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật quy định thành công!");
            loadSettings();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật quy định!");
        }
    }

    private void handleReset() {
        boolean result = thongTinGarageService.resetDefault();

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã reset quy định về mặc định!");
            loadSettings();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể reset quy định!");
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