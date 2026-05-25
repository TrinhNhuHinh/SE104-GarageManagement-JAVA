package Controllers;

import MODEL.PhieuThuTien;
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

public class BillingCollectionsController implements Initializable {

    @FXML private Label lblCurrentDebt;
    @FXML private Label lblAmountPaid;
    @FXML private Label lblRemainingDebt;

    @FXML private TextField txtMaPhieuThu;
    @FXML private ComboBox<String> cbbMaTiepNhanXe;
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
    @FXML private TableColumn<PhieuThuTien, String> colMaTiepNhanXe;
    @FXML private TableColumn<PhieuThuTien, String> colBienSoXe;
    @FXML private TableColumn<PhieuThuTien, Date> colNgayThuTien;
    @FXML private TableColumn<PhieuThuTien, String> colSoDienThoai;
    @FXML private TableColumn<PhieuThuTien, Double> colSoTienThu;

    private final PhieuThuTienService phieuThuService = new PhieuThuTienService();

    private final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        loadComboBox();
        loadTableData();
        setupEvents();
        updateDebtCards();
    }

    private void setupTableColumns() {
        colMaPhieuThu.setCellValueFactory(new PropertyValueFactory<>("maPhieuThuTien"));
        colMaTiepNhanXe.setCellValueFactory(new PropertyValueFactory<>("maTiepNhanXe"));
        colBienSoXe.setCellValueFactory(new PropertyValueFactory<>("bienSoXe"));
        colNgayThuTien.setCellValueFactory(new PropertyValueFactory<>("ngayThuTien"));
        colSoDienThoai.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        colSoTienThu.setCellValueFactory(new PropertyValueFactory<>("soTienThu"));
    }

    private void loadComboBox() {
        cbbMaTiepNhanXe.getItems().setAll(phieuThuService.getAllIntakeIds());
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
            updateDebtCards();
        });

        cbbMaTiepNhanXe.setOnAction(e -> updateDebtCards());

        txtSoTienThu.textProperty().addListener((obs, oldValue, newValue) -> updateDebtCards());

        tblReceipts.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldValue, selected) -> {
                    if (selected != null) {
                        fillForm(selected);
                    }
                }
        );
    }

    private void handleAdd() {
        LocalDate localDate = dpNgayThuTien.getValue();
        Double amount = getAmount();

        if (localDate == null) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Ngày thu tiền không được rỗng!");
            return;
        }

        if (amount == null) {
            return;
        }

        boolean result = phieuThuService.add(
                txtMaPhieuThu.getText().trim(),
                cbbMaTiepNhanXe.getValue(),
                Date.valueOf(localDate),
                amount
        );

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Tạo phiếu thu thành công!");
            loadComboBox();
            loadTableData();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tạo phiếu thu. Kiểm tra mã phiếu, số tiền thu và tiền nợ!");
        }
    }

    private void handleUpdate() {
        PhieuThuTien selected = tblReceipts.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn phiếu thu cần sửa!");
            return;
        }

        LocalDate localDate = dpNgayThuTien.getValue();
        Double amount = getAmount();

        if (localDate == null) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Ngày thu tiền không được rỗng!");
            return;
        }

        if (amount == null) {
            return;
        }

        boolean result = phieuThuService.update(
                selected.getMaPhieuThuTien(),
                Date.valueOf(localDate),
                amount
        );

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật phiếu thu thành công!");
            loadComboBox();
            loadTableData();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật. Số tiền thu có thể lớn hơn tiền nợ!");
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

        cbbMaTiepNhanXe.setValue(pt.getMaTiepNhanXe());
        cbbMaTiepNhanXe.setDisable(true);

        if (pt.getNgayThuTien() != null) {
            dpNgayThuTien.setValue(pt.getNgayThuTien().toLocalDate());
        } else {
            dpNgayThuTien.setValue(null);
        }

        txtSoTienThu.setText(String.valueOf(pt.getSoTienThu()));
        updateDebtCards();
    }

    private void clearForm() {
        txtMaPhieuThu.setDisable(false);
        cbbMaTiepNhanXe.setDisable(false);

        txtMaPhieuThu.clear();
        cbbMaTiepNhanXe.setValue(null);
        dpNgayThuTien.setValue(null);
        txtSoTienThu.clear();
        txtSearchReceipt.clear();

        tblReceipts.getSelectionModel().clearSelection();
        updateDebtCards();
    }

    private Double getAmount() {
        try {
            double amount = Double.parseDouble(txtSoTienThu.getText().trim());

            if (amount <= 0) {
                showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Số tiền thu phải lớn hơn 0!");
                return null;
            }

            return amount;

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Số tiền thu phải là số!");
            return null;
        }
    }

    private void updateDebtCards() {
        String maTiepNhanXe = cbbMaTiepNhanXe.getValue();

        double currentDebt = 0;

        if (maTiepNhanXe != null && !maTiepNhanXe.trim().isEmpty()) {
            currentDebt = phieuThuService.getCurrentDebt(maTiepNhanXe);
        }

        double paid = 0;

        try {
            String text = txtSoTienThu.getText().trim();

            if (!text.isEmpty()) {
                paid = Double.parseDouble(text);
            }

        } catch (Exception e) {
            paid = 0;
        }

        lblCurrentDebt.setText(formatMoney(currentDebt));
        lblAmountPaid.setText(formatMoney(paid));
        lblRemainingDebt.setText(formatMoney(currentDebt - paid));
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
}