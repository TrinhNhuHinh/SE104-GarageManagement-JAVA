package Controllers;

import MODEL.NguoiDung;
import Service.AuthService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LognRegController implements Initializable {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtUsernamerg;
    @FXML private PasswordField txtPasswordrg;
    @FXML private PasswordField txtPasswordrg1;

    private final AuthService authService = new AuthService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    // ==================== LOGIN ====================

    @FXML
    public void openLoginModal() {
        openModal("/Views/loginModal.fxml", "Đăng nhập");
    }

    @FXML
    public void btnLoginAction(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        NguoiDung loggedInUser = authService.login(username, password);

        if (loggedInUser != null) {

            // Đóng hết màn hình cũ
            new java.util.ArrayList<>(Stage.getWindows())
                .forEach(w -> ((Stage) w).close());

            // Mở Home
            openScreen("/Views/home.fxml", "Home");

        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi đăng nhập",
                "Sai tài khoản, mật khẩu hoặc không tồn tại!");
        }
    }

    // ==================== REGISTER ====================

    @FXML
    public void openRegisterModal() {
        openModal("/Views/regisModal.fxml", "Đăng ký");
    }

    @FXML
    public void btnRegisAction(ActionEvent event) {
        String username = txtUsernamerg.getText();
        String password = txtPasswordrg.getText();
        String confirm = txtPasswordrg1.getText();

        String result = authService.register(username, password, confirm);

        if (result.equals("SUCCESS")) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công",
                "Tạo tài khoản thành công! Giờ bạn có thể đăng nhập.");
            closeCurrentWindow(event);
        } else {
            showAlert(Alert.AlertType.WARNING, "Lỗi đăng ký", result);
        }
    }

    // ==================== SWITCH ====================

    @FXML
    public void switchToRegister(javafx.scene.input.MouseEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        openRegisterModal();
    }

    @FXML
    public void switchToLogin(javafx.scene.input.MouseEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        openLoginModal();
    }

    // ==================== HELPER ====================

    private void openModal(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage popup = new Stage();
            popup.setScene(new Scene(root));
            popup.setTitle(title);
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openScreen(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeCurrentWindow(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}