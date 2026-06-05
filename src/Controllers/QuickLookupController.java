package Controllers;

import DAO.KhachHangDAO;
import DAO.TiepNhanXeDAO;
import DAO.VatTuPhuTungDAO;
import MODEL.KhachHang;
import MODEL.SuaChuaXe;
import MODEL.TiepNhanXe;
import MODEL.VatTuPhuTung;
import Service.SuaChuaXeService;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class QuickLookupController implements Initializable, Refreshable {

    private static final String TYPE_ALL = "Tất cả";
    private static final String TYPE_VEHICLES = "Xe tiếp nhận";
    private static final String TYPE_CUSTOMERS = "Khách hàng";
    private static final String TYPE_PARTS = "Vật tư";
    private static final String TYPE_REPAIRS = "Sửa chữa";

    @FXML private ComboBox<String> cbbLookupType;
    @FXML private TextField txtLookupKeyword;
    @FXML private Button btnLookup;
    @FXML private Button btnResetLookup;

    @FXML private Label lblVehiclesFound;
    @FXML private Label lblPartsFound;
    @FXML private Label lblRepairRecordsFound;

    @FXML private TableView<ObservableList<String>> tblLookupResults;
    @FXML private TableColumn<ObservableList<String>, String> colLookupType;
    @FXML private TableColumn<ObservableList<String>, String> colLookupCode;
    @FXML private TableColumn<ObservableList<String>, String> colLookupName;
    @FXML private TableColumn<ObservableList<String>, String> colLookupInfo;
    @FXML private TableColumn<ObservableList<String>, String> colLookupStatus;

    private final TiepNhanXeDAO tiepNhanXeDAO = new TiepNhanXeDAO();
    private final VatTuPhuTungDAO vatTuPhuTungDAO = new VatTuPhuTungDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final SuaChuaXeService suaChuaXeService = new SuaChuaXeService();

    private List<LookupRow> cachedRows = new ArrayList<>();
    private boolean loading = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupComboBox();
        setupTableColumns();
        setupEvents();
        loadLookupDataAsync();
    }

    @Override
    public void refreshData() {
        loadLookupDataAsync();
    }

    private void setupComboBox() {
        cbbLookupType.getItems().setAll(
                TYPE_ALL,
                TYPE_VEHICLES,
                TYPE_CUSTOMERS,
                TYPE_PARTS,
                TYPE_REPAIRS
        );

        cbbLookupType.setValue(TYPE_ALL);
    }

    private void setupTableColumns() {
        colLookupType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
        colLookupCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
        colLookupName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(2)));
        colLookupInfo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(3)));
        colLookupStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(4)));
    }

    private void setupEvents() {
        btnLookup.setOnAction(e -> handleLookup());

        btnResetLookup.setOnAction(e -> {
            cbbLookupType.setValue(TYPE_ALL);
            txtLookupKeyword.clear();
            handleLookup();
        });
    }

    private void loadLookupDataAsync() {
        if (loading) {
            return;
        }

        loading = true;
        setLookupButtonsDisabled(true);
        tblLookupResults.setItems(singleRow("Đang tải", "", "", "Đang tải dữ liệu tra cứu...", ""));

        Task<List<LookupRow>> task = new Task<>() {
            @Override
            protected List<LookupRow> call() {
                return loadLookupRows();
            }
        };

        task.setOnSucceeded(e -> {
            loading = false;
            setLookupButtonsDisabled(false);
            cachedRows = task.getValue();
            handleLookup();
        });

        task.setOnFailed(e -> {
            loading = false;
            setLookupButtonsDisabled(false);
            tblLookupResults.setItems(singleRow("Lỗi", "", "", "Không thể tải dữ liệu tra cứu.", ""));
            lblVehiclesFound.setText("0");
            lblPartsFound.setText("0");
            lblRepairRecordsFound.setText("0");
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private List<LookupRow> loadLookupRows() {
        List<LookupRow> rows = new ArrayList<>();

        for (TiepNhanXe tnx : tiepNhanXeDAO.getALL()) {
            rows.add(new LookupRow(
                    TYPE_VEHICLES,
                    "Xe",
                    tnx.getMaTiepNhanXe(),
                    tnx.getBienSoXe(),
                    "Khách hàng: " + tnx.getMaKhachHang()
                            + " | Hiệu xe: " + tnx.getMaHieuXe()
                            + " | Tiền nợ: " + tnx.getTienNo(),
                    tnx.getTienNo() > 0 ? "Còn nợ" : "Đã tất toán"
            ));
        }

        for (KhachHang kh : khachHangDAO.getAll()) {
            rows.add(new LookupRow(
                    TYPE_CUSTOMERS,
                    "Khách hàng",
                    kh.getMaKhachHang(),
                    kh.getTenKhachHang(),
                    "SĐT: " + kh.getSoDienThoaiKhachHang()
                            + " | Địa chỉ: " + kh.getDiaChiKhachHang(),
                    "Đang lưu"
            ));
        }

        for (VatTuPhuTung vt : vatTuPhuTungDAO.getAll()) {
            rows.add(new LookupRow(
                    TYPE_PARTS,
                    "Vật tư",
                    vt.getMaVatTuPhuTung(),
                    vt.getTenVatTuPhuTung(),
                    "SL: " + vt.getSoLuongVatTuPhuTung()
                            + " | ĐVT: " + vt.getDonViTinh()
                            + " | Đơn giá: " + vt.getDonGiaVatTuPhuTung(),
                    getInventoryStatus(vt)
            ));
        }

        for (SuaChuaXe sc : suaChuaXeService.getAll()) {
            String bienSo = suaChuaXeService.getBienSoXeByIntakeId(sc.getMaTiepNhanXe());
            String noiDung = suaChuaXeService.getNoiDungByRepairId(sc.getMaSuaChuaXe());

            rows.add(new LookupRow(
                    TYPE_REPAIRS,
                    "Sửa chữa",
                    sc.getMaSuaChuaXe(),
                    bienSo,
                    "Mã tiếp nhận: " + sc.getMaTiepNhanXe()
                            + " | " + noiDung
                            + " | Thành tiền: " + sc.getThanhTien(),
                    sc.getThanhTien() > 0 ? "Đang sửa chữa" : "Chưa tính tiền"
            ));
        }

        return rows;
    }

    private void handleLookup() {
        if (loading) {
            return;
        }

        String type = cbbLookupType.getValue();
        String keyword = txtLookupKeyword.getText() == null
                ? ""
                : txtLookupKeyword.getText().trim().toLowerCase();

        ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();
        int vehicleCount = 0;
        int partCount = 0;
        int repairCount = 0;

        for (LookupRow lookupRow : cachedRows) {
            if (!matchesType(type, lookupRow.group())) {
                continue;
            }

            if (!keyword.isEmpty() && !lookupRow.searchText().contains(keyword)) {
                continue;
            }

            rows.add(row(
                    lookupRow.displayType(),
                    lookupRow.code(),
                    lookupRow.name(),
                    lookupRow.info(),
                    lookupRow.status()
            ));

            if (TYPE_VEHICLES.equals(lookupRow.group())) {
                vehicleCount++;
            } else if (TYPE_PARTS.equals(lookupRow.group())) {
                partCount++;
            } else if (TYPE_REPAIRS.equals(lookupRow.group())) {
                repairCount++;
            }
        }

        lblVehiclesFound.setText(String.valueOf(vehicleCount));
        lblPartsFound.setText(String.valueOf(partCount));
        lblRepairRecordsFound.setText(String.valueOf(repairCount));
        tblLookupResults.setItems(rows);
    }

    private boolean matchesType(String selectedType, String rowType) {
        return selectedType == null || TYPE_ALL.equals(selectedType) || selectedType.equals(rowType);
    }

    private String getInventoryStatus(VatTuPhuTung part) {
        if (part.getSoLuongVatTuPhuTung() <= 0) {
            return "Hết hàng";
        }

        if (part.getSoLuongVatTuPhuTung() <= 5) {
            return "Sắp hết";
        }

        return "Còn hàng";
    }

    private void setLookupButtonsDisabled(boolean disabled) {
        btnLookup.setDisable(disabled);
        btnResetLookup.setDisable(disabled);
    }

    private ObservableList<String> row(String type, String code, String name, String info, String status) {
        return FXCollections.observableArrayList(type, code, name, info, status);
    }

    private ObservableList<ObservableList<String>> singleRow(
            String type,
            String code,
            String name,
            String info,
            String status
    ) {
        ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();
        rows.add(row(type, code, name, info, status));
        return rows;
    }

    private record LookupRow(
            String group,
            String displayType,
            String code,
            String name,
            String info,
            String status
    ) {
        String searchText() {
            return (group + " " + displayType + " " + code + " " + name + " " + info + " " + status)
                    .toLowerCase();
        }
    }
}
