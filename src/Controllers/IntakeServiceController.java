package Controllers;

import DAO.HieuXeDAO;
import DAO.KhachHangDAO;
import MODEL.HieuXe;
import MODEL.KhachHang;
import MODEL.TiepNhanXe;
import Service.DataRefreshService;
import Service.TiepNhanXeService;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

public class IntakeServiceController implements Initializable, Refreshable {

    @FXML private TextField txtMaTiepNhanXe;
    @FXML private TextField txtMaKhachHang;
    @FXML private TextField txtBienSoXe;
    @FXML private ComboBox<String> cbbMaHieuXe;
    @FXML private DatePicker dpNgayTiepNhan;

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
    private final Map<String, String> brandNameToId = new HashMap<>();
    private final Map<String, String> brandIdToName = new HashMap<>();
    private boolean refreshing = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        restrictDatePickerToToday(dpNgayTiepNhan);
        loadHieuXeComboBox();
        loadTableData();
        setupEvents();
    }

    @Override
    public void refreshData() {
        refreshDataAsync();
    }

    private void setupTableColumns() {
        colMaTiepNhanXe.setCellValueFactory(new PropertyValueFactory<>("maTiepNhanXe"));
        colMaKhachHang.setCellValueFactory(new PropertyValueFactory<>("maKhachHang"));
        colBienSoXe.setCellValueFactory(new PropertyValueFactory<>("bienSoXe"));
        colMaHieuXe.setCellValueFactory(cellData -> new SimpleStringProperty(
                brandIdToName.getOrDefault(
                        safeTrim(cellData.getValue().getMaHieuXe()),
                        safeTrim(cellData.getValue().getMaHieuXe())
                )
        ));
        colNgayTiepNhan.setCellValueFactory(new PropertyValueFactory<>("ngayTiepNhan"));
        colTienNo.setCellValueFactory(new PropertyValueFactory<>("tienNo"));
    }

    private void setupEvents() {
        btnAdd.setOnAction(e -> handleAdd());
        btnUpdate.setOnAction(e -> handleUpdate());
        btnDelete.setOnAction(e -> handleDelete());
        btnClear.setOnAction(e -> clearForm());
        btnRefresh.setOnAction(e -> reloadAll());
        btnSearch.setOnAction(e -> handleSearch());

        tblTiepNhanXe.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, selectedItem) -> {
                    if (selectedItem != null) {
                        fillForm(selectedItem);
                    }
                }
        );
    }

    private void reloadAll() {
        loadHieuXeComboBox();
        loadTableData();
    }

    private void loadTableData() {
        try {
            List<TiepNhanXe> list = tiepNhanXeService.getAll();
            ObservableList<TiepNhanXe> data = FXCollections.observableArrayList(list);
            tblTiepNhanXe.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải dữ liệu tiếp nhận xe.");
        }
    }

    private void loadHieuXeComboBox() {
        try {
            applyBrandComboBox(hieuXeDAO.getAll());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải danh sách hiệu xe.");
        }
    }

    private void refreshDataAsync() {
        if (refreshing) {
            return;
        }

        refreshing = true;
        String selectedBrandId = getSelectedBrandId();

        Task<IntakeRefreshData> task = new Task<>() {
            @Override
            protected IntakeRefreshData call() {
                return new IntakeRefreshData(hieuXeDAO.getAll(), tiepNhanXeService.getAll());
            }
        };

        task.setOnSucceeded(e -> {
            refreshing = false;
            IntakeRefreshData data = task.getValue();
            applyBrandComboBox(data.brands());
            cbbMaHieuXe.setValue(brandIdToName.get(selectedBrandId));
            tblTiepNhanXe.setItems(FXCollections.observableArrayList(data.intakes()));
        });

        task.setOnFailed(e -> refreshing = false);

        Thread thread = new Thread(task, "intake-refresh");
        thread.setDaemon(true);
        thread.start();
    }

    private void applyBrandComboBox(List<HieuXe> list) {
        cbbMaHieuXe.getItems().clear();
        brandNameToId.clear();
        brandIdToName.clear();

        for (HieuXe hx : list) {
            String id = safeTrim(hx.getMaHieuXe());
            String name = safeTrim(hx.getTenHieuXe());
            brandNameToId.put(name, id);
            brandIdToName.put(id, name);
            cbbMaHieuXe.getItems().add(name);
        }
    }

    private void handleAdd() {
        TiepNhanXe tnx = getFormData(0);

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
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Tạo hồ sơ tiếp nhận xe thành công.");
            loadTableData();
            clearForm();
            markIntakeDataChanged();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tạo hồ sơ. Kiểm tra mã hồ sơ, biển số, khách hàng, hiệu xe hoặc giới hạn tiếp nhận trong ngày.");
        }
    }

    private void handleUpdate() {
        TiepNhanXe selected = tblTiepNhanXe.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn hồ sơ cần sửa.");
            return;
        }

        TiepNhanXe tnx = getFormData(selected.getTienNo());

        if (tnx == null) {
            return;
        }

        boolean result = tiepNhanXeService.update(tnx);

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật hồ sơ tiếp nhận xe thành công.");
            loadTableData();
            clearForm();
            markIntakeDataChanged();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật. Biển số có thể đã thuộc hồ sơ khác hoặc dữ liệu đang được tham chiếu.");
        }
    }

    private void handleDelete() {
        TiepNhanXe selected = tblTiepNhanXe.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn hồ sơ cần xóa.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc muốn xóa hồ sơ tiếp nhận xe " + selected.getMaTiepNhanXe() + "?");

        Optional<ButtonType> option = confirm.showAndWait();

        if (option.isPresent() && option.get() == ButtonType.OK) {
            boolean result = tiepNhanXeService.delete(selected.getMaTiepNhanXe());

            if (result) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa hồ sơ tiếp nhận xe thành công.");
                loadTableData();
                clearForm();
                markIntakeDataChanged();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa vì hồ sơ đang được phiếu sửa chữa hoặc phiếu thu tham chiếu.");
            }
        }
    }

    private void handleSearch() {
        try {
            List<TiepNhanXe> list = tiepNhanXeService.search(txtSearch.getText());
            tblTiepNhanXe.setItems(FXCollections.observableArrayList(list));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tìm kiếm dữ liệu.");
        }
    }

    private TiepNhanXe getFormData(double tienNo) {
        String maTiepNhanXe = safeTrim(txtMaTiepNhanXe.getText());
        String maKhachHang = safeTrim(txtMaKhachHang.getText());
        String bienSoXe = normalizeLicensePlate(txtBienSoXe.getText());
        String maHieuXe = brandNameToId.get(cbbMaHieuXe.getValue());
        LocalDate ngayTiepNhanLocal = dpNgayTiepNhan.getValue();

        if (maTiepNhanXe.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Mã hồ sơ không được rỗng.");
            return null;
        }

        if (maKhachHang.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Mã khách hàng không được rỗng.");
            return null;
        }

        if (bienSoXe.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Biển số xe không được rỗng.");
            return null;
        }

        if (bienSoXe.length() > 10) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Biển số xe tối đa 10 ký tự sau khi bỏ khoảng trắng và dấu gạch.");
            return null;
        }

        if (maHieuXe == null || maHieuXe.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Vui lòng chọn hiệu xe.");
            return null;
        }

        if (ngayTiepNhanLocal == null) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Ngày tiếp nhận không được rỗng.");
            return null;
        }

        if (!isToday(ngayTiepNhanLocal)) {
            showAlert(Alert.AlertType.WARNING, "Sai ngày", "Ngày tiếp nhận chỉ được chọn ngày hôm nay.");
            return null;
        }

        return new TiepNhanXe(
                maTiepNhanXe,
                maKhachHang,
                bienSoXe,
                maHieuXe,
                Date.valueOf(ngayTiepNhanLocal),
                tienNo
        );
    }

    private void fillForm(TiepNhanXe tnx) {
        txtMaTiepNhanXe.setDisable(true);
        txtMaTiepNhanXe.setText(safeTrim(tnx.getMaTiepNhanXe()));
        txtMaKhachHang.setText(safeTrim(tnx.getMaKhachHang()));
        txtBienSoXe.setText(safeTrim(tnx.getBienSoXe()));

        String brandId = safeTrim(tnx.getMaHieuXe());
        cbbMaHieuXe.setValue(brandIdToName.getOrDefault(brandId, brandId));

        if (tnx.getNgayTiepNhan() != null) {
            dpNgayTiepNhan.setValue(tnx.getNgayTiepNhan().toLocalDate());
        } else {
            dpNgayTiepNhan.setValue(null);
        }
    }

    private void clearForm() {
        txtMaTiepNhanXe.setDisable(false);
        txtMaTiepNhanXe.clear();
        txtMaKhachHang.clear();
        txtBienSoXe.clear();
        cbbMaHieuXe.setValue(null);
        dpNgayTiepNhan.setValue(LocalDate.now());
        txtSearch.clear();
        tblTiepNhanXe.getSelectionModel().clearSelection();
    }

    private void restrictDatePickerToToday(DatePicker datePicker) {
        datePicker.setEditable(false);
        datePicker.setValue(LocalDate.now());
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || !isToday(date));
            }
        });
    }

    private boolean isToday(LocalDate date) {
        return LocalDate.now().equals(date);
    }

    private String normalizeLicensePlate(String value) {
        return safeTrim(value).replace("-", "").replace(" ", "").toUpperCase();
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private KhachHang inputNewCustomer(String maKhachHang) {
        String tenKhachHang = askText("Tạo khách hàng mới", "Nhập tên khách hàng:");

        if (tenKhachHang == null || tenKhachHang.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Tên khách hàng không được rỗng.");
            return null;
        }

        String soDienThoai = askText("Tạo khách hàng mới", "Nhập số điện thoại khách hàng:");

        if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Số điện thoại không được rỗng.");
            return null;
        }

        String diaChi = askText("Tạo khách hàng mới", "Nhập địa chỉ khách hàng:");

        if (diaChi == null || diaChi.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Địa chỉ không được rỗng.");
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
        return result.orElse(null);
    }

    private void markIntakeDataChanged() {
        DataRefreshService.markDirty(
                DataRefreshService.DASHBOARD,
                DataRefreshService.REPAIR,
                DataRefreshService.BILLING,
                DataRefreshService.LOOKUP,
                DataRefreshService.REPORTS,
                DataRefreshService.PARTS
        );
    }

    private String getSelectedBrandId() {
        String value = cbbMaHieuXe.getValue();
        return value == null ? null : brandNameToId.getOrDefault(value, value).trim();
    }

    private record IntakeRefreshData(
            List<HieuXe> brands,
            List<TiepNhanXe> intakes
    ) {
    }
}
