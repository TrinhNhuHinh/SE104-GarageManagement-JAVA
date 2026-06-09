package Controllers;

import MODEL.PhieuThuTien;
import Service.DataRefreshService;
import Service.PhieuThuTienService;
import java.net.URL;
import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class BillingCollectionsController implements Initializable, Refreshable {

    @FXML private Label lblRepairTotal;
    @FXML private Label lblAmountPaid;
    @FXML private Label lblRemainingDebt;

    @FXML private TextField txtMaPhieuThu;
    @FXML private ComboBox<String> cbbMaSuaChuaXe;
    @FXML private TextField txtMaTiepNhanXe;
    @FXML private DatePicker dpNgayThuTien;
    @FXML private TextField txtSoTienThu;

    @FXML private Button btnCreateReceipt;
    @FXML private Button btnUpdateReceipt;
    @FXML private Button btnDeleteReceipt;
    @FXML private Button btnClearReceipt;

    @FXML private TextField txtSearchReceipt;
    @FXML private Button btnSearchReceipt;
    @FXML private Button btnRefreshReceipt;

    @FXML private TableView<PhieuThuTien> tblReceipts;
    @FXML private TableColumn<PhieuThuTien, String> colMaPhieuThu;
    @FXML private TableColumn<PhieuThuTien, String> colMaSuaChuaXe;
    @FXML private TableColumn<PhieuThuTien, String> colMaTiepNhanXe;
    @FXML private TableColumn<PhieuThuTien, String> colBienSoXe;
    @FXML private TableColumn<PhieuThuTien, Date> colNgayThuTien;
    @FXML private TableColumn<PhieuThuTien, String> colSoDienThoai;
    @FXML private TableColumn<PhieuThuTien, Double> colSoTienThu;

    private final PhieuThuTienService phieuThuService = new PhieuThuTienService();
    private boolean refreshing = false;

    private final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        setupInitialState();
        restrictDatePickerToToday(dpNgayThuTien);
        loadComboBox();
        loadTableData();
        setupEvents();
        updateRepairCards();
    }

    @Override
    public void refreshData() {
        refreshDataAsync();
    }

    private void setupInitialState() {
        txtMaTiepNhanXe.setDisable(true);
        txtSoTienThu.setDisable(true);
    }

    private void setupTableColumns() {
        colMaPhieuThu.setCellValueFactory(new PropertyValueFactory<>("maPhieuThuTien"));
        colMaSuaChuaXe.setCellValueFactory(new PropertyValueFactory<>("maSuaChuaXe"));
        colMaTiepNhanXe.setCellValueFactory(new PropertyValueFactory<>("maTiepNhanXe"));
        colBienSoXe.setCellValueFactory(new PropertyValueFactory<>("bienSoXe"));
        colNgayThuTien.setCellValueFactory(new PropertyValueFactory<>("ngayThuTien"));
        colSoDienThoai.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        colSoTienThu.setCellValueFactory(new PropertyValueFactory<>("soTienThu"));
    }

    private void loadComboBox() {
        String selectedRepair = cbbMaSuaChuaXe.getValue();
        List<String> repairIds = phieuThuService.getAllRepairIds();

        cbbMaSuaChuaXe.getItems().setAll(repairIds);
        cbbMaSuaChuaXe.setValue(repairIds.contains(selectedRepair) ? selectedRepair : null);
    }

    private void loadTableData() {
        List<PhieuThuTien> list = phieuThuService.getAll();
        tblReceipts.setItems(FXCollections.observableArrayList(list));
    }

    private void setupEvents() {
        btnCreateReceipt.setOnAction(e -> handleAdd());
        btnUpdateReceipt.setOnAction(e -> handleUpdate());
        btnDeleteReceipt.setOnAction(e -> handleDelete());
        btnClearReceipt.setOnAction(e -> clearForm());
        btnSearchReceipt.setOnAction(e -> handleSearch());

        btnRefreshReceipt.setOnAction(e -> {
            txtSearchReceipt.clear();
            loadComboBox();
            loadTableData();
            updateRepairCards();
        });

        cbbMaSuaChuaXe.setOnAction(e -> updateRepairCards());

        tblReceipts.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldValue, selected) -> {
                    if (selected != null) {
                        fillForm(selected);
                    }
                }
        );
    }

    private void handleAdd() {
        String maPhieuThu = txtMaPhieuThu.getText().trim();
        String maSuaChuaXe = cbbMaSuaChuaXe.getValue();
        LocalDate localDate = dpNgayThuTien.getValue();

        if (maPhieuThu.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Mã phiếu thu không được rỗng!");
            return;
        }

        if (maSuaChuaXe == null || maSuaChuaXe.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Vui lòng chọn mã sửa chữa!");
            return;
        }

        if (localDate == null) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Ngày thu tiền không được rỗng!");
            return;
        }

        if (!isToday(localDate)) {
            showAlert(Alert.AlertType.WARNING, "Sai ngày", "Ngày thu tiền chỉ được chọn ngày hôm nay!");
            return;
        }

        double amount = phieuThuService.getRemainingByRepairId(maSuaChuaXe);
        double currentDebt = phieuThuService.getCurrentDebtByRepairId(maSuaChuaXe);

        if (amount <= 0) {
            showAlert(Alert.AlertType.WARNING, "Không thể thu", "Phiếu sửa chữa này đã được thu đủ tiền!");
            return;
        }

        if (amount > currentDebt) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Dữ liệu nợ không hợp lệ",
                    "Số tiền còn phải thu của phiếu sửa chữa lớn hơn tiền nợ hiện tại của xe.\n"
                    + "Còn phải thu: " + formatMoney(amount) + "\n"
                    + "Tiền nợ hiện tại: " + formatMoney(currentDebt)
            );
            return;
        }

        boolean result = phieuThuService.add(
                maPhieuThu,
                maSuaChuaXe,
                Date.valueOf(localDate),
                amount
        );

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Tạo phiếu thu thành công!");
            loadComboBox();
            loadTableData();
            clearForm();
            markBillingDataChanged();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tạo phiếu thu. Kiểm tra mã phiếu, mã sửa chữa hoặc tiền nợ!");
        }
    }

    private void handleUpdate() {
        PhieuThuTien selected = tblReceipts.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn phiếu thu cần sửa!");
            return;
        }

        LocalDate localDate = dpNgayThuTien.getValue();

        if (localDate == null) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Ngày thu tiền không được rỗng!");
            return;
        }

        if (!isToday(localDate)) {
            showAlert(Alert.AlertType.WARNING, "Sai ngày", "Ngày thu tiền chỉ được chọn ngày hôm nay!");
            return;
        }

        double amount = selected.getSoTienThu();

        boolean result = phieuThuService.update(
                selected.getMaPhieuThuTien(),
                Date.valueOf(localDate),
                amount
        );

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật ngày thu tiền thành công!");
            loadComboBox();
            loadTableData();
            clearForm();
            markBillingDataChanged();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật phiếu thu!");
        }
    }

    private void handleDelete() {
        PhieuThuTien selected = tblReceipts.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn phiếu thu cần xóa!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc muốn xóa phiếu thu " + selected.getMaPhieuThuTien() + "?");

        Optional<ButtonType> option = confirm.showAndWait();

        if (option.isPresent() && option.get() == ButtonType.OK) {
            boolean result = phieuThuService.delete(selected.getMaPhieuThuTien());

            if (result) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa phiếu thu thành công!");
                loadComboBox();
                loadTableData();
                clearForm();
                markBillingDataChanged();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa phiếu thu!");
            }
        }
    }

    private void handleSearch() {
        List<PhieuThuTien> list = phieuThuService.search(txtSearchReceipt.getText());
        tblReceipts.setItems(FXCollections.observableArrayList(list));
    }

    private void fillForm(PhieuThuTien pt) {
        txtMaPhieuThu.setText(pt.getMaPhieuThuTien());
        txtMaPhieuThu.setDisable(true);

        cbbMaSuaChuaXe.setValue(pt.getMaSuaChuaXe());
        cbbMaSuaChuaXe.setDisable(true);

        txtMaTiepNhanXe.setText(pt.getMaTiepNhanXe());

        if (pt.getNgayThuTien() != null) {
            dpNgayThuTien.setValue(pt.getNgayThuTien().toLocalDate());
        } else {
            dpNgayThuTien.setValue(null);
        }

        txtSoTienThu.setText(String.valueOf(pt.getSoTienThu()));
        updateRepairCards();
    }

    private void clearForm() {
        txtMaPhieuThu.setDisable(false);
        cbbMaSuaChuaXe.setDisable(false);

        txtMaPhieuThu.clear();
        cbbMaSuaChuaXe.setValue(null);
        txtMaTiepNhanXe.clear();
        dpNgayThuTien.setValue(LocalDate.now());
        txtSoTienThu.clear();
        txtSearchReceipt.clear();

        tblReceipts.getSelectionModel().clearSelection();
        updateRepairCards();
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

    private void updateRepairCards() {
        String maSuaChuaXe = cbbMaSuaChuaXe.getValue();

        if (maSuaChuaXe == null || maSuaChuaXe.trim().isEmpty()) {
            lblRepairTotal.setText(formatMoney(0));
            lblAmountPaid.setText(formatMoney(0));
            lblRemainingDebt.setText(formatMoney(0));
            txtMaTiepNhanXe.clear();
            txtSoTienThu.clear();
            return;
        }

        double repairTotal = phieuThuService.getRepairTotal(maSuaChuaXe);
        double paid = phieuThuService.getPaidByRepairId(maSuaChuaXe);
        double remaining = phieuThuService.getRemainingByRepairId(maSuaChuaXe);
        String intakeId = phieuThuService.getIntakeIdByRepairId(maSuaChuaXe);

        lblRepairTotal.setText(formatMoney(repairTotal));
        lblAmountPaid.setText(formatMoney(paid));
        lblRemainingDebt.setText(formatMoney(remaining));

        txtMaTiepNhanXe.setText(intakeId);

        if (tblReceipts.getSelectionModel().getSelectedItem() == null) {
            txtSoTienThu.setText(String.valueOf(remaining));
        }
    }

    private String formatMoney(double value) {
        return currencyFormat.format(value);
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
        String selectedRepair = cbbMaSuaChuaXe.getValue();

        Task<BillingRefreshData> task = new Task<>() {
            @Override
            protected BillingRefreshData call() {
                return new BillingRefreshData(
                        phieuThuService.getAllRepairIds(),
                        phieuThuService.getAll()
                );
            }
        };

        task.setOnSucceeded(e -> {
            refreshing = false;
            BillingRefreshData data = task.getValue();
            cbbMaSuaChuaXe.getItems().setAll(data.repairIds());
            cbbMaSuaChuaXe.setValue(data.repairIds().contains(selectedRepair) ? selectedRepair : null);
            tblReceipts.setItems(FXCollections.observableArrayList(data.receipts()));
            updateRepairCards();
        });

        task.setOnFailed(e -> refreshing = false);

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void markBillingDataChanged() {
        DataRefreshService.markDirty(
                DataRefreshService.DASHBOARD,
                DataRefreshService.INTAKE,
                DataRefreshService.REPAIR,
                DataRefreshService.LOOKUP,
                DataRefreshService.REPORTS
        );
    }

    private record BillingRefreshData(
            List<String> repairIds,
            List<PhieuThuTien> receipts
    ) {
    }
}
