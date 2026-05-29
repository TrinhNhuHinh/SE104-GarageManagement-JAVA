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
                "All",
                "Vehicles",
                "Customers",
                "Parts",
                "Repairs"
        );

        cbbLookupType.setValue("All");
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
            cbbLookupType.setValue("All");
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

        if (type == null || type.equals("All") || type.equals("Vehicles")) {
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
                            "Vehicle",
                            tnx.getMaTiepNhanXe(),
                            tnx.getBienSoXe(),
                            "Customer: " + tnx.getMaKhachHang() + " | Brand: " + tnx.getMaHieuXe() + " | Debt: " + tnx.getTienNo(),
                            "Active"
                    ));

                    vehicleCount++;
                }
            }
        }

        if (type == null || type.equals("All") || type.equals("Customers")) {
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
                            "Customer",
                            kh.getMaKhachHang(),
                            kh.getTenKhachHang(),
                            "Phone: " + kh.getSoDienThoaiKhachHang() + " | Address: " + kh.getDiaChiKhachHang(),
                            "Active"
                    ));
                }
            }
        }

        if (type == null || type.equals("All") || type.equals("Parts")) {
            List<VatTuPhuTung> parts = vatTuPhuTungDAO.getAll();

            for (VatTuPhuTung vt : parts) {
                String text = (
                        vt.getMaVatTuPhuTung() + " " +
                        vt.getTenVatTuPhuTung() + " " +
                        vt.getDonViTinh()
                ).toLowerCase();

                if (keyword.isEmpty() || text.contains(keyword)) {
                    String status = vt.getSoLuongVatTuPhuTung() <= 5 ? "Low stock" : "Available";

                    rows.add(row(
                            "Part",
                            vt.getMaVatTuPhuTung(),
                            vt.getTenVatTuPhuTung(),
                            "Qty: " + vt.getSoLuongVatTuPhuTung() + " | Unit: " + vt.getDonViTinh() + " | Price: " + vt.getDonGiaVatTuPhuTung(),
                            status
                    ));

                    partCount++;
                }
            }
        }

        if (type == null || type.equals("All") || type.equals("Repairs")) {
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
                            "Repair",
                            sc.getMaSuaChuaXe(),
                            bienSo,
                            "Intake: " + sc.getMaTiepNhanXe() + " | " + noiDung + " | Total: " + sc.getThanhTien(),
                            "Completed"
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