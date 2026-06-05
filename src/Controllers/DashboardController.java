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
import Service.AuthService;
import Service.AuthorizationService;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DashboardController implements Initializable, Refreshable {

    @FXML private Label lblTotalCustomers;
    @FXML private Label lblCarsReceived;
    @FXML private Label lblRepairOrders;
    @FXML private Label lblRevenue;

    @FXML private Label lblTodayVehicles;
    @FXML private Label lblTodayRepairs;
    @FXML private Label lblTodayReceipts;
    @FXML private Label lblLowStockParts;
    @FXML private Label lblSystemStatus;
    @FXML private Label lblDebtStatus;
    @FXML private Label lblInventoryStatus;
    @FXML private Label lblRoleStatus;

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
        refreshData();
    }

    @Override
    public void refreshData() {
        List<KhachHang> customers = safeList(khachHangDAO.getAll());
        List<TiepNhanXe> intakes = safeList(tiepNhanXeDAO.getALL());
        List<SuaChuaXe> repairs = safeList(suaChuaXeDAO.getAll());
        List<PhieuThuTien> receipts = safeList(phieuThuTienDAO.getAll());
        List<VatTuPhuTung> parts = safeList(vatTuPhuTungDAO.getAll());

        loadSummaryCards(customers, intakes, repairs, receipts);
        loadSystemSummary(customers, intakes, repairs, receipts, parts);
        loadRecentActivities(intakes, repairs, receipts, parts);
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

    private void loadSummaryCards(List<KhachHang> customers, List<TiepNhanXe> intakes,
            List<SuaChuaXe> repairs, List<PhieuThuTien> receipts) {
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
    private void loadSystemSummary(List<KhachHang> customers, List<TiepNhanXe> intakes,
            List<SuaChuaXe> repairs, List<PhieuThuTien> receipts, List<VatTuPhuTung> parts) {
        int lowStockCount = 0;
        int outOfStockCount = 0;
        int debtCount = 0;
        double totalDebt = 0;

        for (VatTuPhuTung vt : parts) {
            if (vt != null && vt.getSoLuongVatTuPhuTung() <= 5) {
                lowStockCount++;
            }

            if (vt != null && vt.getSoLuongVatTuPhuTung() <= 0) {
                outOfStockCount++;
            }
        }

        for (TiepNhanXe tnx : intakes) {
            if (tnx != null && tnx.getTienNo() > 0) {
                debtCount++;
                totalDebt += tnx.getTienNo();
            }
        }

        setLabel(lblTodayVehicles, "Tổng xe đã tiếp nhận: " + intakes.size());
        setLabel(lblTodayRepairs, "Tổng phiếu sửa chữa: " + repairs.size());
        setLabel(lblTodayReceipts, "Tổng phiếu thu: " + receipts.size());
        setLabel(lblLowStockParts, "Vật tư sắp hết: " + lowStockCount);
        setLabel(lblSystemStatus, buildSystemStatus(!customers.isEmpty(), intakes, repairs, receipts, parts));
        setLabel(lblDebtStatus, "Công nợ còn lại: " + debtCount + " xe - " + formatMoney(totalDebt));
        setLabel(lblInventoryStatus, "Trạng thái tồn kho: " + buildInventoryStatus(lowStockCount, outOfStockCount));
        setLabel(lblRoleStatus, "Vai trò hiện tại: " + AuthorizationService.getRoleName(AuthService.currentUser));
    }

    private void loadRecentActivities(List<TiepNhanXe> intakes, List<SuaChuaXe> repairs,
            List<PhieuThuTien> receipts, List<VatTuPhuTung> parts) {
        if (tblRecentActivities == null) {
            System.out.println("tblRecentActivities is missing fx:id in Dashboard.fxml");
            return;
        }

        ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();
        Map<String, SuaChuaXe> repairByIntakeId = buildRepairByIntakeId(repairs);
        Set<String> paidRepairIds = buildPaidRepairIds(receipts);

        int count = 0;

        for (int i = intakes.size() - 1; i >= 0 && count < 4; i--) {
            TiepNhanXe tnx = intakes.get(i);

            if (tnx == null) {
                continue;
            }

            rows.add(row(
                    safeDate(tnx.getNgayTiepNhan()),
                    "Tiếp nhận",
                    "Tiếp nhận xe " + safeText(tnx.getBienSoXe()) + " - " + safeText(tnx.getMaTiepNhanXe()),
                    buildIntakeStatus(tnx, repairByIntakeId, paidRepairIds)
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
                    "Sửa chữa",
                    "Phiếu sửa chữa " + safeText(sc.getMaSuaChuaXe()) + " - " + formatMoney(sc.getThanhTien()),
                    buildRepairStatus(sc, paidRepairIds)
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
                    "Thu tiền",
                    "Phiếu thu " + safeText(pt.getMaPhieuThuTien()) + " - " + formatMoney(pt.getSoTienThu()),
                    "Thu tiền thành công"
            ));

            count++;
        }

        for (VatTuPhuTung vt : parts) {
            if (vt != null && vt.getSoLuongVatTuPhuTung() <= 5) {
                rows.add(row(
                        "-",
                        "Tồn kho",
                        safeText(vt.getTenVatTuPhuTung()) + " sắp hết: " + vt.getSoLuongVatTuPhuTung(),
                        buildPartStatus(vt)
                ));
            }
        }

        tblRecentActivities.setItems(rows);
    }

    private String buildSystemStatus(boolean customersLoaded, List<TiepNhanXe> intakes,
            List<SuaChuaXe> repairs, List<PhieuThuTien> receipts, List<VatTuPhuTung> parts) {
        if (customersLoaded || !intakes.isEmpty() || !repairs.isEmpty() || !receipts.isEmpty() || !parts.isEmpty()) {
            return "Trạng thái hệ thống: Đã tải dữ liệu";
        }

        return "Trạng thái hệ thống: Chưa có dữ liệu";
    }

    private String buildInventoryStatus(int lowStockCount, int outOfStockCount) {
        if (outOfStockCount > 0) {
            return outOfStockCount + " hết hàng, " + lowStockCount + " sắp hết";
        }

        if (lowStockCount > 0) {
            return lowStockCount + " vật tư sắp hết";
        }

        return "Ổn định";
    }

    private Map<String, SuaChuaXe> buildRepairByIntakeId(List<SuaChuaXe> repairs) {
        Map<String, SuaChuaXe> repairByIntakeId = new HashMap<>();

        for (SuaChuaXe repair : repairs) {
            if (repair != null) {
                repairByIntakeId.put(safeKey(repair.getMaTiepNhanXe()), repair);
            }
        }

        return repairByIntakeId;
    }

    private Set<String> buildPaidRepairIds(List<PhieuThuTien> receipts) {
        Set<String> paidRepairIds = new HashSet<>();

        for (PhieuThuTien receipt : receipts) {
            if (receipt != null) {
                paidRepairIds.add(safeKey(receipt.getMaSuaChuaXe()));
            }
        }

        return paidRepairIds;
    }

    private String buildIntakeStatus(TiepNhanXe intake, Map<String, SuaChuaXe> repairByIntakeId,
            Set<String> paidRepairIds) {
        if (intake == null) {
            return "Đang tiếp nhận";
        }

        SuaChuaXe repair = repairByIntakeId.get(safeKey(intake.getMaTiepNhanXe()));

        if (repair != null && paidRepairIds.contains(safeKey(repair.getMaSuaChuaXe()))) {
            return "Hoàn tất tiếp nhận";
        }

        return "Đang tiếp nhận";
    }

    private String buildRepairStatus(SuaChuaXe repair, Set<String> paidRepairIds) {
        if (repair != null && paidRepairIds.contains(safeKey(repair.getMaSuaChuaXe()))) {
            return "Đã sửa chữa";
        }

        return "Đang sửa chữa";
    }

    private String buildPartStatus(VatTuPhuTung part) {
        if (part == null) {
            return "Không xác định";
        }

        if (part.getSoLuongVatTuPhuTung() <= 0) {
            return "Hết hàng";
        }

        if (part.getSoLuongVatTuPhuTung() <= 5) {
            return "Sắp hết";
        }

        return "Còn hàng";
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

    private String safeKey(String value) {
        if (value == null) {
            return "";
        }

        return value.trim();
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
