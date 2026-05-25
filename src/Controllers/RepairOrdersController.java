package Controllers;

import MODEL.ChiTietSuaChuaXe;
import MODEL.SuaChuaXe;
import Service.SuaChuaXeService;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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

public class RepairOrdersController implements Initializable {

    @FXML private TextField txtMaSuaChua;
    @FXML private ComboBox<String> cbbMaTiepNhanXe;
    @FXML private DatePicker dpNgaySuaChua;
    @FXML private ComboBox<String> cbbMaVatTu;
    @FXML private TextField txtSoLuong;
    @FXML private TextField txtTienCong;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        loadComboBoxes();
        loadTableData();
        setupEvents();
    }

    private void setupTableColumns() {
        colMaSuaChua.setCellValueFactory(new PropertyValueFactory<>("maSuaChuaXe"));
        colMaTiepNhanXe.setCellValueFactory(new PropertyValueFactory<>("maTiepNhanXe"));
        colNgaySuaChua.setCellValueFactory(new PropertyValueFactory<>("ngaySuaChua"));
        colThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));

        colBienSoXe.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        suaChuaService.getBienSoXeByIntakeId(cellData.getValue().getMaTiepNhanXe())
                )
        );

        colNoiDungSuaChua.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        suaChuaService.getNoiDungByRepairId(cellData.getValue().getMaSuaChuaXe())
                )
        );

        colMaVatTu.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        suaChuaService.getMaVatTuByRepairId(cellData.getValue().getMaSuaChuaXe())
                )
        );

        colSoLuong.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(
                        suaChuaService.getSoLuongByRepairId(cellData.getValue().getMaSuaChuaXe())
                )
        );
    }

    private void loadComboBoxes() {
        cbbMaTiepNhanXe.getItems().setAll(suaChuaService.getAllIntakeIds());
        cbbMaVatTu.getItems().setAll(suaChuaService.getAllPartIds());
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
        Double tienCong = getLaborFee();

        if (ngaySuaChua == null || soLuong == null || tienCong == null) {
            return;
        }

        boolean result = suaChuaService.add(
                txtMaSuaChua.getText().trim(),
                cbbMaTiepNhanXe.getValue(),
                ngaySuaChua,
                cbbMaVatTu.getValue(),
                soLuong,
                tienCong,
                txtNoiDungSuaChua.getText().trim()
        );

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm phiếu sửa chữa thành công!");
            loadComboBoxes();
            loadTableData();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm. Kiểm tra mã phiếu, tồn kho, mã tiếp nhận hoặc dữ liệu nhập!");
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
        Double tienCong = getLaborFee();

        if (ngaySuaChua == null || soLuong == null || tienCong == null) {
            return;
        }

        boolean result = suaChuaService.update(
                txtMaSuaChua.getText().trim(),
                cbbMaTiepNhanXe.getValue(),
                ngaySuaChua,
                cbbMaVatTu.getValue(),
                soLuong,
                tienCong,
                txtNoiDungSuaChua.getText().trim()
        );

        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật phiếu sửa chữa thành công!");
            loadComboBoxes();
            loadTableData();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật. Kiểm tra tồn kho hoặc dữ liệu nhập!");
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
            cbbMaVatTu.setValue(ct.getMaVatTuPhuTung());
            txtSoLuong.setText(String.valueOf(ct.getSoLuong()));
            txtTienCong.setText(String.valueOf(ct.getSoTienCong()));
        }
    }

    private void clearForm() {
        txtMaSuaChua.setDisable(false);
        txtMaSuaChua.clear();
        cbbMaTiepNhanXe.setValue(null);
        dpNgaySuaChua.setValue(null);
        cbbMaVatTu.setValue(null);
        txtSoLuong.clear();
        txtTienCong.clear();
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

        return Date.valueOf(localDate);
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

    private Double getLaborFee() {
        try {
            double value = Double.parseDouble(txtTienCong.getText().trim());

            if (value < 0) {
                showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Tiền công không được âm!");
                return null;
            }

            return value;

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Sai dữ liệu", "Tiền công phải là số!");
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
}