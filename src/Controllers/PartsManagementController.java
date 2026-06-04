package Controllers;

import MODEL.ChiTietBanVatTu;
import MODEL.ChiTietNhapVatTu;
import MODEL.VatTuPhuTung;
import Service.VatTuPhuTungService;
import java.net.URL;
import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    private final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupComboBoxes();
        setupTableColumns();
        loadAllData();
        setupEvents();
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

        // Avoid database calls while rendering each TableView cell.
        colImportSupplier.setCellValueFactory(cellData -> new SimpleStringProperty(""));
        colImportDate.setCellValueFactory(cellData -> new SimpleStringProperty(""));

        colImportUnitPrice.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatMoney(cellData.getValue().getDonGia()))
        );

        colImportTotal.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatMoney(cellData.getValue().getThanhTien()))
        );

        colSaleId.setCellValueFactory(new PropertyValueFactory<>("maBanVatTu"));
        colSalePartId.setCellValueFactory(new PropertyValueFactory<>("maVatTuPhuTung"));
        colSaleQuantity.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        // Avoid database calls while rendering each TableView cell.
        colSaleCustomer.setCellValueFactory(cellData -> new SimpleStringProperty(""));
        colSaleDate.setCellValueFactory(cellData -> new SimpleStringProperty(""));

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
    }

    private void loadAllData() {
        loadTableData();
        loadSummaryCards();
        loadPartComboBoxes();
        tblImportDetails.setItems(FXCollections.observableArrayList());
        tblSaleDetails.setItems(FXCollections.observableArrayList());
    }

    private void loadTableData() {
        List<VatTuPhuTung> list = vatTuService.getAll();
        ObservableList<VatTuPhuTung> data = FXCollections.observableArrayList(list);
        tblParts.setItems(data);
    }

    private void loadImportDetails() {
        tblImportDetails.setItems(FXCollections.observableArrayList(vatTuService.getAllImportDetails()));
    }

    private void loadSaleDetails() {
        tblSaleDetails.setItems(FXCollections.observableArrayList(vatTuService.getAllSaleDetails()));
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
            showAlert(Alert.AlertType.INFORMATION, "ThÃ nh cÃ´ng", "ThÃªm váº­t tÆ° thÃ nh cÃ´ng!");
            refreshAll();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lá»—i", "KhÃ´ng thá»ƒ thÃªm. MÃ£ váº­t tÆ° cÃ³ thá»ƒ Ä‘Ã£ tá»“n táº¡i hoáº·c dá»¯ liá»‡u khÃ´ng há»£p lá»‡!");
        }
    }

    private void handleUpdate() {
        VatTuPhuTung selected = tblParts.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cáº£nh bÃ¡o", "Vui lÃ²ng chá»n váº­t tÆ° cáº§n sá»­a!");
            return;
        }

        VatTuPhuTung vt = getFormData();

        if (vt == null) {
            return;
        }

        boolean result = vatTuService.update(vt);

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "ThÃ nh cÃ´ng", "Cáº­p nháº­t váº­t tÆ° thÃ nh cÃ´ng!");
            refreshAll();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lá»—i", "KhÃ´ng thá»ƒ cáº­p nháº­t váº­t tÆ°!");
        }
    }

    private void handleDelete() {
        VatTuPhuTung selected = tblParts.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cáº£nh bÃ¡o", "Vui lÃ²ng chá»n váº­t tÆ° cáº§n xÃ³a!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("XÃ¡c nháº­n xÃ³a");
        confirm.setHeaderText(null);
        confirm.setContentText("Báº¡n cÃ³ cháº¯c muá»‘n xÃ³a váº­t tÆ°: " + selected.getMaVatTuPhuTung() + "?");

        Optional<ButtonType> option = confirm.showAndWait();

        if (option.isPresent() && option.get() == ButtonType.OK) {
            boolean result = vatTuService.delete(selected.getMaVatTuPhuTung());

            if (result) {
                showAlert(Alert.AlertType.INFORMATION, "ThÃ nh cÃ´ng", "XÃ³a váº­t tÆ° thÃ nh cÃ´ng!");
                refreshAll();
                clearForm();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lá»—i", "KhÃ´ng thá»ƒ xÃ³a. Váº­t tÆ° cÃ³ thá»ƒ Ä‘ang Ä‘Æ°á»£c dÃ¹ng trong sá»­a chá»¯a, nháº­p hoáº·c bÃ¡n!");
            }
        }
    }

    private void handleSearch() {
        List<VatTuPhuTung> list = vatTuService.search(txtSearchPart.getText());
        tblParts.setItems(FXCollections.observableArrayList(list));
    }

    private void handleCreateImport() {
        Date ngayNhap = getSqlDate(dpNgayNhap, "NgÃ y nháº­p khÃ´ng Ä‘Æ°á»£c rá»—ng!");
        Integer soLuong = getPositiveInteger(txtImportSoLuong, "Sá»‘ lÆ°á»£ng nháº­p pháº£i lÃ  sá»‘ nguyÃªn lá»›n hÆ¡n 0!");
        Double donGia = getNonNegativeDouble(txtImportDonGia, "ÄÆ¡n giÃ¡ nháº­p pháº£i lÃ  sá»‘!");

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
            showAlert(Alert.AlertType.INFORMATION, "ThÃ nh cÃ´ng", "Táº¡o phiáº¿u nháº­p váº­t tÆ° thÃ nh cÃ´ng!");
            refreshAll();
            clearImportForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lá»—i", "KhÃ´ng thá»ƒ táº¡o phiáº¿u nháº­p. Kiá»ƒm tra mÃ£ phiáº¿u, nhÃ  cung cáº¥p, váº­t tÆ° hoáº·c dá»¯ liá»‡u nháº­p!");
        }
    }

    private void handleCreateSale() {
        Date ngayBan = getSqlDate(dpNgayBan, "NgÃ y bÃ¡n khÃ´ng Ä‘Æ°á»£c rá»—ng!");
        Integer soLuong = getPositiveInteger(txtSaleSoLuong, "Sá»‘ lÆ°á»£ng bÃ¡n pháº£i lÃ  sá»‘ nguyÃªn lá»›n hÆ¡n 0!");
        Double donGia = getNonNegativeDouble(txtSaleDonGia, "ÄÆ¡n giÃ¡ bÃ¡n pháº£i lÃ  sá»‘!");

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
            showAlert(Alert.AlertType.INFORMATION, "ThÃ nh cÃ´ng", "Táº¡o hÃ³a Ä‘Æ¡n bÃ¡n váº­t tÆ° thÃ nh cÃ´ng!");
            refreshAll();
            clearSaleForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lá»—i", "KhÃ´ng thá»ƒ táº¡o hÃ³a Ä‘Æ¡n bÃ¡n. Kiá»ƒm tra mÃ£ hÃ³a Ä‘Æ¡n, khÃ¡ch hÃ ng, tá»“n kho hoáº·c dá»¯ liá»‡u nháº­p!");
        }
    }

    private VatTuPhuTung getFormData() {
        String maVatTu = txtMaVatTu.getText().trim();
        String tenVatTu = txtTenVatTu.getText().trim();
        String donViTinh = cbbDonViTinh.getValue();

        if (maVatTu.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiáº¿u dá»¯ liá»‡u", "MÃ£ váº­t tÆ° khÃ´ng Ä‘Æ°á»£c rá»—ng!");
            return null;
        }

        if (tenVatTu.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiáº¿u dá»¯ liá»‡u", "TÃªn váº­t tÆ° khÃ´ng Ä‘Æ°á»£c rá»—ng!");
            return null;
        }

        if (donViTinh == null || donViTinh.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiáº¿u dá»¯ liá»‡u", "Vui lÃ²ng chá»n Ä‘Æ¡n vá»‹ tÃ­nh!");
            return null;
        }

        Integer soLuong = getNonNegativeInteger(txtSoLuongTon, "Sá»‘ lÆ°á»£ng tá»“n pháº£i lÃ  sá»‘ nguyÃªn khÃ´ng Ã¢m!");
        Double donGia = getNonNegativeDouble(txtGiaNhap, "ÄÆ¡n giÃ¡ pháº£i lÃ  sá»‘ khÃ´ng Ã¢m!");

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
        loadAllData();
        updateTotalLabels();
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
            showAlert(Alert.AlertType.WARNING, "Thiáº¿u dá»¯ liá»‡u", message);
            return null;
        }

        return Date.valueOf(localDate);
    }

    private Integer getPositiveInteger(TextField field, String message) {
        try {
            int value = Integer.parseInt(field.getText().trim());

            if (value <= 0) {
                showAlert(Alert.AlertType.WARNING, "Sai dá»¯ liá»‡u", message);
                return null;
            }

            return value;

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Sai dá»¯ liá»‡u", message);
            return null;
        }
    }

    private Integer getNonNegativeInteger(TextField field, String message) {
        try {
            int value = Integer.parseInt(field.getText().trim());

            if (value < 0) {
                showAlert(Alert.AlertType.WARNING, "Sai dá»¯ liá»‡u", message);
                return null;
            }

            return value;

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Sai dá»¯ liá»‡u", message);
            return null;
        }
    }

    private Double getNonNegativeDouble(TextField field, String message) {
        try {
            double value = Double.parseDouble(field.getText().trim());

            if (value < 0) {
                showAlert(Alert.AlertType.WARNING, "Sai dá»¯ liá»‡u", message);
                return null;
            }

            return value;

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Sai dá»¯ liá»‡u", message);
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
