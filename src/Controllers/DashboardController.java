package Controllers;

import MODEL.NguoiDung;
import MODEL.SessionManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class DashboardController implements Initializable {

    @FXML private Label wcLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        NguoiDung user = SessionManager.getCurrentUser();

        if (user != null) {
            wcLabel.setText("Xin chào, " + user.getTenTaiKhoan() + "!");
        } else {
            wcLabel.setText("Xin chào!");
        }
    }
}