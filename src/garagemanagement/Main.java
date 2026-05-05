package garagemanagement; // Nhớ giữ nguyên tên package của ní nha

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // 1. Dùng FXMLLoader để móc cái file thiết kế mặt tiền của ní lên
        // LƯU Ý CHÍ MẠNG: Đảm bảo đường dẫn "/Views/LognReg.fxml" đúng với thư mục của ní!
        Parent root = FXMLLoader.load(getClass().getResource("/Views/LognReg.fxml"));
        
        // 2. Nhét nó vào Scene
        Scene scene = new Scene(root);
        
        // 3. Setup cửa sổ (Stage)
        stage.setTitle("Garage Management System"); 
        stage.setScene(scene);
        stage.setResizable(false); // Khóa không cho user kéo dãn màn hình cho đỡ vỡ layout
        
        // 4. Lên đèn!
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}