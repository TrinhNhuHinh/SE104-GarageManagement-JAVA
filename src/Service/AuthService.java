package Service;

import DAO.NguoiDungDAO;
import MODEL.NguoiDung;

public class AuthService {
    private final NguoiDungDAO nguoiDungDAO;

    public static NguoiDung currentUser = null;

    public AuthService() {
        this.nguoiDungDAO = new NguoiDungDAO();
    }

    public NguoiDung login(String userName, String password) {
        if (userName == null || password == null
                || userName.trim().isEmpty() || password.trim().isEmpty()) {
            return null;
        }

        NguoiDung user = nguoiDungDAO.checkLogin(userName.trim(), password);
        if (user != null) {
            currentUser = user;
        }

        return user;
    }

    public String register(String userName, String password, String confirmPass) {
        return "Chức năng đăng ký đang được khóa. Vui lòng dùng tài khoản test: admin/123456 hoặc staff/123456.";
    }

    public static void logOut() {
        currentUser = null;
    }
}
