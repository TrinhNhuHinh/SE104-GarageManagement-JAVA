package Service;

import DAO.ChucVuDAO;
import MODEL.ChucVu;
import MODEL.NguoiDung;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AuthorizationService {

    public static final String ROLE_ADMIN = "CV01";
    public static final String ROLE_STAFF = "CV02";

    public static final String PERMISSION_ALL = "QH01";
    public static final String PERMISSION_BASIC = "QH02";
    public static final String PERMISSION_REPORTS = "QH03";
    public static final String PERMISSION_SETTINGS = "QH04";
    public static final String PERMISSION_ROLE_MANAGEMENT = "QH05";

    private static final ChucVuDAO chucVuDAO = new ChucVuDAO();
    private static final Map<String, String> PAGE_PERMISSIONS = new HashMap<>();

    static {
        PAGE_PERMISSIONS.put("StatisticalReports.fxml", PERMISSION_REPORTS);
        PAGE_PERMISSIONS.put("Settings.fxml", PERMISSION_SETTINGS);
        PAGE_PERMISSIONS.put("Regulations.fxml", PERMISSION_SETTINGS);
    }

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

        String requiredPermission = PAGE_PERMISSIONS.get(fxmlFile);
        return requiredPermission == null || hasPermission(AuthService.currentUser, requiredPermission);
    }

    public static boolean hasPermission(String permissionId) {
        return hasPermission(AuthService.currentUser, permissionId);
    }

    public static boolean hasPermission(NguoiDung user, String permissionId) {
        if (user == null || permissionId == null || permissionId.trim().isEmpty()) {
            return false;
        }

        if (isAdmin(user)) {
            return true;
        }

        Set<String> permissions = chucVuDAO.getPermissionIdsByRole(normalizeRole(user.getMaChucVu()));
        return permissions.contains(PERMISSION_ALL) || permissions.contains(permissionId.trim());
    }

    public static String getRoleName(NguoiDung user) {
        if (user == null) {
            return "Khong xac dinh";
        }

        ChucVu role = chucVuDAO.getRoleById(user.getMaChucVu());

        if (role != null && role.getTenChucVu() != null) {
            return role.getTenChucVu().trim();
        }

        return "Khong xac dinh";
    }

    private static String normalizeRole(String role) {
        if (role == null) {
            return "";
        }

        return role.trim();
    }
}
