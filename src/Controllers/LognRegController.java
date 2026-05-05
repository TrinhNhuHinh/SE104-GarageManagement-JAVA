package Controllers;

import Service.AuthService;
import MODEL.NguoiDung;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;

public class LognRegController implements Initializable {
    
    //Móc nối fx:id với giao diện
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtPasswordrg;
    @FXML private TextField txtUsernamerg;
    @FXML private PasswordField txtPasswordrg1;

    //Khởi tạo service
    private AuthService authService = new AuthService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Hàm này chạy đầu tiên khi mở form lên
    }
    
    //Gọi Modal login
    @FXML
    public void openLoginModal() {
        try {
            //Tải file giao diện popup (Nhớ check lại chữ l viết thường hay hoa nha, tui thấy trong clip là loginModal.fxml)
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/Views/loginModal.fxml")); 
            
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            javafx.stage.Stage popupStage = new javafx.stage.Stage();
            
            popupStage.setScene(scene);
            popupStage.setTitle("Đăng nhập Hệ thống");
            
            //Khóa màn hình chính lại khi popup hiện lên
            popupStage.initModality(javafx.stage.Modality.APPLICATION_MODAL); 
            
            //Hiển thị!
            popupStage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi: Không tìm thấy file loginModal.fxml!");
        }
    }

    //Hàm xử lý khi bấm nút Đăng Nhập
    @FXML
    public void btnLoginAction(ActionEvent event) {
        //Lấy chữ người dùng gõ
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        //Đưa dữ liệu cho service kiểm tra trên database
        NguoiDung loggedInUser = authService.login(username, password);

        if (loggedInUser != null) {
            // -- ĐĂNG NHẬP THÀNH CÔNG --
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công");
            alert.setHeaderText(null);
            alert.setContentText("Xin chào " + loggedInUser.getTenTaiKhoan() + "! Đăng nhập thành công.");
            alert.showAndWait();

            // Đóng popup đăng nhập lại
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            // TODO: Code gọi màn hình Dashboard (Làm ở bước sau)
            
        } else {
            // -- ĐĂNG NHẬP THẤT BẠI --
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi đăng nhập");
            alert.setHeaderText(null);
            alert.setContentText("Sai tài khoản, mật khẩu hoặc không tồn tại!");
            alert.showAndWait();
        }
    }
    
    //Gọi modal register
    @FXML
    public void openRegisterModal() {
        try {
            //Tải file giao diện popup (Nhớ check lại chữ l viết thường hay hoa nha, tui thấy trong clip là loginModal.fxml)
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/Views/regisModal.fxml")); 
            
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            javafx.stage.Stage popupStage = new javafx.stage.Stage();
            
            popupStage.setScene(scene);
            popupStage.setTitle("Đăng ký");
            
            //Khóa màn hình chính lại khi popup hiện lên
            popupStage.initModality(javafx.stage.Modality.APPLICATION_MODAL); 
            
            //Hiển thị!
            popupStage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi: Không tìm thấy file regisModal.fxml!");
        }
    }
    
    //Hàm xử lý khi bấm nút Đăng Ký
    @FXML
    public void btnRegisAction(ActionEvent event) {
        //Lấy chữ người dùng gõ
        String username = txtUsernamerg.getText();
        String password = txtPasswordrg.getText();
        String password1 = txtPasswordrg1.getText();

        String result = authService.register(username, password, password1);

    if (result.equals("SUCCESS")) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Tạo tài khoản thành công! Giờ bạn có thể đăng nhập.");
        alert.showAndWait();
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        
    } else {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Lỗi đăng ký");
        alert.setHeaderText(null);
        alert.setContentText(result);
        alert.showAndWait();
    }
}
    
    @FXML
    public void switchToRegister(javafx.scene.input.MouseEvent event) {
        // Lấy cái cửa sổ Popup Đăng Nhập hiện tại và đóng nó lại
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        
        // Đóng xong rồi thì gọi hàm mở Popup Đăng Ký lên
        openRegisterModal();
    }

    @FXML
    public void switchToLogin(javafx.scene.input.MouseEvent event) {
        // Lấy cái cửa sổ Popup Đăng Ký hiện tại và đóng nó lại
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        
        // Đóng xong rồi thì gọi hàm mở Popup Đăng Nhập lên
        openLoginModal();
    }
}