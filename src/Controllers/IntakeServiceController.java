package Controllers;

import DAO.HieuXeDAO;
import MODEL.HieuXe;
import MODEL.TiepNhanXe;
import Service.TiepNhanXeService;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import DAO.KhachHangDAO;
import MODEL.KhachHang;
import javafx.scene.control.TextInputDialog;

public class IntakeServiceController implements Initializable, Refreshable {

    @FXML private TextField txtMaTiepNhanXe;
    @FXML private TextField txtMaKhachHang;
    @FXML private TextField txtBienSoXe;
    @FXML private ComboBox<String> cbbMaHieuXe;
    @FXML private DatePicker dpNgayTiepNhan;
    @FXML private TextField txtTienNo;

    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;
    @FXML private Button btnSearch;
    @FXML private Button btnRefresh;

    @FXML private TextField txtSearch;

    @FXML private TableView<TiepNhanXe> tblTiepNhanXe;
    @FXML private TableColumn<TiepNhanXe, String> colMaTiepNhanXe;
    @FXML private TableColumn<TiepNhanXe, String> colMaKhachHang;
    @FXML private TableColumn<TiepNhanXe, String> colBienSoXe;
    @FXML private TableColumn<TiepNhanXe, String> colMaHieuXe;
    @FXML private TableColumn<TiepNhanXe, Date> colNgayTiepNhan;
    @FXML private TableColumn<TiepNhanXe, Double> colTienNo;

    private final TiepNhanXeService tiepNhanXeService = new TiepNhanXeService();
    private final HieuXeDAO hieuXeDAO = new HieuXeDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        loadHieuXeComboBox();
        loadTableData();
        setupEvents();
    }

    @Override
    public void refreshData() {
        loadHieuXeComboBox();
        loadTableData();
    }

    private void setupTableColumns() {
        colMaTiepNhanXe.setCellValueFactory(new PropertyValueFactory<>("maTiepNhanXe"));
        colMaKhachHang.setCellValueFactory(new PropertyValueFactory<>("maKhachHang"));
        colBienSoXe.setCellValueFactory(new PropertyValueFactory<>("bienSoXe"));
        colMaHieuXe.setCellValueFactory(new PropertyValueFactory<>("maHieuXe"));
        colNgayTiepNhan.setCellValueFactory(new PropertyValueFactory<>("ngayTiepNhan"));
        colTienNo.setCellValueFactory(new PropertyValueFactory<>("tienNo"));
    }

    private void setupEvents() {
        btnAdd.setOnAction(e -> handleAdd());
        btnUpdate.setOnAction(e -> handleUpdate());
        btnDelete.setOnAction(e -> handleDelete());
        btnClear.setOnAction(e -> clearForm());
        btnRefresh.setOnAction(e -> loadTableData());
        btnSearch.setOnAction(e -> handleSearch());

        tblTiepNhanXe.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, selectedItem) -> {
                if (selectedItem != null) {
                    fillForm(selectedItem);
                }
            }
        );
    }

    private void loadTableData() {
        try {
            List<TiepNhanXe> list = tiepNhanXeService.getAll();
            ObservableList<TiepNhanXe> data = FXCollections.observableArrayList(list);
            tblTiepNhanXe.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể load dữ liệu tiếp nhận xe!");
        }
    }

    private void loadHieuXeComboBox() {
        try {
            List<HieuXe> list = hieuXeDAO.getAll();

            cbbMaHieuXe.getItems().clear();

            for (HieuXe hx : list) {
                cbbMaHieuXe.getItems().add(hx.getMaHieuXe());
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể load danh sách hiệu xe!");
        }
    }

    private void handleAdd() {
    TiepNhanXe tnx = getFormData();

    if (tnx == null) {
        return;
    }

    KhachHang khachHangMoi = null;

    KhachHang khachHangDaCo = khachHangDAO.getById(tnx.getMaKhachHang());

    if (khachHangDaCo == null) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Khách hàng mới");
        confirm.setHeaderText(null);
        confirm.setContentText(
                "Mã khách hàng " + tnx.getMaKhachHang() + " chưa tồn tại.\n"
                + "Bạn có muốn tạo khách hàng mới không?"
        );

        Optional<ButtonType> option = confirm.showAndWait();

        if (option.isEmpty() || option.get() != ButtonType.OK) {
            return;
        }

        khachHangMoi = inputNewCustomer(tnx.getMaKhachHang());

        if (khachHangMoi == null) {
            return;
        }
    }

    boolean result = tiepNhanXeService.addWithCustomer(tnx, khachHangMoi);

    if (result) {
        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm tiếp nhận xe thành công!");
        loadTableData();
        clearForm();
    } else {
        showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm. Kiểm tra mã tiếp nhận, khách hàng, hiệu xe hoặc giới hạn tiếp nhận!");
    }
}

    private void handleUpdate() {
        TiepNhanXe selected = tblTiepNhanXe.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn dòng cần sửa!");
            return;
        }

        TiepNhanXe tnx = getFormData();

        if (tnx == null) {
            return;
        }

        boolean result = tiepNhanXeService.update(tnx);

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật tiếp nhận xe thành công!");
            loadTableData();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật dữ liệu!");
        }
    }

    private void handleDelete() {
        TiepNhanXe selected = tblTiepNhanXe.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn dòng cần xóa!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc muốn xóa tiếp nhận xe: " + selected.getMaTiepNhanXe() + "?");

        Optional<ButtonType> option = confirm.showAndWait();

        if (option.isPresent() && option.get() == ButtonType.OK) {
            boolean result = tiepNhanXeService.delete(selected.getMaTiepNhanXe());

            if (result) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa tiếp nhận xe thành công!");
                loadTableData();
                clearForm();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa. Dữ liệu có thể đang được tham chiếu ở bảng khác!");
            }
        }
    }

    private void handleSearch() {
        String keyword = txtSearch.getText();

        try {
            List<TiepNhanXe> list = tiepNhanXeService.search(keyword);
            ObservableList<TiepNhanXe> data = FXCollections.observableArrayList(list);
            tblTiepNhanXe.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tìm kiếm dữ liệu!");
        }
    }

    private TiepNhanXe getFormData() {
        String maTiepNhanXe = txtMaTiepNhanXe.getText().trim();
        String maKhachHang = txtMaKhachHang.getText().trim();
        String bienSoXe = txtBienSoXe.getText().trim();
        String maHieuXe = cbbMaHieuXe.getValue();
        LocalDate ngayTiepNhanLocal = dpNgayTiepNhan.getValue();
        String tienNoText = txtTienNo.getText().trim();

        if (maTiepNhanXe.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Mã tiếp nhận xe không được rỗng!");
            return null;
        }

        if (maKhachHang.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Mã khách hàng không được rỗng!");
            return null;
        }

        if (bienSoXe.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Biển số xe không được rỗng!");
            return null;
        }

        if (maHieuXe == null || maHieuXe.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Vui lòng chọn hiệu xe!");
            return null;
        }

        if (ngayTiepNhanLocal == null) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Ngày tiếp nhận không được rỗng!");
            return null;
        }

        double tienNo;

        try {
            tienNo = Double.parseDouble(tienNoText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Tiền nợ phải là số!");
            return null;
        }

        if (tienNo < 0) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Tiền nợ không được âm!");
            return null;
        }

        Date ngayTiepNhan = Date.valueOf(ngayTiepNhanLocal);

        return new TiepNhanXe(
            maTiepNhanXe,
            maKhachHang,
            bienSoXe,
            maHieuXe,
            ngayTiepNhan,
            tienNo
        );
    }

    private void fillForm(TiepNhanXe tnx) {
        txtMaTiepNhanXe.setText(tnx.getMaTiepNhanXe());
        txtMaKhachHang.setText(tnx.getMaKhachHang());
        txtBienSoXe.setText(tnx.getBienSoXe());
        cbbMaHieuXe.setValue(tnx.getMaHieuXe());

        if (tnx.getNgayTiepNhan() != null) {
            dpNgayTiepNhan.setValue(tnx.getNgayTiepNhan().toLocalDate());
        } else {
            dpNgayTiepNhan.setValue(null);
        }

        txtTienNo.setText(String.valueOf(tnx.getTienNo()));
    }

    private void clearForm() {
        txtMaTiepNhanXe.clear();
        txtMaKhachHang.clear();
        txtBienSoXe.clear();
        cbbMaHieuXe.setValue(null);
        dpNgayTiepNhan.setValue(null);
        txtTienNo.clear();
        txtSearch.clear();

        tblTiepNhanXe.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private KhachHang inputNewCustomer(String maKhachHang) {
    String tenKhachHang = askText(
            "Tạo khách hàng mới",
            "Nhập tên khách hàng:"
    );

    if (tenKhachHang == null || tenKhachHang.trim().isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Tên khách hàng không được rỗng!");
        return null;
    }

    String soDienThoai = askText(
            "Tạo khách hàng mới",
            "Nhập số điện thoại khách hàng:"
    );

    if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Số điện thoại không được rỗng!");
        return null;
    }

    String diaChi = askText(
            "Tạo khách hàng mới",
            "Nhập địa chỉ khách hàng:"
    );

    if (diaChi == null || diaChi.trim().isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Địa chỉ không được rỗng!");
        return null;
    }

    return new KhachHang(
            maKhachHang,
            tenKhachHang.trim(),
            diaChi.trim(),
            soDienThoai.trim()
    );
}

private String askText(String title, String content) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle(title);
    dialog.setHeaderText(null);
    dialog.setContentText(content);

    Optional<String> result = dialog.showAndWait();

    if (result.isEmpty()) {
        return null;
    }

    return result.get();
}
}
