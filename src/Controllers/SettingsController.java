package Controllers;

import DAO.NguoiDungDAO;
import MODEL.HieuXe;
import MODEL.NguoiDung;
import MODEL.NhaCungCap;
import MODEL.TienCong;
import Service.AuthService;
import Service.AuthorizationService;
import Service.DataRefreshService;
import Service.HieuXeService;
import Service.NhaCungCapService;
import Service.TienCongService;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class SettingsController implements Initializable {

    @FXML private TextField txtSettingMaNguoiDung;
    @FXML private TextField txtSettingUsername;
    @FXML private TextField txtSettingRole;
    @FXML private Button btnUpdateProfile;
    @FXML private Button btnRefreshProfile;
    @FXML private Button btnLogout;
    @FXML private TabPane settingsTabPane;
    @FXML private Tab tabUsers;
    @FXML private Tab tabLabor;
    @FXML private Tab tabSuppliers;
    @FXML private Tab tabBrands;

    @FXML private PasswordField txtOldPassword;
    @FXML private PasswordField txtNewPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private Button btnChangePassword;
    @FXML private Button btnResetPasswordForm;

    @FXML private TextField txtUserId;
    @FXML private TextField txtUserName;
    @FXML private PasswordField txtUserPassword;
    @FXML private ComboBox<String> cbbUserRole;
    @FXML private Button btnAddUser;
    @FXML private Button btnUpdateUser;
    @FXML private Button btnDeleteUser;
    @FXML private Button btnClearUser;
    @FXML private TableView<NguoiDung> tblUsers;
    @FXML private TableColumn<NguoiDung, String> colUserId;
    @FXML private TableColumn<NguoiDung, String> colUserName;
    @FXML private TableColumn<NguoiDung, String> colUserRole;

    @FXML private TextField txtMaTienCong;
    @FXML private TextField txtNoiDungTienCong;
    @FXML private TextField txtSoTienCong;
    @FXML private Button btnAddLabor;
    @FXML private Button btnUpdateLabor;
    @FXML private Button btnDeleteLabor;
    @FXML private Button btnClearLabor;
    @FXML private TableView<TienCong> tblLabor;
    @FXML private TableColumn<TienCong, String> colLaborId;
    @FXML private TableColumn<TienCong, String> colLaborContent;
    @FXML private TableColumn<TienCong, Double> colLaborAmount;

    @FXML private TextField txtMaNhaCungCap;
    @FXML private TextField txtTenNhaCungCap;
    @FXML private TextField txtSdtNhaCungCap;
    @FXML private TextField txtEmailNhaCungCap;
    @FXML private Button btnAddSupplier;
    @FXML private Button btnUpdateSupplier;
    @FXML private Button btnDeleteSupplier;
    @FXML private Button btnClearSupplier;
    @FXML private TableView<NhaCungCap> tblSupplier;
    @FXML private TableColumn<NhaCungCap, String> colSupplierId;
    @FXML private TableColumn<NhaCungCap, String> colSupplierName;
    @FXML private TableColumn<NhaCungCap, String> colSupplierPhone;
    @FXML private TableColumn<NhaCungCap, String> colSupplierEmail;

    @FXML private TextField txtMaHieuXe;
    @FXML private TextField txtTenHieuXe;
    @FXML private Button btnAddBrand;
    @FXML private Button btnUpdateBrand;
    @FXML private Button btnDeleteBrand;
    @FXML private Button btnClearBrand;
    @FXML private TableView<HieuXe> tblBrand;
    @FXML private TableColumn<HieuXe, String> colBrandId;
    @FXML private TableColumn<HieuXe, String> colBrandName;

    private final NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();
    private final TienCongService tienCongService = new TienCongService();
    private final NhaCungCapService nhaCungCapService = new NhaCungCapService();
    private final HieuXeService hieuXeService = new HieuXeService();

    private NguoiDung currentUser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentUser = AuthService.currentUser;

        setupTables();
        setupEvents();
        applyRolePermissions();

        loadProfile();
        if (AuthorizationService.isAdmin(currentUser)) {
            loadUserTable();
            loadLaborTable();
            loadSupplierTable();
            loadBrandTable();
        }
    }

    private void setupTables() {
        colLaborId.setCellValueFactory(new PropertyValueFactory<>("maTienCong"));
        colLaborContent.setCellValueFactory(new PropertyValueFactory<>("noiDungTienCong"));
        colLaborAmount.setCellValueFactory(new PropertyValueFactory<>("soTienCong"));

        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("maNhaCungCap"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("tenNhaCungCap"));
        colSupplierPhone.setCellValueFactory(new PropertyValueFactory<>("soDienThoaiNhaCungCap"));
        colSupplierEmail.setCellValueFactory(new PropertyValueFactory<>("emailNhaCungCap"));

        colBrandId.setCellValueFactory(new PropertyValueFactory<>("maHieuXe"));
        colBrandName.setCellValueFactory(new PropertyValueFactory<>("tenHieuXe"));

        colUserId.setCellValueFactory(new PropertyValueFactory<>("maNguoiDung"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("tenTaiKhoan"));
        colUserRole.setCellValueFactory(cellData ->
                new SimpleStringProperty(AuthorizationService.getRoleName(cellData.getValue()))
        );
    }

    private void setupEvents() {
        btnUpdateProfile.setOnAction(e -> handleUpdateProfile());
        btnRefreshProfile.setOnAction(e -> loadProfile());
        btnLogout.setOnAction(e -> handleLogout(e));
        btnChangePassword.setOnAction(e -> handleChangePassword());
        btnResetPasswordForm.setOnAction(e -> clearPasswordForm());

        cbbUserRole.getItems().setAll("Quản trị viên", "Nhân viên");
        btnAddUser.setOnAction(e -> handleAddUser());
        btnUpdateUser.setOnAction(e -> handleUpdateUser());
        btnDeleteUser.setOnAction(e -> handleDeleteUser());
        btnClearUser.setOnAction(e -> clearUserForm());

        btnAddLabor.setOnAction(e -> handleAddLabor());
        btnUpdateLabor.setOnAction(e -> handleUpdateLabor());
        btnDeleteLabor.setOnAction(e -> handleDeleteLabor());
        btnClearLabor.setOnAction(e -> clearLaborForm());

        btnAddSupplier.setOnAction(e -> handleAddSupplier());
        btnUpdateSupplier.setOnAction(e -> handleUpdateSupplier());
        btnDeleteSupplier.setOnAction(e -> handleDeleteSupplier());
        btnClearSupplier.setOnAction(e -> clearSupplierForm());

        btnAddBrand.setOnAction(e -> handleAddBrand());
        btnUpdateBrand.setOnAction(e -> handleUpdateBrand());
        btnDeleteBrand.setOnAction(e -> handleDeleteBrand());
        btnClearBrand.setOnAction(e -> clearBrandForm());

        tblLabor.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            if (selected != null) fillLaborForm(selected);
        });

        tblSupplier.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            if (selected != null) fillSupplierForm(selected);
        });

        tblBrand.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            if (selected != null) fillBrandForm(selected);
        });

        tblUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            if (selected != null) fillUserForm(selected);
        });
    }

    private void loadProfile() {
        currentUser = AuthService.currentUser;

        if (currentUser == null) {
            txtSettingMaNguoiDung.clear();
            txtSettingUsername.clear();
            txtSettingRole.clear();
            return;
        }

        txtSettingMaNguoiDung.setText(currentUser.getMaNguoiDung());
        txtSettingUsername.setText(currentUser.getTenTaiKhoan());
        txtSettingRole.setText(AuthorizationService.getRoleName(currentUser));

        txtSettingMaNguoiDung.setDisable(true);
        txtSettingRole.setDisable(true);

        clearPasswordForm();
    }

    private void handleUpdateProfile() {
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Chưa có người dùng đăng nhập!");
            return;
        }

        String username = txtSettingUsername.getText().trim();

        if (username.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Tên tài khoản không được rỗng!");
            return;
        }

        currentUser.setTenTaiKhoan(username);

        boolean result = nguoiDungDAO.update(currentUser);

        if (result) {
            AuthService.currentUser = currentUser;
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật profile thành công!");
            loadProfile();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật profile!");
        }
    }

    private void applyRolePermissions() {
        boolean admin = AuthorizationService.isAdmin(currentUser);

        if (settingsTabPane != null && !admin) {
            settingsTabPane.getTabs().remove(tabUsers);
            settingsTabPane.getTabs().remove(tabLabor);
            settingsTabPane.getTabs().remove(tabSuppliers);
            settingsTabPane.getTabs().remove(tabBrands);
        }

        setAdminControlsEnabled(admin,
                btnAddLabor, btnUpdateLabor, btnDeleteLabor, btnClearLabor,
                btnAddUser, btnUpdateUser, btnDeleteUser, btnClearUser,
                btnAddSupplier, btnUpdateSupplier, btnDeleteSupplier, btnClearSupplier,
                btnAddBrand, btnUpdateBrand, btnDeleteBrand, btnClearBrand,
                txtMaTienCong, txtNoiDungTienCong, txtSoTienCong,
                txtUserId, txtUserName, txtUserPassword, cbbUserRole,
                txtMaNhaCungCap, txtTenNhaCungCap, txtSdtNhaCungCap, txtEmailNhaCungCap,
                txtMaHieuXe, txtTenHieuXe
        );
    }

    private void setAdminControlsEnabled(boolean enabled, javafx.scene.Node... nodes) {
        for (javafx.scene.Node node : nodes) {
            if (node != null) {
                node.setDisable(!enabled);
            }
        }
    }

    private boolean requireAdmin() {
        if (AuthorizationService.isAdmin(currentUser)) {
            return true;
        }

        showAlert(Alert.AlertType.WARNING, "Không có quyền", "Chỉ tài khoản quản trị viên mới được thay đổi cấu hình hệ thống.");
        return false;
    }

    private void handleChangePassword() {
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Chưa có người dùng đăng nhập!");
            return;
        }

        String oldPassword = txtOldPassword.getText();
        String newPassword = txtNewPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Vui lòng nhập đủ thông tin mật khẩu!");
            return;
        }

        if (!oldPassword.equals(currentUser.getMatKhau())) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Mật khẩu cũ không đúng!");
            return;
        }

        if (newPassword.length() < 6) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Mật khẩu mới phải có ít nhất 6 ký tự!");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Mật khẩu xác nhận không khớp!");
            return;
        }

        currentUser.setMatKhau(newPassword);

        boolean result = nguoiDungDAO.update(currentUser);

        if (result) {
            AuthService.currentUser = currentUser;
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đổi mật khẩu thành công!");
            clearPasswordForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể đổi mật khẩu!");
        }
    }

    private void handleLogout(ActionEvent event) {
        AuthService.logOut();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Views/LognReg.fxml"));
            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể đăng xuất!");
        }
    }

    private void clearPasswordForm() {
        txtOldPassword.clear();
        txtNewPassword.clear();
        txtConfirmPassword.clear();
    }

    private void loadUserTable() {
        tblUsers.setItems(FXCollections.observableArrayList(nguoiDungDAO.getAll()));
    }

    private void handleAddUser() {
        if (!requireAdmin()) return;

        NguoiDung user = getUserForm(true);

        if (user == null) return;

        if (nguoiDungDAO.checkTaiKhoanTonTai(user.getTenTaiKhoan())) {
            showAlert(Alert.AlertType.WARNING, "Trùng tài khoản", "Tên tài khoản này đã tồn tại!");
            return;
        }

        boolean result = nguoiDungDAO.insert(user);

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm tài khoản thành công!");
            loadUserTable();
            clearUserForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm tài khoản!");
        }
    }

    private void handleUpdateUser() {
        if (!requireAdmin()) return;

        NguoiDung selected = tblUsers.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn tài khoản cần sửa!");
            return;
        }

        NguoiDung user = getUserForm(false);

        if (user == null) return;

        if (isUsernameUsedByAnotherUser(user.getTenTaiKhoan(), user.getMaNguoiDung())) {
            showAlert(Alert.AlertType.WARNING, "Trùng tài khoản", "Tên tài khoản này đã tồn tại!");
            return;
        }

        if (user.getMatKhau().trim().isEmpty()) {
            user.setMatKhau(selected.getMatKhau());
        }

        boolean result = nguoiDungDAO.update(user);

        if (result) {
            if (currentUser != null && currentUser.getMaNguoiDung().trim().equals(user.getMaNguoiDung().trim())) {
                AuthService.currentUser = user;
                currentUser = user;
                loadProfile();
            }

            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật tài khoản thành công!");
            loadUserTable();
            clearUserForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật tài khoản!");
        }
    }

    private void handleDeleteUser() {
        if (!requireAdmin()) return;

        NguoiDung selected = tblUsers.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn tài khoản cần xóa!");
            return;
        }

        if (currentUser != null
                && currentUser.getMaNguoiDung().trim().equals(selected.getMaNguoiDung().trim())) {
            showAlert(Alert.AlertType.WARNING, "Không thể xóa", "Không thể xóa tài khoản đang đăng nhập!");
            return;
        }

        if (!confirmDelete("Bạn có chắc muốn xóa tài khoản " + selected.getTenTaiKhoan() + "?")) {
            return;
        }

        boolean result = nguoiDungDAO.delete(selected.getMaNguoiDung());

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa tài khoản thành công!");
            loadUserTable();
            clearUserForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa tài khoản!");
        }
    }

    private NguoiDung getUserForm(boolean requirePassword) {
        String id = txtUserId.getText().trim();
        String username = txtUserName.getText().trim();
        String password = txtUserPassword.getText();
        String role = toRoleId(cbbUserRole.getValue());

        if (id.isEmpty()) {
            id = nguoiDungDAO.getMaNguoiDungTiepTheo();
        }

        if (username.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Tên tài khoản không được rỗng!");
            return null;
        }

        if (requirePassword && password.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Mật khẩu không được rỗng!");
            return null;
        }

        if (!password.trim().isEmpty() && password.length() < 6) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Mật khẩu phải có ít nhất 6 ký tự!");
            return null;
        }

        if (role == null) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Vui lòng chọn vai trò!");
            return null;
        }

        return new NguoiDung(id, username, password, role);
    }

    private boolean isUsernameUsedByAnotherUser(String username, String userId) {
        for (NguoiDung user : nguoiDungDAO.getAll()) {
            if (user.getTenTaiKhoan().trim().equalsIgnoreCase(username.trim())
                    && !user.getMaNguoiDung().trim().equalsIgnoreCase(userId.trim())) {
                return true;
            }
        }

        return false;
    }

    private void fillUserForm(NguoiDung user) {
        txtUserId.setText(user.getMaNguoiDung());
        txtUserId.setDisable(true);
        txtUserName.setText(user.getTenTaiKhoan());
        txtUserPassword.clear();
        cbbUserRole.setValue(toRoleName(user.getMaChucVu()));
    }

    private void clearUserForm() {
        txtUserId.setDisable(false);
        txtUserId.clear();
        txtUserName.clear();
        txtUserPassword.clear();
        cbbUserRole.setValue(null);
        tblUsers.getSelectionModel().clearSelection();
    }

    private String toRoleId(String roleName) {
        if ("Quản trị viên".equals(roleName)) {
            return AuthorizationService.ROLE_ADMIN;
        }

        if ("Nhân viên".equals(roleName)) {
            return AuthorizationService.ROLE_STAFF;
        }

        return null;
    }

    private String toRoleName(String roleId) {
        if (AuthorizationService.ROLE_ADMIN.equals(roleId == null ? "" : roleId.trim())) {
            return "Quản trị viên";
        }

        if (AuthorizationService.ROLE_STAFF.equals(roleId == null ? "" : roleId.trim())) {
            return "Nhân viên";
        }

        return null;
    }

    private void loadLaborTable() {
        tblLabor.setItems(FXCollections.observableArrayList(tienCongService.getAll()));
    }

    private void handleAddLabor() {
        if (!requireAdmin()) return;

        TienCong tc = getLaborForm();

        if (tc == null) return;

        boolean result = tienCongService.add(tc);

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm tiền công thành công!");
            loadLaborTable();
            clearLaborForm();
            markSettingsDataChanged();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm. Có thể trùng mã hoặc vượt số loại tiền công tối đa!");
        }
    }

    private void handleUpdateLabor() {
        if (!requireAdmin()) return;

        if (tblLabor.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn tiền công cần sửa!");
            return;
        }

        TienCong tc = getLaborForm();

        if (tc == null) return;

        boolean result = tienCongService.update(tc);

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật tiền công thành công!");
            loadLaborTable();
            clearLaborForm();
            markSettingsDataChanged();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật tiền công!");
        }
    }

    private void handleDeleteLabor() {
        if (!requireAdmin()) return;

        TienCong selected = tblLabor.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn tiền công cần xóa!");
            return;
        }

        if (!confirmDelete("Bạn có chắc muốn xóa tiền công " + selected.getMaTienCong() + "?")) {
            return;
        }

        boolean result = tienCongService.delete(selected.getMaTienCong());

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa tiền công thành công!");
            loadLaborTable();
            clearLaborForm();
            markSettingsDataChanged();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa. Tiền công có thể đang được dùng trong phiếu sửa chữa!");
        }
    }

    private TienCong getLaborForm() {
        String ma = txtMaTienCong.getText().trim();
        String noiDung = txtNoiDungTienCong.getText().trim();

        if (ma.isEmpty() || noiDung.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Mã và nội dung tiền công không được rỗng!");
            return null;
        }

        try {
            double soTien = Double.parseDouble(txtSoTienCong.getText().trim());

            if (soTien < 0) {
                showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Số tiền công không được âm!");
                return null;
            }

            return new TienCong(ma, soTien, noiDung);

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Số tiền công phải là số!");
            return null;
        }
    }

    private void fillLaborForm(TienCong tc) {
        txtMaTienCong.setText(tc.getMaTienCong());
        txtMaTienCong.setDisable(true);
        txtNoiDungTienCong.setText(tc.getNoiDungTienCong());
        txtSoTienCong.setText(String.valueOf(tc.getSoTienCong()));
    }

    private void clearLaborForm() {
        txtMaTienCong.setDisable(false);
        txtMaTienCong.clear();
        txtNoiDungTienCong.clear();
        txtSoTienCong.clear();
        tblLabor.getSelectionModel().clearSelection();
    }

    private void loadSupplierTable() {
        tblSupplier.setItems(FXCollections.observableArrayList(nhaCungCapService.getAll()));
    }

    private void handleAddSupplier() {
        if (!requireAdmin()) return;

        NhaCungCap ncc = getSupplierForm();

        if (ncc == null) return;

        boolean result = nhaCungCapService.add(ncc);

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm nhà cung cấp thành công!");
            loadSupplierTable();
            clearSupplierForm();
            markSettingsDataChanged();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm nhà cung cấp. Mã có thể đã tồn tại!");
        }
    }

    private void handleUpdateSupplier() {
        if (!requireAdmin()) return;

        if (tblSupplier.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn nhà cung cấp cần sửa!");
            return;
        }

        NhaCungCap ncc = getSupplierForm();

        if (ncc == null) return;

        boolean result = nhaCungCapService.update(ncc);

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật nhà cung cấp thành công!");
            loadSupplierTable();
            clearSupplierForm();
            markSettingsDataChanged();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật nhà cung cấp!");
        }
    }

    private void handleDeleteSupplier() {
        if (!requireAdmin()) return;

        NhaCungCap selected = tblSupplier.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn nhà cung cấp cần xóa!");
            return;
        }

        if (!confirmDelete("Bạn có chắc muốn xóa nhà cung cấp " + selected.getMaNhaCungCap() + "?")) {
            return;
        }

        boolean result = nhaCungCapService.delete(selected.getMaNhaCungCap());

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa nhà cung cấp thành công!");
            loadSupplierTable();
            clearSupplierForm();
            markSettingsDataChanged();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa. Nhà cung cấp có thể đang được dùng trong phiếu nhập!");
        }
    }

    private NhaCungCap getSupplierForm() {
        String ma = txtMaNhaCungCap.getText().trim();
        String ten = txtTenNhaCungCap.getText().trim();
        String phone = txtSdtNhaCungCap.getText().trim();
        String email = txtEmailNhaCungCap.getText().trim();

        if (ma.isEmpty() || ten.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Thông tin nhà cung cấp không được rỗng!");
            return null;
        }

        return new NhaCungCap(ma, ten, phone, email);
    }

    private void fillSupplierForm(NhaCungCap ncc) {
        txtMaNhaCungCap.setText(ncc.getMaNhaCungCap());
        txtMaNhaCungCap.setDisable(true);
        txtTenNhaCungCap.setText(ncc.getTenNhaCungCap());
        txtSdtNhaCungCap.setText(ncc.getSoDienThoaiNhaCungCap());
        txtEmailNhaCungCap.setText(ncc.getEmailNhaCungCap());
    }

    private void clearSupplierForm() {
        txtMaNhaCungCap.setDisable(false);
        txtMaNhaCungCap.clear();
        txtTenNhaCungCap.clear();
        txtSdtNhaCungCap.clear();
        txtEmailNhaCungCap.clear();
        tblSupplier.getSelectionModel().clearSelection();
    }

    private void loadBrandTable() {
        tblBrand.setItems(FXCollections.observableArrayList(hieuXeService.getAll()));
    }

    private void handleAddBrand() {
        if (!requireAdmin()) return;

        HieuXe hx = getBrandForm();

        if (hx == null) return;

        boolean result = hieuXeService.add(hx);

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm hiệu xe thành công!");
            loadBrandTable();
            clearBrandForm();
            markSettingsDataChanged();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm. Có thể trùng mã hoặc vượt số hiệu xe tối đa!");
        }
    }

    private void handleUpdateBrand() {
        if (!requireAdmin()) return;

        if (tblBrand.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn hiệu xe cần sửa!");
            return;
        }

        HieuXe hx = getBrandForm();

        if (hx == null) return;

        boolean result = hieuXeService.update(hx);

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật hiệu xe thành công!");
            loadBrandTable();
            clearBrandForm();
            markSettingsDataChanged();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật hiệu xe!");
        }
    }

    private void handleDeleteBrand() {
        if (!requireAdmin()) return;

        HieuXe selected = tblBrand.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn hiệu xe cần xóa!");
            return;
        }

        if (!confirmDelete("Bạn có chắc muốn xóa hiệu xe " + selected.getMaHieuXe() + "?")) {
            return;
        }

        boolean result = hieuXeService.delete(selected.getMaHieuXe());

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa hiệu xe thành công!");
            loadBrandTable();
            clearBrandForm();
            markSettingsDataChanged();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa. Hiệu xe có thể đang được dùng trong tiếp nhận xe!");
        }
    }

    private HieuXe getBrandForm() {
        String ma = txtMaHieuXe.getText().trim();
        String ten = txtTenHieuXe.getText().trim();

        if (ma.isEmpty() || ten.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Mã hiệu xe và tên hiệu xe không được rỗng!");
            return null;
        }

        return new HieuXe(ma, ten);
    }

    private void fillBrandForm(HieuXe hx) {
        txtMaHieuXe.setText(hx.getMaHieuXe());
        txtMaHieuXe.setDisable(true);
        txtTenHieuXe.setText(hx.getTenHieuXe());
    }

    private void clearBrandForm() {
        txtMaHieuXe.setDisable(false);
        txtMaHieuXe.clear();
        txtTenHieuXe.clear();
        tblBrand.getSelectionModel().clearSelection();
    }

    private boolean confirmDelete(String message) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText(null);
        confirm.setContentText(message);

        Optional<ButtonType> option = confirm.showAndWait();

        return option.isPresent() && option.get() == ButtonType.OK;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void markSettingsDataChanged() {
        DataRefreshService.markDirty(
                DataRefreshService.INTAKE,
                DataRefreshService.REPAIR,
                DataRefreshService.PARTS,
                DataRefreshService.LOOKUP,
                DataRefreshService.REPORTS
        );
    }
}
