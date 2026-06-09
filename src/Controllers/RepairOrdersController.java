package Controllers;

import MODEL.ChiTietSuaChuaXe;
import MODEL.SuaChuaXe;
import MODEL.VatTuPhuTung;
import Service.DataRefreshService;
import Service.SuaChuaXeService;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class RepairOrdersController implements Initializable, Refreshable {

    @FXML private TextField txtMaSuaChua;
    @FXML private ComboBox<String> cbbMaTiepNhanXe;
    @FXML private DatePicker dpNgaySuaChua;
    @FXML private ComboBox<String> cbbMaVatTu;
    @FXML private TextField txtSoLuong;
    @FXML private ComboBox<String> cbbMaTienCong;
    @FXML private TextArea txtNoiDungSuaChua;

    @FXML private Button btnAddRepair;
    @FXML private Button btnUpdateRepair;
    @FXML private Button btnDeleteRepair;
    @FXML private Button btnClearRepair;

    @FXML private TextField txtSearchRepair;
    @FXML private Button btnSearchRepair;
    @FXML private Button btnRefreshRepair;

    @FXML private TableView<SuaChuaXe> tblRepairOrders;
    @FXML private TableColumn<SuaChuaXe, String> colMaSuaChua;
    @FXML private TableColumn<SuaChuaXe, String> colMaTiepNhanXe;
    @FXML private TableColumn<SuaChuaXe, String> colBienSoXe;
    @FXML private TableColumn<SuaChuaXe, Date> colNgaySuaChua;
    @FXML private TableColumn<SuaChuaXe, String> colNoiDungSuaChua;
    @FXML private TableColumn<SuaChuaXe, String> colMaVatTu;
    @FXML private TableColumn<SuaChuaXe, Integer> colSoLuong;
    @FXML private TableColumn<SuaChuaXe, Double> colThanhTien;

    private final SuaChuaXeService suaChuaService = new SuaChuaXeService();
    private final Map<String, String> partNameToId = new HashMap<>();
    private final Map<String, String> partIdToName = new HashMap<>();
    private boolean refreshing = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        restrictDatePickerToToday(dpNgaySuaChua);
        loadComboBoxes();
        loadTableData();
        setupEvents();
    }

    @Override
    public void refreshData() {
        refreshDataAsync();
    }

    private void setupTableColumns() {
        colMaSuaChua.setCellValueFactory(new PropertyValueFactory<>("maSuaChuaXe"));
        colMaTiepNhanXe.setCellValueFactory(new PropertyValueFactory<>("maTiepNhanXe"));
        colNgaySuaChua.setCellValueFactory(new PropertyValueFactory<>("ngaySuaChua"));
        colThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));

        // Do not query the database while TableView renders each cell.
        // Details are still loaded into the form when a repair order is selected.
        colBienSoXe.setCellValueFactory(cellData -> new SimpleStringProperty(""));
        colNoiDungSuaChua.setCellValueFactory(cellData -> new SimpleStringProperty(""));
        colMaVatTu.setCellValueFactory(cellData -> new SimpleStringProperty(""));
        colSoLuong.setCellValueFactory(cellData -> new SimpleObjectProperty<>(0));
    }

    private void loadComboBoxes() {
        String selectedIntake = cbbMaTiepNhanXe.getValue();
        String selectedPartId = getSelectedPartId();
        String selectedLabor = cbbMaTienCong.getValue();

        List<String> intakeIds = suaChuaService.getAllIntakeIds();
        List<VatTuPhuTung> parts = suaChuaService.getAllParts();
        List<String> laborIds = suaChuaService.getAllLaborIds();

        cbbMaTiepNhanXe.getItems().setAll(intakeIds);
        applyPartComboBox(parts);
        cbbMaTienCong.getItems().setAll(laborIds);

        cbbMaTiepNhanXe.setValue(intakeIds.contains(selectedIntake) ? selectedIntake : null);
        cbbMaVatTu.setValue(partIdToName.get(selectedPartId));
        cbbMaTienCong.setValue(laborIds.contains(selectedLabor) ? selectedLabor : null);
    }

    private void loadTableData() {
        List<SuaChuaXe> list = suaChuaService.getAll();
        tblRepairOrders.setItems(FXCollections.observableArrayList(list));
    }

    private void setupEvents() {
        btnAddRepair.setOnAction(e -> handleAdd());
        btnUpdateRepair.setOnAction(e -> handleUpdate());
        btnDeleteRepair.setOnAction(e -> handleDelete());
        btnClearRepair.setOnAction(e -> clearForm());
        btnSearchRepair.setOnAction(e -> handleSearch());

        btnRefreshRepair.setOnAction(e -> {
            txtSearchRepair.clear();
            loadComboBoxes();
            loadTableData();
        });

        tblRepairOrders.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldValue, selected) -> {
                    if (selected != null) {
                        fillForm(selected);
                    }
                }
        );
    }

    private void handleAdd() {
        Date ngaySuaChua = getRepairDate();
        Integer soLuong = getQuantity();

        if (ngaySuaChua == null || soLuong == null) {
            return;
        }

        boolean result = suaChuaService.add(
                txtMaSuaChua.getText().trim(),
                cbbMaTiepNhanXe.getValue(),
                ngaySuaChua,
                getSelectedPartId(),
                soLuong,
                cbbMaTienCong.getValue(),
                txtNoiDungSuaChua.getText().trim()
        );

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm phiếu sửa chữa thành công!");
            loadComboBoxes();
            loadTableData();
            clearForm();
            markRepairDataChanged();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm. Kiểm tra mã phiếu, tồn kho, mã tiếp nhận, mã tiền công hoặc dữ liệu nhập!");
        }
    }

    private void handleUpdate() {
        SuaChuaXe selected = tblRepairOrders.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn phiếu cần sửa!");
            return;
        }

        Date ngaySuaChua = getRepairDate();
        Integer soLuong = getQuantity();

        if (ngaySuaChua == null || soLuong == null) {
            return;
        }

        boolean result = suaChuaService.update(
                txtMaSuaChua.getText().trim(),
                cbbMaTiepNhanXe.getValue(),
                ngaySuaChua,
                getSelectedPartId(),
                soLuong,
                cbbMaTienCong.getValue(),
                txtNoiDungSuaChua.getText().trim()
        );

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật phiếu sửa chữa thành công!");
            loadComboBoxes();
            loadTableData();
            clearForm();
            markRepairDataChanged();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật. Kiểm tra tồn kho, mã tiền công hoặc dữ liệu nhập!");
        }
    }

    private void handleDelete() {
        SuaChuaXe selected = tblRepairOrders.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn phiếu cần xóa!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc muốn xóa phiếu sửa chữa " + selected.getMaSuaChuaXe() + "?");

        Optional<ButtonType> option = confirm.showAndWait();

        if (option.isPresent() && option.get() == ButtonType.OK) {
            boolean result = suaChuaService.delete(selected.getMaSuaChuaXe());

            if (result) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa phiếu sửa chữa thành công!");
                loadComboBoxes();
                loadTableData();
                clearForm();
                markRepairDataChanged();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa phiếu sửa chữa!");
            }
        }
    }

    private void handleSearch() {
        List<SuaChuaXe> list = suaChuaService.search(txtSearchRepair.getText());
        tblRepairOrders.setItems(FXCollections.observableArrayList(list));
    }

    private void fillForm(SuaChuaXe sc) {
        txtMaSuaChua.setText(sc.getMaSuaChuaXe());
        txtMaSuaChua.setDisable(true);

        cbbMaTiepNhanXe.setValue(sc.getMaTiepNhanXe());

        if (sc.getNgaySuaChua() != null) {
            dpNgaySuaChua.setValue(sc.getNgaySuaChua().toLocalDate());
        } else {
            dpNgaySuaChua.setValue(null);
        }

        ChiTietSuaChuaXe ct = suaChuaService.getDetailByRepairId(sc.getMaSuaChuaXe());

        if (ct != null) {
            txtNoiDungSuaChua.setText(ct.getNoiDung());
            String partId = ct.getMaVatTuPhuTung() == null ? "" : ct.getMaVatTuPhuTung().trim();
            cbbMaVatTu.setValue(partIdToName.getOrDefault(partId, partId));
            txtSoLuong.setText(String.valueOf(ct.getSoLuong()));
            cbbMaTienCong.setValue(ct.getMaTienCong());
        }
    }

    private void clearForm() {
        txtMaSuaChua.setDisable(false);
        txtMaSuaChua.clear();
        cbbMaTiepNhanXe.setValue(null);
        dpNgaySuaChua.setValue(LocalDate.now());
        cbbMaVatTu.setValue(null);
        txtSoLuong.clear();
        cbbMaTienCong.setValue(null);
        txtNoiDungSuaChua.clear();
        txtSearchRepair.clear();

        tblRepairOrders.getSelectionModel().clearSelection();
    }

    private Date getRepairDate() {
        LocalDate localDate = dpNgaySuaChua.getValue();

        if (localDate == null) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Ngày sửa chữa không được rỗng!");
            return null;
        }

        if (!isToday(localDate)) {
            showAlert(Alert.AlertType.WARNING, "Sai ngày", "Ngày sửa chữa chỉ được chọn ngày hôm nay!");
            return null;
        }

        return Date.valueOf(localDate);
    }

    private void restrictDatePickerToToday(DatePicker datePicker) {
        datePicker.setEditable(false);
        datePicker.setValue(LocalDate.now());
        datePicker.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
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

    private Integer getQuantity() {
        try {
            int value = Integer.parseInt(txtSoLuong.getText().trim());

            if (value <= 0) {
                showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Số lượng phải lớn hơn 0!");
                return null;
            }

            return value;

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Số lượng phải là số nguyên!");
            return null;
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void refreshDataAsync() {
        if (refreshing) {
            return;
        }

        refreshing = true;
        String selectedIntake = cbbMaTiepNhanXe.getValue();
        String selectedPartId = getSelectedPartId();
        String selectedLabor = cbbMaTienCong.getValue();

        Task<RepairRefreshData> task = new Task<>() {
            @Override
            protected RepairRefreshData call() {
                return new RepairRefreshData(
                        suaChuaService.getAllIntakeIds(),
                        suaChuaService.getAllParts(),
                        suaChuaService.getAllLaborIds(),
                        suaChuaService.getAll()
                );
            }
        };

        task.setOnSucceeded(e -> {
            refreshing = false;
            RepairRefreshData data = task.getValue();

            cbbMaTiepNhanXe.getItems().setAll(data.intakeIds());
            applyPartComboBox(data.parts());
            cbbMaTienCong.getItems().setAll(data.laborIds());
            cbbMaTiepNhanXe.setValue(data.intakeIds().contains(selectedIntake) ? selectedIntake : null);
            cbbMaVatTu.setValue(partIdToName.get(selectedPartId));
            cbbMaTienCong.setValue(data.laborIds().contains(selectedLabor) ? selectedLabor : null);
            tblRepairOrders.setItems(FXCollections.observableArrayList(data.repairs()));
        });

        task.setOnFailed(e -> refreshing = false);

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void markRepairDataChanged() {
        DataRefreshService.markDirty(
                DataRefreshService.DASHBOARD,
                DataRefreshService.INTAKE,
                DataRefreshService.BILLING,
                DataRefreshService.LOOKUP,
                DataRefreshService.REPORTS,
                DataRefreshService.PARTS
        );
    }

    private void applyPartComboBox(List<VatTuPhuTung> parts) {
        partNameToId.clear();
        partIdToName.clear();
        cbbMaVatTu.getItems().clear();

        for (VatTuPhuTung part : parts) {
            String id = part.getMaVatTuPhuTung().trim();
            String name = part.getTenVatTuPhuTung().trim();
            partNameToId.put(name, id);
            partIdToName.put(id, name);
            cbbMaVatTu.getItems().add(name);
        }
    }

    private String getSelectedPartId() {
        String value = cbbMaVatTu.getValue();
        return value == null ? null : partNameToId.getOrDefault(value, value).trim();
    }

    private record RepairRefreshData(
            List<String> intakeIds,
            List<VatTuPhuTung> parts,
            List<String> laborIds,
            List<SuaChuaXe> repairs
    ) {
    }
}
