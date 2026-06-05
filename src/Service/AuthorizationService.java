package Service;

import MODEL.NguoiDung;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AuthorizationService {

    public static final String ROLE_ADMIN = "CV01";
    public static final String ROLE_STAFF = "CV02";

    private static final Set<String> ADMIN_ONLY_PAGES = new HashSet<>(Arrays.asList(
            "StatisticalReports.fxml",
            "Regulations.fxml"
    ));

    private AuthorizationService() {
    }

    public static boolean isAdmin() {
        return isAdmin(AuthService.currentUser);
    }

    public static boolean isAdmin(NguoiDung user) {
        return user != null && ROLE_ADMIN.equalsIgnoreCase(normalizeRole(user.getMaChucVu()));
    }

    public static boolean isStaff(NguoiDung user) {
        return user != null && ROLE_STAFF.equalsIgnoreCase(normalizeRole(user.getMaChucVu()));
    }

    public static boolean canAccessPage(String fxmlFile) {
        if (AuthService.currentUser == null) {
            return false;
        }

        if (ADMIN_ONLY_PAGES.contains(fxmlFile)) {
            return isAdmin();
        }

        return true;
    }

    public static String getRoleName(NguoiDung user) {
        if (isAdmin(user)) {
            return "Quản trị viên";
        }

        if (isStaff(user)) {
            return "Nhân viên";
        }

        return "Không xác định";
    }

    private static String normalizeRole(String role) {
        if (role == null) {
            return "";
        }

        return role.trim();
    }
}
