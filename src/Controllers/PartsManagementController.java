package Controllers;

import MODEL.ChiTietBanVatTu;
import MODEL.ChiTietNhapVatTu;
import MODEL.VatTuPhuTung;
import Service.VatTuPhuTungService;
import java.net.URL;
import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class PartsManagementController implements Initializable, Refreshable {

    @FXML private Label lblTotalParts;
    @FXML private Label lblLowStock;
    @FXML private Label lblInventoryValue;
    @FXML private TabPane partsTabPane;
    @FXML private Tab tabInventory;
    @FXML private Tab tabImport;
    @FXML private Tab tabSale;

    @FXML private TextField txtMaVatTu;
    @FXML private TextField txtTenVatTu;
    @FXML private ComboBox<String> cbbDonViTinh;
    @FXML private TextField txtSoLuongTon;
    @FXML private TextField txtGiaNhap;
    @FXML private TextField txtGiaBan;

    @FXML private Button btnAddPart;
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

    @FXML private TextField txtMaNhapVatTu;
    @FXML private ComboBox<String> cbbMaNhaCungCap;
    @FXML private DatePicker dpNgayNhap;
    @FXML private ComboBox<String> cbbImportMaVatTu;
    @FXML private TextField txtImportSoLuong;
    @FXML private TextField txtImportDonGia;
    @FXML private Label lblImportTotal;
    @FXML private Button btnCreateImport;
    @FXML private Button btnClearImport;

    @FXML private TableView<ChiTietNhapVatTu> tblImportDetails;
    @FXML private TableColumn<ChiTietNhapVatTu, String> colImportId;
    @FXML private TableColumn<ChiTietNhapVatTu, String> colImportSupplier;
    @FXML private TableColumn<ChiTietNhapVatTu, String> colImportDate;
    @FXML private TableColumn<ChiTietNhapVatTu, String> colImportPartId;
    @FXML private TableColumn<ChiTietNhapVatTu, Integer> colImportQuantity;
    @FXML private TableColumn<ChiTietNhapVatTu, String> colImportUnitPrice;
    @FXML private TableColumn<ChiTietNhapVatTu, String> colImportTotal;

    @FXML private TextField txtMaBanVatTu;
    @FXML private ComboBox<String> cbbMaKhachHang;
    @FXML private DatePicker dpNgayBan;
    @FXML private ComboBox<String> cbbSaleMaVatTu;
    @FXML private TextField txtSaleSoLuong;
    @FXML private TextField txtSaleDonGia;
    @FXML private Label lblSaleTotal;
    @FXML private Button btnCreateSale;
    @FXML private Button btnClearSale;

    @FXML private TableView<ChiTietBanVatTu> tblSaleDetails;
    @FXML private TableColumn<ChiTietBanVatTu, String> colSaleId;
    @FXML private TableColumn<ChiTietBanVatTu, String> colSaleCustomer;
    @FXML private TableColumn<ChiTietBanVatTu, String> colSaleDate;
    @FXML private TableColumn<ChiTietBanVatTu, String> colSalePartId;
    @FXML private TableColumn<ChiTietBanVatTu, Integer> colSaleQuantity;
    @FXML private TableColumn<ChiTietBanVatTu, String> colSaleUnitPrice;
    @FXML private TableColumn<ChiTietBanVatTu, String> colSaleTotal;

    private final VatTuPhuTungService vatTuService = new VatTuPhuTungService();
    private final Map<String, String> importSupplierById = new HashMap<>();
    private final Map<String, String> importDateById = new HashMap<>();
    private final Map<String, String> saleCustomerById = new HashMap<>();
    private final Map<String, String> saleDateById = new HashMap<>();
    private boolean importDetailsLoaded = false;
    private boolean saleDetailsLoaded = false;

    private final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupComboBoxes();
        setupTableColumns();
        setupEvents();
        loadInitialData();
        updateTotalLabels();
    }

    @Override
    public void refreshData() {
        loadAllData();
        updateTotalLabels();
    }

    private void setupComboBoxes() {
        cbbDonViTinh.getItems().setAll("Cái", "Bộ", "Chiếc", "Lít", "Hộp", "Chai", "Cặp", "Mét");

        cbbMaNhaCungCap.getItems().setAll(vatTuService.getAllSupplierIds());
        cbbMaKhachHang.getItems().setAll(vatTuService.getAllCustomerIds());

        loadPartComboBoxes();
    }

    private void loadPartComboBoxes() {
        List<String> partIds = vatTuService.getAllPartIds();

        cbbImportMaVatTu.getItems().setAll(partIds);
        cbbSaleMaVatTu.getItems().setAll(partIds);
    }

    private void setupTableColumns() {
        colMaVatTu.setCellValueFactory(new PropertyValueFactory<>("maVatTuPhuTung"));
        colTenVatTu.setCellValueFactory(new PropertyValueFactory<>("tenVatTuPhuTung"));
        colDonViTinh.setCellValueFactory(new PropertyValueFactory<>("donViTinh"));
        colSoLuongTon.setCellValueFactory(new PropertyValueFactory<>("soLuongVatTuPhuTung"));

        colGiaNhap.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatMoney(cellData.getValue().getDonGiaVatTuPhuTung()))
        );

        colGiaBan.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatMoney(cellData.getValue().getDonGiaVatTuPhuTung()))
        );

        colImportId.setCellValueFactory(new PropertyValueFactory<>("maNhapVatTu"));
        colImportPartId.setCellValueFactory(new PropertyValueFactory<>("maVatTuPhuTung"));
        colImportQuantity.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        colImportSupplier.setCellValueFactory(cellData ->
                new SimpleStringProperty(importSupplierById.getOrDefault(cellData.getValue().getMaNhapVatTu(), ""))
        );

        colImportDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(importDateById.getOrDefault(cellData.getValue().getMaNhapVatTu(), ""))
        );

        colImportUnitPrice.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatMoney(cellData.getValue().getDonGia()))
        );

        colImportTotal.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatMoney(cellData.getValue().getThanhTien()))
        );

        colSaleId.setCellValueFactory(new PropertyValueFactory<>("maBanVatTu"));
        colSalePartId.setCellValueFactory(new PropertyValueFactory<>("maVatTuPhuTung"));
        colSaleQuantity.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        colSaleCustomer.setCellValueFactory(cellData ->
                new SimpleStringProperty(saleCustomerById.getOrDefault(cellData.getValue().getMaBanVatTu(), ""))
        );

        colSaleDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(saleDateById.getOrDefault(cellData.getValue().getMaBanVatTu(), ""))
        );

        colSaleUnitPrice.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatMoney(cellData.getValue().getDonGia()))
        );

        colSaleTotal.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatMoney(cellData.getValue().getThanhTien()))
        );
    }

    private void setupEvents() {
        btnAddPart.setOnAction(e -> handleAdd());
        btnUpdatePart.setOnAction(e -> handleUpdate());
        btnDeletePart.setOnAction(e -> handleDelete());
        btnSearchPart.setOnAction(e -> handleSearch());

        btnRefreshPart.setOnAction(e -> {
            txtSearchPart.clear();
            refreshAll();
        });

        btnCreateImport.setOnAction(e -> handleCreateImport());
        btnClearImport.setOnAction(e -> clearImportForm());

        btnCreateSale.setOnAction(e -> handleCreateSale());
        btnClearSale.setOnAction(e -> clearSaleForm());

        tblParts.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, selectedItem) -> {
                    if (selectedItem != null) {
                        fillForm(selectedItem);
                    }
                }
        );

        txtGiaNhap.textProperty().addListener((observable, oldValue, newValue) -> txtGiaBan.setText(newValue));

        cbbImportMaVatTu.setOnAction(e -> fillImportPriceByPart());
        cbbSaleMaVatTu.setOnAction(e -> fillSalePriceByPart());

        txtImportSoLuong.textProperty().addListener((obs, oldValue, newValue) -> updateImportTotalLabel());
        txtImportDonGia.textProperty().addListener((obs, oldValue, newValue) -> updateImportTotalLabel());

        txtSaleSoLuong.textProperty().addListener((obs, oldValue, newValue) -> updateSaleTotalLabel());
        txtSaleDonGia.textProperty().addListener((obs, oldValue, newValue) -> updateSaleTotalLabel());

        partsTabPane.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldTab, selectedTab) -> loadDetailsForTab(selectedTab)
        );
    }

    private void loadInitialData() {
        loadTableData();
        loadSummaryCards();
        loadPartComboBoxes();
        tblImportDetails.setItems(FXCollections.observableArrayList());
        tblSaleDetails.setItems(FXCollections.observableArrayList());
    }

    private void loadDetailsForTab(Tab selectedTab) {
        if (selectedTab == tabImport && !importDetailsLoaded) {
            loadImportDetails();
        } else if (selectedTab == tabSale && !saleDetailsLoaded) {
            loadSaleDetails();
        }
    }

    private void loadAllData() {
        loadTableData();
        loadImportDetails();
        loadSaleDetails();
        loadSummaryCards();
        loadPartComboBoxes();
    }

    private void loadTableData() {
        List<VatTuPhuTung> list = vatTuService.getAll();
        ObservableList<VatTuPhuTung> data = FXCollections.observableArrayList(list);
        tblParts.setItems(data);
    }

    private void loadImportDetails() {
        List<ChiTietNhapVatTu> details = vatTuService.getAllImportDetails();

        importSupplierById.clear();
        importDateById.clear();

        for (ChiTietNhapVatTu detail : details) {
            String importId = detail.getMaNhapVatTu();
            importSupplierById.computeIfAbsent(importId, vatTuService::getSupplierByImportId);
            importDateById.computeIfAbsent(importId, vatTuService::getImportDateByImportId);
        }

        tblImportDetails.setItems(FXCollections.observableArrayList(details));
        importDetailsLoaded = true;
    }

    private void loadSaleDetails() {
        List<ChiTietBanVatTu> details = vatTuService.getAllSaleDetails();

        saleCustomerById.clear();
        saleDateById.clear();

        for (ChiTietBanVatTu detail : details) {
            String saleId = detail.getMaBanVatTu();
            saleCustomerById.computeIfAbsent(saleId, vatTuService::getCustomerBySaleId);
            saleDateById.computeIfAbsent(saleId, vatTuService::getSaleDateBySaleId);
        }

        tblSaleDetails.setItems(FXCollections.observableArrayList(details));
        saleDetailsLoaded = true;
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
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa. Vật tư có thể đang được dùng trong sửa chữa, nhập hoặc bán!");
            }
        }
    }

    private void handleSearch() {
        List<VatTuPhuTung> list = vatTuService.search(txtSearchPart.getText());
        tblParts.setItems(FXCollections.observableArrayList(list));
    }

    private void handleCreateImport() {
        Date ngayNhap = getSqlDate(dpNgayNhap, "Ngày nhập không được rỗng!");
        Integer soLuong = getPositiveInteger(txtImportSoLuong, "Số lượng nhập phải là số nguyên lớn hơn 0!");
        Double donGia = getNonNegativeDouble(txtImportDonGia, "Đơn giá nhập phải là số!");

        if (ngayNhap == null || soLuong == null || donGia == null) {
            return;
        }

        boolean result = vatTuService.createImportInvoice(
                txtMaNhapVatTu.getText().trim(),
                cbbMaNhaCungCap.getValue(),
                ngayNhap,
                cbbImportMaVatTu.getValue(),
                soLuong,
                donGia
        );

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Tạo phiếu nhập vật tư thành công!");
            refreshInventoryData();
            loadImportDetails();
            clearImportForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tạo phiếu nhập. Kiểm tra mã phiếu, nhà cung cấp, vật tư hoặc dữ liệu nhập!");
        }
    }

    private void handleCreateSale() {
        Date ngayBan = getSqlDate(dpNgayBan, "Ngày bán không được rỗng!");
        Integer soLuong = getPositiveInteger(txtSaleSoLuong, "Số lượng bán phải là số nguyên lớn hơn 0!");
        Double donGia = getNonNegativeDouble(txtSaleDonGia, "Đơn giá bán phải là số!");

        if (ngayBan == null || soLuong == null || donGia == null) {
            return;
        }

        boolean result = vatTuService.createSaleInvoice(
                txtMaBanVatTu.getText().trim(),
                cbbMaKhachHang.getValue(),
                ngayBan,
                cbbSaleMaVatTu.getValue(),
                soLuong,
                donGia
        );

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Tạo hóa đơn bán vật tư thành công!");
            refreshInventoryData();
            loadSaleDetails();
            clearSaleForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tạo hóa đơn bán. Kiểm tra mã hóa đơn, khách hàng, tồn kho hoặc dữ liệu nhập!");
        }
    }

    private VatTuPhuTung getFormData() {
        String maVatTu = txtMaVatTu.getText().trim();
        String tenVatTu = txtTenVatTu.getText().trim();
        String donViTinh = cbbDonViTinh.getValue();

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

        Integer soLuong = getNonNegativeInteger(txtSoLuongTon, "Số lượng tồn phải là số nguyên không âm!");
        Double donGia = getNonNegativeDouble(txtGiaNhap, "Đơn giá phải là số không âm!");

        if (soLuong == null || donGia == null) {
            return null;
        }

        return new VatTuPhuTung(maVatTu, tenVatTu, donGia, soLuong, donViTinh);
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

    private void fillImportPriceByPart() {
        VatTuPhuTung vt = vatTuService.getPartById(cbbImportMaVatTu.getValue());

        if (vt != null) {
            txtImportDonGia.setText(String.valueOf(vt.getDonGiaVatTuPhuTung()));
        }

        updateImportTotalLabel();
    }

    private void fillSalePriceByPart() {
        VatTuPhuTung vt = vatTuService.getPartById(cbbSaleMaVatTu.getValue());

        if (vt != null) {
            txtSaleDonGia.setText(String.valueOf(vt.getDonGiaVatTuPhuTung()));
        }

        updateSaleTotalLabel();
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

    private void clearImportForm() {
        txtMaNhapVatTu.clear();
        cbbMaNhaCungCap.setValue(null);
        dpNgayNhap.setValue(null);
        cbbImportMaVatTu.setValue(null);
        txtImportSoLuong.clear();
        txtImportDonGia.clear();
        updateImportTotalLabel();
    }

    private void clearSaleForm() {
        txtMaBanVatTu.clear();
        cbbMaKhachHang.setValue(null);
        dpNgayBan.setValue(null);
        cbbSaleMaVatTu.setValue(null);
        txtSaleSoLuong.clear();
        txtSaleDonGia.clear();
        updateSaleTotalLabel();
    }

    private void refreshAll() {
        refreshData();
    }

    private void refreshInventoryData() {
        loadTableData();
        loadSummaryCards();
        loadPartComboBoxes();
    }

    private void updateTotalLabels() {
        updateImportTotalLabel();
        updateSaleTotalLabel();
    }

    private void updateImportTotalLabel() {
        lblImportTotal.setText(formatMoney(calculateTotal(txtImportSoLuong, txtImportDonGia)));
    }

    private void updateSaleTotalLabel() {
        lblSaleTotal.setText(formatMoney(calculateTotal(txtSaleSoLuong, txtSaleDonGia)));
    }

    private double calculateTotal(TextField quantityField, TextField priceField) {
        try {
            String quantityText = quantityField.getText().trim();
            String priceText = priceField.getText().trim();

            if (quantityText.isEmpty() || priceText.isEmpty()) {
                return 0;
            }

            int quantity = Integer.parseInt(quantityText);
            double price = Double.parseDouble(priceText);

            if (quantity <= 0 || price < 0) {
                return 0;
            }

            return quantity * price;

        } catch (Exception e) {
            return 0;
        }
    }

    private Date getSqlDate(DatePicker picker, String message) {
        LocalDate localDate = picker.getValue();

        if (localDate == null) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", message);
            return null;
        }

        return Date.valueOf(localDate);
    }

    private Integer getPositiveInteger(TextField field, String message) {
        try {
            int value = Integer.parseInt(field.getText().trim());

            if (value <= 0) {
                showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", message);
                return null;
            }

            return value;

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", message);
            return null;
        }
    }

    private Integer getNonNegativeInteger(TextField field, String message) {
        try {
            int value = Integer.parseInt(field.getText().trim());

            if (value < 0) {
                showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", message);
                return null;
            }

            return value;

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", message);
            return null;
        }
    }

    private Double getNonNegativeDouble(TextField field, String message) {
        try {
            double value = Double.parseDouble(field.getText().trim());

            if (value < 0) {
                showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", message);
                return null;
            }

            return value;

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", message);
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
