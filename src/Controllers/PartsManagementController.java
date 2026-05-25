package Controllers;

import MODEL.VatTuPhuTung;
import Service.VatTuPhuTungService;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

public class PartsManagementController implements Initializable {

    @FXML private Label lblTotalParts;
    @FXML private Label lblLowStock;
    @FXML private Label lblInventoryValue;

    @FXML private TextField txtMaVatTu;
    @FXML private TextField txtTenVatTu;
    @FXML private ComboBox<String> cbbDonViTinh;
    @FXML private TextField txtSoLuongTon;
    @FXML private TextField txtGiaNhap;
    @FXML private TextField txtGiaBan;

    @FXML private Button btnAddPart;
    @FXML private Button btnImportPart;
    @FXML private Button btnSellPart;
    @FXML private Button btnUpdatePart;
    @FXML private Button btnDeletePart;

    @FXML private TextField txtSearchPart;
    @FXML private Button btnSearchPart;
    @FXML private Button btnRefreshPart;

    @FXML private TableView<VatTuPhuTung> tblParts;
    @FXML private TableColumn<VatTuPhuTung, String> colMaVatTu;
    @FXML private TableColumn<VatTuPhuTung, String> colTenVatTu;
    @FXML private TableColumn<VatTuPhuTung, String> colDonViTinh;
    @FXML private TableColumn<VatTuPhuTung, Integer> colSoLuongTon;
    @FXML private TableColumn<VatTuPhuTung, String> colGiaNhap;
    @FXML private TableColumn<VatTuPhuTung, String> colGiaBan;

    private final VatTuPhuTungService vatTuService = new VatTuPhuTungService();

    private final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupComboBox();
        setupTableColumns();
        loadTableData();
        loadSummaryCards();
        setupEvents();
    }

    private void setupComboBox() {
        cbbDonViTinh.getItems().setAll(
                "Cái",
                "Bộ",
                "Chiếc",
                "Lít",
                "Hộp",
                "Chai",
                "Cặp",
                "Mét"
        );
    }

    private void setupTableColumns() {
        colMaVatTu.setCellValueFactory(new PropertyValueFactory<>("maVatTuPhuTung"));
        colTenVatTu.setCellValueFactory(new PropertyValueFactory<>("tenVatTuPhuTung"));
        colDonViTinh.setCellValueFactory(new PropertyValueFactory<>("donViTinh"));
        colSoLuongTon.setCellValueFactory(new PropertyValueFactory<>("soLuongVatTuPhuTung"));

        colGiaNhap.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatMoney(cellData.getValue().getDonGiaVatTuPhuTung()))
        );

        // Database hiện chỉ có 1 đơn giá, nên cột Sell Price hiển thị cùng giá.
        colGiaBan.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatMoney(cellData.getValue().getDonGiaVatTuPhuTung()))
        );
    }

    private void setupEvents() {
        btnAddPart.setOnAction(e -> handleAdd());
        btnUpdatePart.setOnAction(e -> handleUpdate());
        btnDeletePart.setOnAction(e -> handleDelete());
        btnImportPart.setOnAction(e -> handleImport());
        btnSellPart.setOnAction(e -> handleSell());
        btnSearchPart.setOnAction(e -> handleSearch());
        btnRefreshPart.setOnAction(e -> {
            txtSearchPart.clear();
            loadTableData();
            loadSummaryCards();
        });

        tblParts.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, selectedItem) -> {
                    if (selectedItem != null) {
                        fillForm(selectedItem);
                    }
                }
        );

        txtGiaNhap.textProperty().addListener((observable, oldValue, newValue) -> {
            // Tạm thời đồng bộ Sell Price vì database chỉ có 1 đơn giá.
            txtGiaBan.setText(newValue);
        });
    }

    private void loadTableData() {
        try {
            List<VatTuPhuTung> list = vatTuService.getAll();
            ObservableList<VatTuPhuTung> data = FXCollections.observableArrayList(list);
            tblParts.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể load dữ liệu vật tư!");
        }
    }

    private void loadSummaryCards() {
        lblTotalParts.setText(String.valueOf(vatTuService.countAll()));
        lblLowStock.setText(String.valueOf(vatTuService.countLowStock()));
        lblInventoryValue.setText(formatMoney(vatTuService.getInventoryValue()));
    }

    private void handleAdd() {
        VatTuPhuTung vt = getFormData();

        if (vt == null) {
            return;
        }

        boolean result = vatTuService.add(vt);

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm vật tư thành công!");
            refreshAll();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm. Mã vật tư có thể đã tồn tại hoặc dữ liệu không hợp lệ!");
        }
    }

    private void handleUpdate() {
        VatTuPhuTung selected = tblParts.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn vật tư cần sửa!");
            return;
        }

        VatTuPhuTung vt = getFormData();

        if (vt == null) {
            return;
        }

        boolean result = vatTuService.update(vt);

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật vật tư thành công!");
            refreshAll();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật vật tư!");
        }
    }

    private void handleDelete() {
        VatTuPhuTung selected = tblParts.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn vật tư cần xóa!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc muốn xóa vật tư: " + selected.getMaVatTuPhuTung() + "?");

        Optional<ButtonType> option = confirm.showAndWait();

        if (option.isPresent() && option.get() == ButtonType.OK) {
            boolean result = vatTuService.delete(selected.getMaVatTuPhuTung());

            if (result) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa vật tư thành công!");
                refreshAll();
                clearForm();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa. Vật tư có thể đang được dùng trong phiếu sửa chữa, nhập vật tư hoặc bán vật tư!");
            }
        }
    }

    private void handleImport() {
        VatTuPhuTung selected = tblParts.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn vật tư cần nhập thêm!");
            return;
        }

        Integer quantity = askQuantity("Nhập vật tư", "Nhập số lượng cần cộng thêm:");

        if (quantity == null) {
            return;
        }

        boolean result = vatTuService.importPart(selected.getMaVatTuPhuTung(), quantity);

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Nhập thêm vật tư thành công!");
            refreshAll();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể nhập vật tư. Số lượng phải lớn hơn 0!");
        }
    }

    private void handleSell() {
        VatTuPhuTung selected = tblParts.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn vật tư cần bán!");
            return;
        }

        Integer quantity = askQuantity("Bán vật tư", "Nhập số lượng cần bán:");

        if (quantity == null) {
            return;
        }

        boolean result = vatTuService.sellPart(selected.getMaVatTuPhuTung(), quantity);

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Bán vật tư thành công!");
            refreshAll();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể bán. Số lượng bán phải lớn hơn 0 và không vượt quá tồn kho!");
        }
    }

    private void handleSearch() {
        String keyword = txtSearchPart.getText();

        try {
            List<VatTuPhuTung> list = vatTuService.search(keyword);
            ObservableList<VatTuPhuTung> data = FXCollections.observableArrayList(list);
            tblParts.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tìm kiếm vật tư!");
        }
    }

    private VatTuPhuTung getFormData() {
        String maVatTu = txtMaVatTu.getText().trim();
        String tenVatTu = txtTenVatTu.getText().trim();
        String donViTinh = cbbDonViTinh.getValue();
        String soLuongText = txtSoLuongTon.getText().trim();
        String giaText = txtGiaNhap.getText().trim();

        if (maVatTu.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Mã vật tư không được rỗng!");
            return null;
        }

        if (tenVatTu.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Tên vật tư không được rỗng!");
            return null;
        }

        if (donViTinh == null || donViTinh.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Vui lòng chọn đơn vị tính!");
            return null;
        }

        int soLuong;

        try {
            soLuong = Integer.parseInt(soLuongText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Số lượng tồn phải là số nguyên!");
            return null;
        }

        if (soLuong < 0) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Số lượng tồn không được âm!");
            return null;
        }

        double donGia;

        try {
            donGia = Double.parseDouble(giaText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Đơn giá phải là số!");
            return null;
        }

        if (donGia < 0) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Đơn giá không được âm!");
            return null;
        }

        return new VatTuPhuTung(
                maVatTu,
                tenVatTu,
                donGia,
                soLuong,
                donViTinh
        );
    }

    private void fillForm(VatTuPhuTung vt) {
        txtMaVatTu.setText(vt.getMaVatTuPhuTung());
        txtMaVatTu.setDisable(true);

        txtTenVatTu.setText(vt.getTenVatTuPhuTung());
        cbbDonViTinh.setValue(vt.getDonViTinh());
        txtSoLuongTon.setText(String.valueOf(vt.getSoLuongVatTuPhuTung()));
        txtGiaNhap.setText(String.valueOf(vt.getDonGiaVatTuPhuTung()));
        txtGiaBan.setText(String.valueOf(vt.getDonGiaVatTuPhuTung()));
    }

    private void clearForm() {
        txtMaVatTu.setDisable(false);
        txtMaVatTu.clear();
        txtTenVatTu.clear();
        cbbDonViTinh.setValue(null);
        txtSoLuongTon.clear();
        txtGiaNhap.clear();
        txtGiaBan.clear();

        tblParts.getSelectionModel().clearSelection();
    }

    private void refreshAll() {
        loadTableData();
        loadSummaryCards();
    }

    private Integer askQuantity(String title, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();

        if (result.isEmpty()) {
            return null;
        }

        try {
            int quantity = Integer.parseInt(result.get().trim());

            if (quantity <= 0) {
                showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Số lượng phải lớn hơn 0!");
                return null;
            }

            return quantity;

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Số lượng phải là số nguyên!");
            return null;
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
}