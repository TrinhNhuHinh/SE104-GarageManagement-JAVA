package Controllers;

import MODEL.ChiTietSuaChuaXe;
import MODEL.SuaChuaXe;
import MODEL.TiepNhanXe;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class RepairOrdersController implements Initializable, Refreshable {

    private static final String NO_PART_OPTION = "Không dùng vật tư";

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
    @FXML private Button btnAddDetail;
    @FXML private Button btnDeleteDetail;

    @FXML private TextField txtSearchRepair;
    @FXML private Button btnSearchRepair;
    @FXML private Button btnRefreshRepair;

    @FXML private TableView<SuaChuaXe> tblRepairOrders;
    @FXML private TableColumn<SuaChuaXe, String> colMaSuaChua;
    @FXML private TableColumn<SuaChuaXe, String> colMaTiepNhanXe;
    @FXML private TableColumn<SuaChuaXe, String> colBienSoXe;
    @FXML private TableColumn<SuaChuaXe, Date> colNgaySuaChua;
    @FXML private TableColumn<SuaChuaXe, Double> colThanhTien;

    @FXML private TableView<ChiTietSuaChuaXe> tblRepairDetails;
    @FXML private TableColumn<ChiTietSuaChuaXe, String> colDetailContent;
    @FXML private TableColumn<ChiTietSuaChuaXe, String> colDetailPart;
    @FXML private TableColumn<ChiTietSuaChuaXe, Integer> colDetailQuantity;
    @FXML private TableColumn<ChiTietSuaChuaXe, Double> colDetailUnitPrice;
    @FXML private TableColumn<ChiTietSuaChuaXe, String> colDetailLabor;
    @FXML private TableColumn<ChiTietSuaChuaXe, Double> colDetailTotal;

    private final SuaChuaXeService suaChuaService = new SuaChuaXeService();
    private final Map<String, String> partNameToId = new HashMap<>();
    private final Map<String, String> partIdToName = new HashMap<>();
    private final Map<String, String> intakePlateById = new HashMap<>();
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
        colBienSoXe.setCellValueFactory(cellData -> new SimpleStringProperty(
                intakePlateById.getOrDefault(
                        safeTrim(cellData.getValue().getMaTiepNhanXe()),
                        ""
                )
        ));
        colNgaySuaChua.setCellValueFactory(new PropertyValueFactory<>("ngaySuaChua"));
        colThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));

        colDetailContent.setCellValueFactory(new PropertyValueFactory<>("noiDung"));
        colDetailPart.setCellValueFactory(cellData -> {
            String partId = safeTrim(cellData.getValue().getMaVatTuPhuTung());
            return new SimpleStringProperty(partIdToName.getOrDefault(partId, partId.isEmpty() ? NO_PART_OPTION : partId));
        });
        colDetailQuantity.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colDetailUnitPrice.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        colDetailLabor.setCellValueFactory(new PropertyValueFactory<>("maTienCong"));
        colDetailTotal.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
    }

    private void loadComboBoxes() {
        String selectedIntake = cbbMaTiepNhanXe.getValue();
        String selectedPartId = getSelectedPartId();
        String selectedLabor = cbbMaTienCong.getValue();

        List<TiepNhanXe> intakes = suaChuaService.getAllIntakes();
        List<VatTuPhuTung> parts = suaChuaService.getAllParts();
        List<String> laborIds = suaChuaService.getAllLaborIds();

        applyIntakeComboBox(intakes);
        applyPartComboBox(parts);
        cbbMaTienCong.getItems().setAll(laborIds);

        cbbMaTiepNhanXe.setValue(cbbMaTiepNhanXe.getItems().contains(selectedIntake) ? selectedIntake : null);
        cbbMaVatTu.setValue(partIdToName.getOrDefault(selectedPartId, NO_PART_OPTION));
        cbbMaTienCong.setValue(laborIds.contains(selectedLabor) ? selectedLabor : null);
    }

    private void loadTableData() {
        List<SuaChuaXe> list = suaChuaService.getAll();
        tblRepairOrders.setItems(FXCollections.observableArrayList(list));
        tblRepairDetails.setItems(FXCollections.observableArrayList());
    }

    private void setupEvents() {
        btnAddRepair.setOnAction(e -> handleAdd());
        btnUpdateRepair.setOnAction(e -> handleUpdate());
        btnDeleteRepair.setOnAction(e -> handleDelete());
        btnClearRepair.setOnAction(e -> clearForm());
        btnAddDetail.setOnAction(e -> handleAddDetail());
        btnDeleteDetail.setOnAction(e -> handleDeleteDetail());
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

        tblRepairDetails.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldValue, selected) -> {
                    if (selected != null) {
                        fillDetailForm(selected);
                    }
                }
        );
    }

    private void handleAdd() {
        Date ngaySuaChua = getRepairDate();
        RepairDetailInput detail = getDetailInput();

        if (ngaySuaChua == null || detail == null) {
            return;
        }

        boolean result = suaChuaService.add(
                safeTrim(txtMaSuaChua.getText()),
                cbbMaTiepNhanXe.getValue(),
                ngaySuaChua,
                detail.partId(),
                detail.quantity(),
                detail.laborId(),
                detail.content()
        );

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Tạo phiếu sửa chữa thành công.");
            loadComboBoxes();
            loadTableData();
            clearForm();
            markRepairDataChanged();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tạo phiếu. Xe này có thể còn phiếu sửa chữa chưa thu tiền xong, hoặc dữ liệu nhập chưa hợp lệ.");
        }
    }

    private void handleUpdate() {
        SuaChuaXe selected = tblRepairOrders.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn phiếu cần cập nhật.");
            return;
        }

        Date ngaySuaChua = getRepairDate();

        if (ngaySuaChua == null) {
            return;
        }

        boolean result = suaChuaService.updateHeader(
                selected.getMaSuaChuaXe(),
                selected.getMaTiepNhanXe(),
                ngaySuaChua
        );

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật thông tin phiếu sửa chữa thành công.");
            loadComboBoxes();
            loadTableData();
            clearForm();
            markRepairDataChanged();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật phiếu sửa chữa.");
        }
    }

    private void handleDelete() {
        SuaChuaXe selected = tblRepairOrders.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn phiếu cần xóa.");
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
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa phiếu sửa chữa thành công.");
                loadComboBoxes();
                loadTableData();
                clearForm();
                markRepairDataChanged();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa phiếu đã có thu tiền hoặc đang bị dữ liệu khác tham chiếu.");
            }
        }
    }

    private void handleAddDetail() {
        SuaChuaXe selected = tblRepairOrders.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn phiếu sửa chữa trước khi thêm chi tiết.");
            return;
        }

        RepairDetailInput detail = getDetailInput();

        if (detail == null) {
            return;
        }

        boolean result = suaChuaService.addDetail(
                selected.getMaSuaChuaXe(),
                detail.partId(),
                detail.quantity(),
                detail.laborId(),
                detail.content()
        );

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm chi tiết sửa chữa thành công.");
            loadComboBoxes();
            loadTableData();
            selectRepair(selected.getMaSuaChuaXe());
            clearDetailForm();
            markRepairDataChanged();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm chi tiết. Kiểm tra tồn kho hoặc phiếu đã được thu tiền.");
        }
    }

    private void handleDeleteDetail() {
        ChiTietSuaChuaXe selectedDetail = tblRepairDetails.getSelectionModel().getSelectedItem();
        SuaChuaXe selectedRepair = tblRepairOrders.getSelectionModel().getSelectedItem();

        if (selectedRepair == null || selectedDetail == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn dòng chi tiết cần xóa.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc muốn xóa dòng chi tiết đang chọn?");

        Optional<ButtonType> option = confirm.showAndWait();

        if (option.isPresent() && option.get() == ButtonType.OK) {
            boolean result = suaChuaService.deleteDetail(selectedDetail.getMaChiTietSuaChuaXe());

            if (result) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa chi tiết sửa chữa thành công.");
                loadComboBoxes();
                loadTableData();
                selectRepair(selectedRepair.getMaSuaChuaXe());
                clearDetailForm();
                markRepairDataChanged();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa chi tiết của phiếu đã có thu tiền.");
            }
        }
    }

    private void handleSearch() {
        List<SuaChuaXe> list = suaChuaService.search(txtSearchRepair.getText());
        tblRepairOrders.setItems(FXCollections.observableArrayList(list));
        tblRepairDetails.setItems(FXCollections.observableArrayList());
    }

    private void fillForm(SuaChuaXe sc) {
        txtMaSuaChua.setDisable(true);
        txtMaSuaChua.setText(safeTrim(sc.getMaSuaChuaXe()));

        cbbMaTiepNhanXe.setDisable(true);
        cbbMaTiepNhanXe.setValue(safeTrim(sc.getMaTiepNhanXe()));

        if (sc.getNgaySuaChua() != null) {
            dpNgaySuaChua.setValue(sc.getNgaySuaChua().toLocalDate());
        } else {
            dpNgaySuaChua.setValue(null);
        }

        loadRepairDetails(sc.getMaSuaChuaXe());
    }

    private void loadRepairDetails(String repairId) {
        List<ChiTietSuaChuaXe> details = suaChuaService.getDetailsByRepairId(repairId);
        tblRepairDetails.setItems(FXCollections.observableArrayList(details));

        if (details.isEmpty()) {
            clearDetailForm();
        } else {
            tblRepairDetails.getSelectionModel().selectFirst();
        }
    }

    private void fillDetailForm(ChiTietSuaChuaXe ct) {
        txtNoiDungSuaChua.setText(safeTrim(ct.getNoiDung()));

        String partId = safeTrim(ct.getMaVatTuPhuTung());
        cbbMaVatTu.setValue(partId.isEmpty() ? NO_PART_OPTION : partIdToName.getOrDefault(partId, partId));
        txtSoLuong.setText(String.valueOf(ct.getSoLuong()));
        cbbMaTienCong.setValue(safeTrim(ct.getMaTienCong()));
    }

    private void clearForm() {
        txtMaSuaChua.setDisable(false);
        txtMaSuaChua.clear();
        cbbMaTiepNhanXe.setDisable(false);
        cbbMaTiepNhanXe.setValue(null);
        dpNgaySuaChua.setValue(LocalDate.now());
        clearDetailForm();
        txtSearchRepair.clear();
        tblRepairOrders.getSelectionModel().clearSelection();
        tblRepairDetails.setItems(FXCollections.observableArrayList());
    }

    private void clearDetailForm() {
        cbbMaVatTu.setValue(NO_PART_OPTION);
        txtSoLuong.clear();
        cbbMaTienCong.setValue(null);
        txtNoiDungSuaChua.clear();
        tblRepairDetails.getSelectionModel().clearSelection();
    }

    private Date getRepairDate() {
        LocalDate localDate = dpNgaySuaChua.getValue();

        if (localDate == null) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Ngày sửa chữa không được rỗng.");
            return null;
        }

        if (!isToday(localDate)) {
            showAlert(Alert.AlertType.WARNING, "Sai ngày", "Ngày sửa chữa chỉ được chọn ngày hôm nay.");
            return null;
        }

        return Date.valueOf(localDate);
    }

    private RepairDetailInput getDetailInput() {
        String partId = getSelectedPartId();
        String laborId = cbbMaTienCong.getValue();
        String content = safeTrim(txtNoiDungSuaChua.getText());
        Integer quantity = getQuantity(partId);

        if (quantity == null) {
            return null;
        }

        if (laborId == null || laborId.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Vui lòng chọn tiền công.");
            return null;
        }

        if (content.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu dữ liệu", "Nội dung sửa chữa không được rỗng.");
            return null;
        }

        return new RepairDetailInput(partId, quantity, laborId.trim(), content);
    }

    private Integer getQuantity(String partId) {
        String text = safeTrim(txtSoLuong.getText());

        if (partId == null || partId.isEmpty()) {
            if (text.isEmpty()) {
                return 0;
            }

            try {
                int value = Integer.parseInt(text);

                if (value != 0) {
                    showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Không dùng vật tư thì số lượng phải để trống hoặc bằng 0.");
                    return null;
                }

                return 0;
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Số lượng phải là số nguyên.");
                return null;
            }
        }

        try {
            int value = Integer.parseInt(text);

            if (value <= 0) {
                showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Số lượng vật tư phải lớn hơn 0.");
                return null;
            }

            return value;
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Số lượng phải là số nguyên.");
            return null;
        }
    }

    private void selectRepair(String repairId) {
        for (SuaChuaXe repair : tblRepairOrders.getItems()) {
            if (safeTrim(repair.getMaSuaChuaXe()).equalsIgnoreCase(safeTrim(repairId))) {
                tblRepairOrders.getSelectionModel().select(repair);
                tblRepairOrders.scrollTo(repair);
                return;
            }
        }
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
        String selectedRepairId = getSelectedRepairId();

        Task<RepairRefreshData> task = new Task<>() {
            @Override
            protected RepairRefreshData call() {
                return new RepairRefreshData(
                        suaChuaService.getAllIntakes(),
                        suaChuaService.getAllParts(),
                        suaChuaService.getAllLaborIds(),
                        suaChuaService.getAll()
                );
            }
        };

        task.setOnSucceeded(e -> {
            refreshing = false;
            RepairRefreshData data = task.getValue();

            applyIntakeComboBox(data.intakes());
            applyPartComboBox(data.parts());
            cbbMaTienCong.getItems().setAll(data.laborIds());
            cbbMaTiepNhanXe.setValue(cbbMaTiepNhanXe.getItems().contains(selectedIntake) ? selectedIntake : null);
            cbbMaVatTu.setValue(partIdToName.getOrDefault(selectedPartId, NO_PART_OPTION));
            cbbMaTienCong.setValue(data.laborIds().contains(selectedLabor) ? selectedLabor : null);
            tblRepairOrders.setItems(FXCollections.observableArrayList(data.repairs()));
            selectRepair(selectedRepairId);
        });

        task.setOnFailed(e -> refreshing = false);

        Thread thread = new Thread(task, "repair-refresh");
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

    private void applyIntakeComboBox(List<TiepNhanXe> intakes) {
        intakePlateById.clear();
        cbbMaTiepNhanXe.getItems().clear();

        for (TiepNhanXe intake : intakes) {
            String id = safeTrim(intake.getMaTiepNhanXe());
            intakePlateById.put(id, safeTrim(intake.getBienSoXe()));
            cbbMaTiepNhanXe.getItems().add(id);
        }
    }

    private void applyPartComboBox(List<VatTuPhuTung> parts) {
        partNameToId.clear();
        partIdToName.clear();
        cbbMaVatTu.getItems().clear();
        cbbMaVatTu.getItems().add(NO_PART_OPTION);

        for (VatTuPhuTung part : parts) {
            String id = safeTrim(part.getMaVatTuPhuTung());
            String name = safeTrim(part.getTenVatTuPhuTung());
            partNameToId.put(name, id);
            partIdToName.put(id, name);
            cbbMaVatTu.getItems().add(name);
        }
    }

    private String getSelectedPartId() {
        String value = cbbMaVatTu.getValue();

        if (value == null || value.trim().isEmpty() || NO_PART_OPTION.equals(value)) {
            return null;
        }

        return partNameToId.getOrDefault(value, value).trim();
    }

    private String getSelectedRepairId() {
        SuaChuaXe selected = tblRepairOrders.getSelectionModel().getSelectedItem();
        return selected == null ? null : selected.getMaSuaChuaXe();
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }

    private record RepairDetailInput(
            String partId,
            int quantity,
            String laborId,
            String content
    ) {
    }

    private record RepairRefreshData(
            List<TiepNhanXe> intakes,
            List<VatTuPhuTung> parts,
            List<String> laborIds,
            List<SuaChuaXe> repairs
    ) {
    }
}
