package Controllers;

import DAO.KhachHangDAO;
import DAO.PhieuThuTienDAO;
import DAO.SuaChuaXeDAO;
import DAO.TiepNhanXeDAO;
import DAO.VatTuPhuTungDAO;
import MODEL.KhachHang;
import MODEL.PhieuThuTien;
import MODEL.SuaChuaXe;
import MODEL.TiepNhanXe;
import MODEL.VatTuPhuTung;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DashboardController implements Initializable {

    @FXML private Label lblTotalCustomers;
    @FXML private Label lblCarsReceived;
    @FXML private Label lblRepairOrders;
    @FXML private Label lblRevenue;

    @FXML private Label lblTodayVehicles;
    @FXML private Label lblTodayRepairs;
    @FXML private Label lblTodayReceipts;
    @FXML private Label lblLowStockParts;

    @FXML private TableView<ObservableList<String>> tblRecentActivities;
    @FXML private TableColumn<ObservableList<String>, String> colActivityTime;
    @FXML private TableColumn<ObservableList<String>, String> colActivityType;
    @FXML private TableColumn<ObservableList<String>, String> colActivityContent;
    @FXML private TableColumn<ObservableList<String>, String> colActivityStatus;

    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final TiepNhanXeDAO tiepNhanXeDAO = new TiepNhanXeDAO();
    private final SuaChuaXeDAO suaChuaXeDAO = new SuaChuaXeDAO();
    private final PhieuThuTienDAO phieuThuTienDAO = new PhieuThuTienDAO();
    private final VatTuPhuTungDAO vatTuPhuTungDAO = new VatTuPhuTungDAO();

    private final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        loadSummaryCards();
        loadSystemSummary();
        loadRecentActivities();
    }

    private void setupTableColumns() {
        if (colActivityTime == null || colActivityType == null
                || colActivityContent == null || colActivityStatus == null) {
            System.out.println("Dashboard table columns are missing fx:id in Dashboard.fxml");
            return;
        }

        colActivityTime.setCellValueFactory(data ->
                new SimpleStringProperty(getCell(data.getValue(), 0)));

        colActivityType.setCellValueFactory(data ->
                new SimpleStringProperty(getCell(data.getValue(), 1)));

        colActivityContent.setCellValueFactory(data ->
                new SimpleStringProperty(getCell(data.getValue(), 2)));

        colActivityStatus.setCellValueFactory(data ->
                new SimpleStringProperty(getCell(data.getValue(), 3)));
    }

    private void loadSummaryCards() {
        List<KhachHang> customers = safeList(khachHangDAO.getAll());
        List<TiepNhanXe> intakes = safeList(tiepNhanXeDAO.getALL());
        List<SuaChuaXe> repairs = safeList(suaChuaXeDAO.getAll());
        List<PhieuThuTien> receipts = safeList(phieuThuTienDAO.getAll());

        double revenue = 0;

        for (PhieuThuTien pt : receipts) {
            if (pt != null) {
                revenue += pt.getSoTienThu();
            }
        }

        setLabel(lblTotalCustomers, String.valueOf(customers.size()));
        setLabel(lblCarsReceived, String.valueOf(intakes.size()));
        setLabel(lblRepairOrders, String.valueOf(repairs.size()));
        setLabel(lblRevenue, formatMoney(revenue));
    }

    /*
     * Dùng tổng toàn hệ thống thay vì "today".
     * Như vậy dashboard không bị toàn số 0 khi data mẫu nằm ở ngày khác.
     */
    private void loadSystemSummary() {
        List<TiepNhanXe> intakes = safeList(tiepNhanXeDAO.getALL());
        List<SuaChuaXe> repairs = safeList(suaChuaXeDAO.getAll());
        List<PhieuThuTien> receipts = safeList(phieuThuTienDAO.getAll());
        List<VatTuPhuTung> parts = safeList(vatTuPhuTungDAO.getAll());

        int lowStockCount = 0;

        for (VatTuPhuTung vt : parts) {
            if (vt != null && vt.getSoLuongVatTuPhuTung() <= 5) {
                lowStockCount++;
            }
        }

        setLabel(lblTodayVehicles, "Total vehicles received: " + intakes.size());
        setLabel(lblTodayRepairs, "Total repair orders: " + repairs.size());
        setLabel(lblTodayReceipts, "Total receipts: " + receipts.size());
        setLabel(lblLowStockParts, "Low stock parts: " + lowStockCount);
    }

    private void loadRecentActivities() {
        if (tblRecentActivities == null) {
            System.out.println("tblRecentActivities is missing fx:id in Dashboard.fxml");
            return;
        }

        ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();

        List<TiepNhanXe> intakes = safeList(tiepNhanXeDAO.getALL());
        List<SuaChuaXe> repairs = safeList(suaChuaXeDAO.getAll());
        List<PhieuThuTien> receipts = safeList(phieuThuTienDAO.getAll());
        List<VatTuPhuTung> parts = safeList(vatTuPhuTungDAO.getAll());

        int count = 0;

        for (int i = intakes.size() - 1; i >= 0 && count < 4; i--) {
            TiepNhanXe tnx = intakes.get(i);

            if (tnx == null) {
                continue;
            }

            rows.add(row(
                    safeDate(tnx.getNgayTiepNhan()),
                    "Intake",
                    "Received vehicle " + safeText(tnx.getBienSoXe()) + " - " + safeText(tnx.getMaTiepNhanXe()),
                    "Active"
            ));

            count++;
        }

        for (int i = repairs.size() - 1; i >= 0 && count < 8; i--) {
            SuaChuaXe sc = repairs.get(i);

            if (sc == null) {
                continue;
            }

            rows.add(row(
                    safeDate(sc.getNgaySuaChua()),
                    "Repair",
                    "Repair order " + safeText(sc.getMaSuaChuaXe()) + " - " + formatMoney(sc.getThanhTien()),
                    "Completed"
            ));

            count++;
        }

        for (int i = receipts.size() - 1; i >= 0 && count < 12; i--) {
            PhieuThuTien pt = receipts.get(i);

            if (pt == null) {
                continue;
            }

            rows.add(row(
                    safeDate(pt.getNgayThuTien()),
                    "Billing",
                    "Receipt " + safeText(pt.getMaPhieuThuTien()) + " - " + formatMoney(pt.getSoTienThu()),
                    "Paid"
            ));

            count++;
        }

        for (VatTuPhuTung vt : parts) {
            if (vt != null && vt.getSoLuongVatTuPhuTung() <= 5) {
                rows.add(row(
                        "-",
                        "Inventory",
                        safeText(vt.getTenVatTuPhuTung()) + " is low stock: " + vt.getSoLuongVatTuPhuTung(),
                        "Warning"
                ));
            }
        }

        tblRecentActivities.setItems(rows);
    }

    private ObservableList<String> row(String time, String type, String content, String status) {
        return FXCollections.observableArrayList(time, type, content, status);
    }

    private String getCell(ObservableList<String> row, int index) {
        if (row == null || row.size() <= index || row.get(index) == null) {
            return "";
        }

        return row.get(index);
    }

    private void setLabel(Label label, String value) {
        if (label != null) {
            label.setText(value);
        } else {
            System.out.println("Missing Label fx:id in Dashboard.fxml. Value not set: " + value);
        }
    }

    private String safeDate(java.sql.Date date) {
        if (date == null) {
            return "-";
        }

        return date.toString();
    }

    private String safeText(String text) {
        if (text == null) {
            return "-";
        }

        return text.trim();
    }

    private String formatMoney(double value) {
        return currencyFormat.format(value);
    }

    private <T> List<T> safeList(List<T> list) {
        if (list == null) {
            return new ArrayList<>();
        }

        return list;
    }
}