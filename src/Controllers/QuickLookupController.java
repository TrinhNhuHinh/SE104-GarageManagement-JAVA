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
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class QuickLookupController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupComboBox();
        setupTableColumns();
        setupEvents();
        handleLookup();
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

    private void handleLookup() {
        String type = cbbLookupType.getValue();
        String keyword = txtLookupKeyword.getText() == null ? "" : txtLookupKeyword.getText().trim().toLowerCase();

        ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();

        int vehicleCount = 0;
        int partCount = 0;
        int repairCount = 0;

        if (type == null || type.equals(TYPE_ALL) || type.equals(TYPE_VEHICLES)) {
            List<TiepNhanXe> vehicles = tiepNhanXeDAO.getALL();

            for (TiepNhanXe tnx : vehicles) {
                String text = (
                        tnx.getMaTiepNhanXe() + " " +
                        tnx.getMaKhachHang() + " " +
                        tnx.getBienSoXe() + " " +
                        tnx.getMaHieuXe()
                ).toLowerCase();

                if (keyword.isEmpty() || text.contains(keyword)) {
                    rows.add(row(
                            "Xe",
                            tnx.getMaTiepNhanXe(),
                            tnx.getBienSoXe(),
                            "Khách hàng: " + tnx.getMaKhachHang() + " | Hiệu xe: " + tnx.getMaHieuXe() + " | Tiền nợ: " + tnx.getTienNo(),
                            tnx.getTienNo() > 0 ? "Còn nợ" : "Đã tất toán"
                    ));

                    vehicleCount++;
                }
            }
        }

        if (type == null || type.equals(TYPE_ALL) || type.equals(TYPE_CUSTOMERS)) {
            List<KhachHang> customers = khachHangDAO.getAll();

            for (KhachHang kh : customers) {
                String text = (
                        kh.getMaKhachHang() + " " +
                        kh.getTenKhachHang() + " " +
                        kh.getSoDienThoaiKhachHang() + " " +
                        kh.getDiaChiKhachHang()
                ).toLowerCase();

                if (keyword.isEmpty() || text.contains(keyword)) {
                    rows.add(row(
                            "Khách hàng",
                            kh.getMaKhachHang(),
                            kh.getTenKhachHang(),
                            "SĐT: " + kh.getSoDienThoaiKhachHang() + " | Địa chỉ: " + kh.getDiaChiKhachHang(),
                            "Đang lưu"
                    ));
                }
            }
        }

        if (type == null || type.equals(TYPE_ALL) || type.equals(TYPE_PARTS)) {
            List<VatTuPhuTung> parts = vatTuPhuTungDAO.getAll();

            for (VatTuPhuTung vt : parts) {
                String text = (
                        vt.getMaVatTuPhuTung() + " " +
                        vt.getTenVatTuPhuTung() + " " +
                        vt.getDonViTinh()
                ).toLowerCase();

                if (keyword.isEmpty() || text.contains(keyword)) {
                    String status;
                    if (vt.getSoLuongVatTuPhuTung() <= 0) {
                        status = "Hết hàng";
                    } else if (vt.getSoLuongVatTuPhuTung() <= 5) {
                        status = "Sắp hết";
                    } else {
                        status = "Còn hàng";
                    }

                    rows.add(row(
                            "Vật tư",
                            vt.getMaVatTuPhuTung(),
                            vt.getTenVatTuPhuTung(),
                            "SL: " + vt.getSoLuongVatTuPhuTung() + " | ĐVT: " + vt.getDonViTinh() + " | Đơn giá: " + vt.getDonGiaVatTuPhuTung(),
                            status
                    ));

                    partCount++;
                }
            }
        }

        if (type == null || type.equals(TYPE_ALL) || type.equals(TYPE_REPAIRS)) {
            List<SuaChuaXe> repairs = suaChuaXeService.getAll();

            for (SuaChuaXe sc : repairs) {
                String bienSo = suaChuaXeService.getBienSoXeByIntakeId(sc.getMaTiepNhanXe());
                String noiDung = suaChuaXeService.getNoiDungByRepairId(sc.getMaSuaChuaXe());

                String text = (
                        sc.getMaSuaChuaXe() + " " +
                        sc.getMaTiepNhanXe() + " " +
                        bienSo + " " +
                        noiDung
                ).toLowerCase();

                if (keyword.isEmpty() || text.contains(keyword)) {
                    rows.add(row(
                            "Sửa chữa",
                            sc.getMaSuaChuaXe(),
                            bienSo,
                            "Mã tiếp nhận: " + sc.getMaTiepNhanXe() + " | " + noiDung + " | Thành tiền: " + sc.getThanhTien(),
                            sc.getThanhTien() > 0 ? "Chờ thu tiền" : "Chưa tính tiền"
                    ));

                    repairCount++;
                }
            }
        }

        lblVehiclesFound.setText(String.valueOf(vehicleCount));
        lblPartsFound.setText(String.valueOf(partCount));
        lblRepairRecordsFound.setText(String.valueOf(repairCount));

        tblLookupResults.setItems(rows);
    }

    private ObservableList<String> row(String type, String code, String name, String info, String status) {
        return FXCollections.observableArrayList(type, code, name, info, status);
    }
}
