package Controllers;

import MODEL.ChiTietBanVatTu;
import MODEL.ChiTietNhapVatTu;
import MODEL.NhaCungCap;
import MODEL.VatTuPhuTung;
import Service.DataRefreshService;
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
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
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
    @FXML private Tab tabImport;
    @FXML private Tab tabSale;

    @FXML private TextField txtMaVatTu;
    @FXML private TextField txtTenVatTu;
    @FXML private ComboBox<String> cbbDonViTinh;
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
    private final Map<String, VatTuPhuTung> partById = new HashMap<>();
    private final Map<String, String> partNameToId = new HashMap<>();
    private final Map<String, String> partIdToName = new HashMap<>();
    private final Map<String, String> supplierNameToId = new HashMap<>();
    private final Map<String, String> supplierIdToName = new HashMap<>();
    private final Map<String, String> importSupplierById = new HashMap<>();
    private final Map<String, String> importDateById = new HashMap<>();
    private final Map<String, String> saleCustomerById = new HashMap<>();
    private final Map<String, String> saleDateById = new HashMap<>();

    private final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private boolean importDetailsLoaded = false;
    private boolean saleDetailsLoaded = false;
    private boolean importDetailsLoading = false;
    private boolean saleDetailsLoading = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupComboBoxes();
        setupTableColumns();
        restrictDatePickersToToday();
        setupEvents();
        refreshInventoryData();
        updateTotalLabels();
        tblImportDetails.setItems(FXCollections.observableArrayList());
        tblSaleDetails.setItems(FXCollections.observableArrayList());
    }

    @Override
    public void refreshData() {
        refreshInventoryDataAsync();
    }

    private void setupComboBoxes() {
        cbbDonViTinh.getItems().setAll("Cái", "Bộ", "Chiếc", "Lít", "Hộp", "Chai", "Cặp", "Mét");
        applySupplierComboBox(vatTuService.getAllSuppliers());
        cbbMaKhachHang.getItems().setAll(vatTuService.getAllCustomerIds());
    }

    private void setupTableColumns() {
        colMaVatTu.setCellValueFactory(new PropertyValueFactory<>("maVatTuPhuTung"));
        colTenVatTu.setCellValueFactory(new PropertyValueFactory<>("tenVatTuPhuTung"));
        colDonViTinh.setCellValueFactory(new PropertyValueFactory<>("donViTinh"));
        colSoLuongTon.setCellValueFactory(new PropertyValueFactory<>("soLuongVatTuPhuTung"));
        colGiaNhap.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatMoney(cellData.getValue().getDonGiaVatTuPhuTung()))
        );

        colImportId.setCellValueFactory(new PropertyValueFactory<>("maNhapVatTu"));
        colImportSupplier.setCellValueFactory(cellData ->
                new SimpleStringProperty(toSupplierName(importSupplierById.getOrDefault(cellData.getValue().getMaNhapVatTu(), "")))
        );
        colImportDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(importDateById.getOrDefault(cellData.getValue().getMaNhapVatTu(), ""))
        );
        colImportPartId.setCellValueFactory(cellData ->
                new SimpleStringProperty(toPartName(cellData.getValue().getMaVatTuPhuTung()))
        );
        colImportQuantity.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colImportUnitPrice.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatMoney(cellData.getValue().getDonGia()))
        );
        colImportTotal.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatMoney(cellData.getValue().getThanhTien()))
        );

        colSaleId.setCellValueFactory(new PropertyValueFactory<>("maBanVatTu"));
        colSaleCustomer.setCellValueFactory(cellData ->
                new SimpleStringProperty(saleCustomerById.getOrDefault(cellData.getValue().getMaBanVatTu(), ""))
        );
        colSaleDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(saleDateById.getOrDefault(cellData.getValue().getMaBanVatTu(), ""))
        );
        colSalePartId.setCellValueFactory(cellData ->
                new SimpleStringProperty(toPartName(cellData.getValue().getMaVatTuPhuTung()))
        );
        colSaleQuantity.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
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
            refreshInventoryDataAsync();
        });

        btnCreateImport.setOnAction(e -> handleCreateImportAsync());
        btnClearImport.setOnAction(e -> clearImportForm());
        btnCreateSale.setOnAction(e -> handleCreateSaleAsync());
        btnClearSale.setOnAction(e -> clearSaleForm());

        tblParts.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, selectedItem) -> {
                    if (selectedItem != null) {
                        fillForm(selectedItem);
                    }
                }
        );

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

    private void refreshInventoryData() {
        InventoryData data = loadInventoryData();
        applyInventoryData(data);
    }

    private void refreshInventoryDataAsync() {
        Task<InventoryData> task = new Task<>() {
            @Override
            protected InventoryData call() {
                return loadInventoryData();
            }
        };

        task.setOnSucceeded(e -> {
            applyInventoryData(task.getValue());
            Tab selectedTab = partsTabPane.getSelectionModel().getSelectedItem();
            if (selectedTab == tabImport) {
                importDetailsLoaded = false;
                loadImportDetailsAsync();
            } else if (selectedTab == tabSale) {
                saleDetailsLoaded = false;
                loadSaleDetailsAsync();
            }
        });

        task.setOnFailed(e -> showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải lại dữ liệu vật tư."));
        startBackgroundTask(task);
    }

    private InventoryData loadInventoryData() {
        List<VatTuPhuTung> parts = vatTuService.getAll();

        return new InventoryData(
                parts,
                vatTuService.countAll(),
                vatTuService.countLowStock(),
                vatTuService.getInventoryValue(),
                vatTuService.getAllSuppliers(),
                vatTuService.getAllCustomerIds()
        );
    }

    private ImportDetailsData loadImportDetailsData() {
        List<ChiTietNhapVatTu> details = vatTuService.getAllImportDetails();
        Map<String, String> suppliers = new HashMap<>();
        Map<String, String> dates = new HashMap<>();

        for (ChiTietNhapVatTu detail : details) {
            String importId = detail.getMaNhapVatTu();
            suppliers.computeIfAbsent(importId, vatTuService::getSupplierByImportId);
            dates.computeIfAbsent(importId, vatTuService::getImportDateByImportId);
        }

        return new ImportDetailsData(details, suppliers, dates);
    }

    private SaleDetailsData loadSaleDetailsData() {
        List<ChiTietBanVatTu> details = vatTuService.getAllSaleDetails();
        Map<String, String> customers = new HashMap<>();
        Map<String, String> dates = new HashMap<>();

        for (ChiTietBanVatTu detail : details) {
            String saleId = detail.getMaBanVatTu();
            customers.computeIfAbsent(saleId, vatTuService::getCustomerBySaleId);
            dates.computeIfAbsent(saleId, vatTuService::getSaleDateBySaleId);
        }

        return new SaleDetailsData(details, customers, dates);
    }

    private void applyInventoryData(InventoryData data) {
        String selectedImportPartId = getSelectedImportPartId();
        String selectedSalePartId = getSelectedSalePartId();
        String selectedSupplierId = getSelectedSupplierId();

        partById.clear();

        for (VatTuPhuTung part : data.parts()) {
            partById.put(part.getMaVatTuPhuTung().trim(), part);
        }

        tblParts.setItems(FXCollections.observableArrayList(data.parts()));
        lblTotalParts.setText(String.valueOf(data.totalParts()));
        lblLowStock.setText(String.valueOf(data.lowStock()));
        lblInventoryValue.setText(formatMoney(data.inventoryValue()));
        applyPartComboBoxes(data.parts());
        applySupplierComboBox(data.suppliers());
        cbbMaKhachHang.getItems().setAll(data.customerIds());
        cbbImportMaVatTu.setValue(partIdToName.get(selectedImportPartId));
        cbbSaleMaVatTu.setValue(partIdToName.get(selectedSalePartId));
        cbbMaNhaCungCap.setValue(supplierIdToName.get(selectedSupplierId));
    }

    private void applyImportDetails(ImportDetailsData data) {
        importSupplierById.clear();
        importSupplierById.putAll(data.suppliers());
        importDateById.clear();
        importDateById.putAll(data.dates());
        tblImportDetails.setItems(FXCollections.observableArrayList(data.details()));
        importDetailsLoaded = true;
    }

    private void applySaleDetails(SaleDetailsData data) {
        saleCustomerById.clear();
        saleCustomerById.putAll(data.customers());
        saleDateById.clear();
        saleDateById.putAll(data.dates());
        tblSaleDetails.setItems(FXCollections.observableArrayList(data.details()));
        saleDetailsLoaded = true;
    }

    private void loadDetailsForTab(Tab selectedTab) {
        if (selectedTab == tabImport && !importDetailsLoaded && !importDetailsLoading) {
            loadImportDetailsAsync();
        } else if (selectedTab == tabSale && !saleDetailsLoaded && !saleDetailsLoading) {
            loadSaleDetailsAsync();
        }
    }

    private void loadImportDetailsAsync() {
        importDetailsLoading = true;
        Task<ImportDetailsData> task = new Task<>() {
            @Override
            protected ImportDetailsData call() {
                return loadImportDetailsData();
            }
        };

        task.setOnSucceeded(e -> {
            importDetailsLoading = false;
            applyImportDetails(task.getValue());
        });

        task.setOnFailed(e -> {
            importDetailsLoading = false;
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải chi tiết phiếu nhập vật tư.");
        });

        startBackgroundTask(task);
    }

    private void loadSaleDetailsAsync() {
        saleDetailsLoading = true;
        Task<SaleDetailsData> task = new Task<>() {
            @Override
            protected SaleDetailsData call() {
                return loadSaleDetailsData();
            }
        };

        task.setOnSucceeded(e -> {
            saleDetailsLoading = false;
            applySaleDetails(task.getValue());
        });

        task.setOnFailed(e -> {
            saleDetailsLoading = false;
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải chi tiết hóa đơn bán vật tư.");
        });

        startBackgroundTask(task);
    }

    private void handleAdd() {
        VatTuPhuTung vt = getFormData();

        if (vt == null) {
            return;
        }

        boolean result = vatTuService.add(vt);

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm vật tư thành công. Tồn kho và đơn giá sẽ được cập nhật khi lập phiếu nhập.");
            refreshInventoryDataAsync();
            clearForm();
            markPartsDataChanged();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm. Mã vật tư có thể đã tồn tại hoặc dữ liệu không hợp lệ.");
        }
    }

    private void handleUpdate() {
        VatTuPhuTung selected = tblParts.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn vật tư cần sửa.");
            return;
        }

        VatTuPhuTung vt = getFormData();

        if (vt == null) {
            return;
        }

        boolean result = vatTuService.update(vt);

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật thông tin vật tư thành công.");
            refreshInventoryDataAsync();
            clearForm();
            markPartsDataChanged();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật vật tư.");
        }
    }

    private void handleDelete() {
        VatTuPhuTung selected = tblParts.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn vật tư cần xóa.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc muốn xóa vật tư " + selected.getMaVatTuPhuTung() + "?");

        Optional<ButtonType> option = confirm.showAndWait();

        if (option.isPresent() && option.get() == ButtonType.OK) {
            boolean result = vatTuService.delete(selected.getMaVatTuPhuTung());

            if (result) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa vật tư thành công.");
                refreshInventoryDataAsync();
                clearForm();
                markPartsDataChanged();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa. Vật tư có thể đang được dùng trong sửa chữa, nhập hoặc bán.");
            }
        }
    }

    private void handleSearch() {
        List<VatTuPhuTung> list = vatTuService.search(txtSearchPart.getText());
        tblParts.setItems(FXCollections.observableArrayList(list));
    }

    private void handleCreateImportAsync() {
        Date ngayNhap = getSqlDate(dpNgayNhap, "Ngày nhập không được rỗng.");
        Integer soLuong = getPositiveInteger(txtImportSoLuong, "Số lượng nhập phải là số nguyên lớn hơn 0.");
        Double donGia = getNonNegativeDouble(txtImportDonGia, "Đơn giá nhập phải là số không âm.");

        if (ngayNhap == null || soLuong == null || donGia == null) {
            return;
        }

        String maNhap = txtMaNhapVatTu.getText().trim();
        String maNhaCungCap = getSelectedSupplierId();
        String maVatTu = getSelectedImportPartId();

        setImportActionsDisabled(true);

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() {
                return vatTuService.createImportInvoice(
                        maNhap,
                        maNhaCungCap,
                        ngayNhap,
                        maVatTu,
                        soLuong,
                        donGia
                );
            }
        };

        task.setOnSucceeded(e -> {
            setImportActionsDisabled(false);
            if (task.getValue()) {
                clearImportForm();
                importDetailsLoaded = false;
                refreshInventoryDataAsync();
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Tạo phiếu nhập vật tư thành công.");
                markPartsDataChanged();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tạo phiếu nhập. Kiểm tra mã phiếu, nhà cung cấp, vật tư hoặc dữ liệu nhập.");
            }
        });

        task.setOnFailed(e -> {
            setImportActionsDisabled(false);
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tạo phiếu nhập vật tư.");
        });

        startBackgroundTask(task);
    }

    private void handleCreateSaleAsync() {
        Date ngayBan = getSqlDate(dpNgayBan, "Ngày bán không được rỗng.");
        Integer soLuong = getPositiveInteger(txtSaleSoLuong, "Số lượng bán phải là số nguyên lớn hơn 0.");
        Double donGia = getNonNegativeDouble(txtSaleDonGia, "Đơn giá bán phải là số không âm.");

        if (ngayBan == null || soLuong == null || donGia == null) {
            return;
        }

        String maBan = txtMaBanVatTu.getText().trim();
        String maKhachHang = cbbMaKhachHang.getValue();
        String maVatTu = getSelectedSalePartId();

        setSaleActionsDisabled(true);

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() {
                return vatTuService.createSaleInvoice(
                        maBan,
                        maKhachHang,
                        ngayBan,
                        maVatTu,
                        soLuong,
                        donGia
                );
            }
        };

        task.setOnSucceeded(e -> {
            setSaleActionsDisabled(false);
            if (task.getValue()) {
                clearSaleForm();
                saleDetailsLoaded = false;
                refreshInventoryDataAsync();
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Tạo hóa đơn bán vật tư thành công.");
                markPartsDataChanged();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tạo hóa đơn bán. Kiểm tra mã hóa đơn, khách hàng, tồn kho hoặc dữ liệu nhập.");
            }
        });

        task.setOnFailed(e -> {
            setSaleActionsDisabled(false);
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tạo hóa đơn bán vật tư.");
        });

        startBackgroundTask(task);
    }

    private VatTuPhuTung getFormData() {
        String maVatTu = txtMaVatTu.getText().trim();
        String tenVatTu = txtTenVatTu.getText().trim();
        String donViTinh = cbbDonViTinh.getValue();

        if (maVatTu.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Mã vật tư không được rỗng.");
            return null;
        }

        if (tenVatTu.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Tên vật tư không được rỗng.");
            return null;
        }

        if (donViTinh == null || donViTinh.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Vui lòng chọn đơn vị tính.");
            return null;
        }

        return new VatTuPhuTung(maVatTu, tenVatTu, 0, 0, donViTinh);
    }

    private void fillForm(VatTuPhuTung vt) {
        txtMaVatTu.setText(vt.getMaVatTuPhuTung());
        txtMaVatTu.setDisable(true);
        txtTenVatTu.setText(vt.getTenVatTuPhuTung());
        cbbDonViTinh.setValue(vt.getDonViTinh());
    }

    private void fillImportPriceByPart() {
        VatTuPhuTung vt = partById.get(getSelectedImportPartId());

        if (vt == null) {
            vt = vatTuService.getPartById(getSelectedImportPartId());
        }

        if (vt != null) {
            txtImportDonGia.setText(String.valueOf(vt.getDonGiaVatTuPhuTung()));
        }

        updateImportTotalLabel();
    }

    private void fillSalePriceByPart() {
        VatTuPhuTung vt = partById.get(getSelectedSalePartId());

        if (vt == null) {
            vt = vatTuService.getPartById(getSelectedSalePartId());
        }

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
        tblParts.getSelectionModel().clearSelection();
    }

    private void clearImportForm() {
        txtMaNhapVatTu.clear();
        cbbMaNhaCungCap.setValue(null);
        dpNgayNhap.setValue(LocalDate.now());
        cbbImportMaVatTu.setValue(null);
        txtImportSoLuong.clear();
        txtImportDonGia.clear();
        updateImportTotalLabel();
    }

    private void clearSaleForm() {
        txtMaBanVatTu.clear();
        cbbMaKhachHang.setValue(null);
        dpNgayBan.setValue(LocalDate.now());
        cbbSaleMaVatTu.setValue(null);
        txtSaleSoLuong.clear();
        txtSaleDonGia.clear();
        updateSaleTotalLabel();
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

        if (!isToday(localDate)) {
            showAlert(Alert.AlertType.WARNING, "Sai ngày", "Ngày lập phiếu chỉ được chọn ngày hôm nay.");
            return null;
        }

        return Date.valueOf(localDate);
    }

    private void restrictDatePickersToToday() {
        restrictDatePickerToToday(dpNgayNhap);
        restrictDatePickerToToday(dpNgayBan);
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

    private void setImportActionsDisabled(boolean disabled) {
        btnCreateImport.setDisable(disabled);
        btnClearImport.setDisable(disabled);
    }

    private void setSaleActionsDisabled(boolean disabled) {
        btnCreateSale.setDisable(disabled);
        btnClearSale.setDisable(disabled);
    }

    private void startBackgroundTask(Task<?> task) {
        Thread thread = new Thread(task, "parts-task");
        thread.setDaemon(true);
        thread.start();
    }

    private void markPartsDataChanged() {
        DataRefreshService.markDirty(
                DataRefreshService.DASHBOARD,
                DataRefreshService.REPAIR,
                DataRefreshService.BILLING,
                DataRefreshService.LOOKUP,
                DataRefreshService.REPORTS
        );
    }

    private void applyPartComboBoxes(List<VatTuPhuTung> parts) {
        partNameToId.clear();
        partIdToName.clear();

        ObservableList<String> partNames = FXCollections.observableArrayList();

        for (VatTuPhuTung part : parts) {
            String id = part.getMaVatTuPhuTung().trim();
            String name = part.getTenVatTuPhuTung().trim();
            partNameToId.put(name, id);
            partIdToName.put(id, name);
            partNames.add(name);
        }

        cbbImportMaVatTu.setItems(FXCollections.observableArrayList(partNames));
        cbbSaleMaVatTu.setItems(FXCollections.observableArrayList(partNames));
    }

    private void applySupplierComboBox(List<NhaCungCap> suppliers) {
        supplierNameToId.clear();
        supplierIdToName.clear();

        ObservableList<String> supplierNames = FXCollections.observableArrayList();

        for (NhaCungCap supplier : suppliers) {
            String id = supplier.getMaNhaCungCap().trim();
            String name = supplier.getTenNhaCungCap().trim();
            supplierNameToId.put(name, id);
            supplierIdToName.put(id, name);
            supplierNames.add(name);
        }

        cbbMaNhaCungCap.setItems(supplierNames);
    }

    private String getSelectedImportPartId() {
        return toPartId(cbbImportMaVatTu.getValue());
    }

    private String getSelectedSalePartId() {
        return toPartId(cbbSaleMaVatTu.getValue());
    }

    private String getSelectedSupplierId() {
        String value = cbbMaNhaCungCap.getValue();
        return value == null ? null : supplierNameToId.getOrDefault(value, value).trim();
    }

    private String toPartId(String value) {
        return value == null ? null : partNameToId.getOrDefault(value, value).trim();
    }

    private String toPartName(String id) {
        if (id == null) {
            return "";
        }

        String key = id.trim();
        return partIdToName.getOrDefault(key, key);
    }

    private String toSupplierName(String id) {
        if (id == null) {
            return "";
        }

        String key = id.trim();
        return supplierIdToName.getOrDefault(key, key);
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

    private record InventoryData(
            List<VatTuPhuTung> parts,
            int totalParts,
            int lowStock,
            double inventoryValue,
            List<NhaCungCap> suppliers,
            List<String> customerIds
    ) {
    }

    private record ImportDetailsData(
            List<ChiTietNhapVatTu> details,
            Map<String, String> suppliers,
            Map<String, String> dates
    ) {
    }

    private record SaleDetailsData(
            List<ChiTietBanVatTu> details,
            Map<String, String> customers,
            Map<String, String> dates
    ) {
    }
}
