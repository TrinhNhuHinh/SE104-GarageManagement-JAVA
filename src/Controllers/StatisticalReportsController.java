package Controllers;

import Config.DBConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class StatisticalReportsController implements Initializable, Refreshable {

    private static final String REPORT_REVENUE = "Báo cáo doanh thu";
    private static final String REPORT_INVENTORY = "Báo cáo vật tư tồn";

    @FXML private Label lblMonthlyRevenue;
    @FXML private Label lblReportRepairOrders;
    @FXML private Label lblReportCarsReceived;
    @FXML private Label lblLowStockParts;

    @FXML private ComboBox<String> cbbReportType;
    @FXML private ComboBox<String> cbbMonth;
    @FXML private ComboBox<String> cbbYear;

    @FXML private Button btnViewReport;
    @FXML private Button btnExportReport;

    @FXML private BarChart<String, Number> barReportChart;
    @FXML private CategoryAxis chartXAxis;
    @FXML private NumberAxis chartYAxis;

    @FXML private TableView<ObservableList<String>> tblReports;
    @FXML private TableColumn<ObservableList<String>, String> colReportNo;
    @FXML private TableColumn<ObservableList<String>, String> colReportName;
    @FXML private TableColumn<ObservableList<String>, String> colReportQuantity;
    @FXML private TableColumn<ObservableList<String>, String> colReportAmount;
    @FXML private TableColumn<ObservableList<String>, String> colReportRate;
    @FXML private TableColumn<ObservableList<String>, String> colReportNote;

    private final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupComboBoxes();
        setupTableColumns();
        setupChart();
        setupEvents();
        reloadReportScreen();
    }

    @Override
    public void refreshData() {
        reloadReportScreen();
    }

    private void setupComboBoxes() {
        cbbReportType.getItems().setAll(REPORT_REVENUE, REPORT_INVENTORY);

        for (int i = 1; i <= 12; i++) {
            cbbMonth.getItems().add(String.valueOf(i));
        }

        int currentYear = LocalDate.now().getYear();
        for (int year = currentYear - 3; year <= currentYear + 1; year++) {
            cbbYear.getItems().add(String.valueOf(year));
        }

        cbbReportType.setValue(REPORT_REVENUE);
        cbbMonth.setValue(String.valueOf(LocalDate.now().getMonthValue()));
        cbbYear.setValue(String.valueOf(currentYear));
    }

    private void setupTableColumns() {
        colReportNo.setCellValueFactory(data -> new SimpleStringProperty(getCell(data.getValue(), 0)));
        colReportName.setCellValueFactory(data -> new SimpleStringProperty(getCell(data.getValue(), 1)));
        colReportQuantity.setCellValueFactory(data -> new SimpleStringProperty(getCell(data.getValue(), 2)));
        colReportAmount.setCellValueFactory(data -> new SimpleStringProperty(getCell(data.getValue(), 3)));
        colReportRate.setCellValueFactory(data -> new SimpleStringProperty(getCell(data.getValue(), 4)));
        colReportNote.setCellValueFactory(data -> new SimpleStringProperty(getCell(data.getValue(), 5)));
    }

    private void setupChart() {
        barReportChart.setTitle("");
        barReportChart.setAnimated(false);
        barReportChart.setLegendVisible(false);
        chartXAxis.setLabel("Nội dung");
        chartYAxis.setLabel("Giá trị");
    }

    private void setupEvents() {
        btnViewReport.setOnAction(e -> reloadReportScreen());

        btnExportReport.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Xuất file");
            alert.setHeaderText(null);
            alert.setContentText("Chức năng xuất file có thể phát triển sau. Hiện tại bảng và biểu đồ đã tải dữ liệu báo cáo.");
            alert.showAndWait();
        });

        cbbReportType.setOnAction(e -> reloadReportScreen());
        cbbMonth.setOnAction(e -> reloadReportScreen());
        cbbYear.setOnAction(e -> reloadReportScreen());
    }

    private void reloadReportScreen() {
        if (cbbMonth.getValue() == null || cbbYear.getValue() == null || cbbReportType.getValue() == null) {
            return;
        }

        loadSummaryCards();
        loadReport();
    }

    private void loadSummaryCards() {
        int month = getSelectedMonth();
        int year = getSelectedYear();

        List<RevenueReportRow> revenueRows = loadRevenueRows(month, year);
        List<InventoryReportRow> inventoryRows = loadInventoryRows(month, year);

        double revenue = 0;
        int repairCount = 0;
        int warningParts = 0;
        double inventoryValue = 0;

        for (RevenueReportRow row : revenueRows) {
            revenue += row.amount();
            repairCount += row.repairCount();
        }

        for (InventoryReportRow row : inventoryRows) {
            inventoryValue += row.endingValue();
            if (row.endingStock() <= 5) {
                warningParts++;
            }
        }

        lblMonthlyRevenue.setText(formatMoney(revenue));
        lblReportRepairOrders.setText(String.valueOf(repairCount));
        lblReportCarsReceived.setText(formatMoney(inventoryValue));
        lblLowStockParts.setText(String.valueOf(warningParts));
    }

    private void loadReport() {
        String type = cbbReportType.getValue();
        if (REPORT_INVENTORY.equals(type)) {
            loadInventoryReport();
        } else {
            loadRevenueReport();
        }
    }

    private void loadRevenueReport() {
        int month = getSelectedMonth();
        int year = getSelectedYear();

        setRevenueTableHeaders();

        ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<RevenueReportRow> reportRows = loadRevenueRows(month, year);

        int index = 1;
        int totalRepairCount = 0;
        double totalRevenue = 0;

        for (RevenueReportRow reportRow : reportRows) {
            totalRepairCount += reportRow.repairCount();
            totalRevenue += reportRow.amount();
        }

        for (RevenueReportRow reportRow : reportRows) {
            rows.add(row(
                    String.valueOf(index++),
                    reportRow.brandName(),
                    String.valueOf(reportRow.repairCount()),
                    formatMoney(reportRow.amount()),
                    formatPercent(reportRow.amount(), totalRevenue),
                    "Mã hiệu xe: " + reportRow.brandId()
            ));

            series.getData().add(new XYChart.Data<>(reportRow.brandName(), reportRow.amount()));
        }

        if (rows.isEmpty()) {
            rows.add(row("", "Không có dữ liệu", "0", formatMoney(0), "0%", "Không có phiếu sửa chữa trong kỳ"));
        } else {
            rows.add(row("", "Tổng doanh thu", String.valueOf(totalRepairCount), formatMoney(totalRevenue), "100%", "Theo hiệu xe"));
        }

        tblReports.setItems(rows);
        updateBarChart("Báo cáo doanh thu tháng " + month + "/" + year, "Hiệu xe", "Doanh thu", series);
    }

    private void loadInventoryReport() {
        int month = getSelectedMonth();
        int year = getSelectedYear();

        setInventoryTableHeaders();

        ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<InventoryReportRow> reportRows = loadInventoryRows(month, year);

        int index = 1;
        int totalBeginning = 0;
        int totalMovement = 0;
        int totalEnding = 0;

        for (InventoryReportRow reportRow : reportRows) {
            totalBeginning += reportRow.beginningStock();
            totalMovement += reportRow.movement();
            totalEnding += reportRow.endingStock();

            rows.add(row(
                    String.valueOf(index++),
                    reportRow.partName(),
                    String.valueOf(reportRow.beginningStock()),
                    formatSignedQuantity(reportRow.movement()),
                    String.valueOf(reportRow.endingStock()),
                    buildInventoryNote(reportRow)
            ));

            series.getData().add(new XYChart.Data<>(reportRow.partName(), reportRow.endingStock()));
        }

        rows.add(row(
                "",
                "Tổng tồn",
                String.valueOf(totalBeginning),
                formatSignedQuantity(totalMovement),
                String.valueOf(totalEnding),
                "Tồn cuối kỳ"
        ));

        tblReports.setItems(rows);
        updateBarChart("Báo cáo vật tư tồn tháng " + month + "/" + year, "Vật tư", "Tồn cuối", series);
    }

    private List<RevenueReportRow> loadRevenueRows(int month, int year) {
        List<RevenueReportRow> rows = new ArrayList<>();

        String sql = """
            SELECT hx.MaHieuXe,
                   hx.TenHieuXe,
                   COUNT(sc.MaSuaChuaXe) AS SoPhieu,
                   ISNULL(SUM(sc.ThanhTien), 0) AS DoanhThu
            FROM HIEUXE hx
            LEFT JOIN TIEPNHANXE tn
                ON tn.Ma_HieuXe = hx.MaHieuXe
            LEFT JOIN SUACHUAXE sc
                ON sc.Ma_TiepNhanXe = tn.MaTiepNhanXe
               AND MONTH(sc.NgaySuaChua) = ?
               AND YEAR(sc.NgaySuaChua) = ?
            GROUP BY hx.MaHieuXe, hx.TenHieuXe
            HAVING COUNT(sc.MaSuaChuaXe) > 0
            ORDER BY DoanhThu DESC, hx.TenHieuXe
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, month);
            ps.setInt(2, year);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rows.add(new RevenueReportRow(
                        trim(rs.getString("MaHieuXe")),
                        trim(rs.getString("TenHieuXe")),
                        rs.getInt("SoPhieu"),
                        rs.getDouble("DoanhThu")
                ));
            }

        } catch (SQLException e) {
            printSqlError(e);
            showLoadError("Không thể tải báo cáo doanh thu!");
        }

        return rows;
    }

    private List<InventoryReportRow> loadInventoryRows(int month, int year) {
        List<InventoryReportRow> rows = new ArrayList<>();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1);

        String sql = """
            WITH Movements AS (
                SELECT ctn.Ma_VatTuPhuTung,
                       nv.NgayNhap AS Ngay,
                       ctn.SoLuong AS Nhap,
                       0 AS Ban,
                       0 AS SuaChua,
                       ctn.SoLuong AS PhatSinh
                FROM CHITIETNHAPVATTU ctn
                JOIN NHAPVATTU nv
                    ON nv.MaNhapVatTu = ctn.Ma_NhapVatTu

                UNION ALL

                SELECT ctb.Ma_VatTuPhuTung,
                       bv.NgayBan AS Ngay,
                       0 AS Nhap,
                       ctb.SoLuong AS Ban,
                       0 AS SuaChua,
                       -ctb.SoLuong AS PhatSinh
                FROM CHITIETBANVATTU ctb
                JOIN BANVATTU bv
                    ON bv.MaBanVatTu = ctb.Ma_BanVatTu

                UNION ALL

                SELECT cts.Ma_VatTuPhuTung,
                       sc.NgaySuaChua AS Ngay,
                       0 AS Nhap,
                       0 AS Ban,
                       cts.SoLuong AS SuaChua,
                       -cts.SoLuong AS PhatSinh
                FROM CHITIETSUACHUAXE cts
                JOIN SUACHUAXE sc
                    ON sc.MaSuaChuaXe = cts.Ma_SuaChuaXe
                WHERE cts.Ma_VatTuPhuTung IS NOT NULL
            )
            SELECT vt.MaVatTuPhuTung,
                   vt.TenVatTuPhuTung,
                   vt.SoLuongVatTuPhuTung AS TonHienTai,
                   vt.DonGiaVatTuPhuTung,
                   ISNULL(SUM(CASE WHEN m.Ngay >= ? AND m.Ngay < ? THEN m.PhatSinh ELSE 0 END), 0) AS PhatSinhTrongKy,
                   ISNULL(SUM(CASE WHEN m.Ngay >= ? THEN m.PhatSinh ELSE 0 END), 0) AS PhatSinhSauKy,
                   ISNULL(SUM(CASE WHEN m.Ngay >= ? AND m.Ngay < ? THEN m.Nhap ELSE 0 END), 0) AS NhapTrongKy,
                   ISNULL(SUM(CASE WHEN m.Ngay >= ? AND m.Ngay < ? THEN m.Ban ELSE 0 END), 0) AS BanTrongKy,
                   ISNULL(SUM(CASE WHEN m.Ngay >= ? AND m.Ngay < ? THEN m.SuaChua ELSE 0 END), 0) AS SuaChuaTrongKy
            FROM VATTUPHUTUNG vt
            LEFT JOIN Movements m
                ON m.Ma_VatTuPhuTung = vt.MaVatTuPhuTung
            GROUP BY vt.MaVatTuPhuTung, vt.TenVatTuPhuTung, vt.SoLuongVatTuPhuTung, vt.DonGiaVatTuPhuTung
            ORDER BY vt.MaVatTuPhuTung
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            Date start = Date.valueOf(startDate);
            Date end = Date.valueOf(endDate);

            ps.setDate(1, start);
            ps.setDate(2, end);
            ps.setDate(3, end);
            ps.setDate(4, start);
            ps.setDate(5, end);
            ps.setDate(6, start);
            ps.setDate(7, end);
            ps.setDate(8, start);
            ps.setDate(9, end);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int currentStock = rs.getInt("TonHienTai");
                int movement = rs.getInt("PhatSinhTrongKy");
                int movementAfterPeriod = rs.getInt("PhatSinhSauKy");
                int endingStock = currentStock - movementAfterPeriod;
                int beginningStock = endingStock - movement;
                double unitPrice = rs.getDouble("DonGiaVatTuPhuTung");

                rows.add(new InventoryReportRow(
                        trim(rs.getString("MaVatTuPhuTung")),
                        trim(rs.getString("TenVatTuPhuTung")),
                        beginningStock,
                        movement,
                        endingStock,
                        rs.getInt("NhapTrongKy"),
                        rs.getInt("BanTrongKy"),
                        rs.getInt("SuaChuaTrongKy"),
                        endingStock * unitPrice
                ));
            }

        } catch (SQLException e) {
            printSqlError(e);
            showLoadError("Không thể tải báo cáo vật tư tồn!");
        }

        return rows;
    }

    private void setRevenueTableHeaders() {
        colReportNo.setText("STT");
        colReportName.setText("Hiệu xe");
        colReportQuantity.setText("Số phiếu");
        colReportAmount.setText("Doanh thu");
        colReportRate.setText("Tỷ lệ");
        colReportNote.setText("Ghi chú");
    }

    private void setInventoryTableHeaders() {
        colReportNo.setText("STT");
        colReportName.setText("Vật tư");
        colReportQuantity.setText("Tồn đầu");
        colReportAmount.setText("Phát sinh");
        colReportRate.setText("Tồn cuối");
        colReportNote.setText("Ghi chú");
    }

    private void updateBarChart(String title, String xLabel, String yLabel, XYChart.Series<String, Number> series) {
        barReportChart.getData().clear();
        chartXAxis.setLabel(xLabel);
        chartYAxis.setLabel(yLabel);
        barReportChart.setTitle(title);

        if (series.getData().isEmpty()) {
            series.getData().add(new XYChart.Data<>("Không có dữ liệu", 0));
        }

        barReportChart.getData().add(series);
    }

    private String buildInventoryNote(InventoryReportRow row) {
        return "Nhập: " + row.imported()
                + ", bán: " + row.sold()
                + ", sửa chữa: " + row.usedForRepair();
    }

    private ObservableList<String> row(String no, String name, String quantity, String amount, String rate, String note) {
        return FXCollections.observableArrayList(no, name, quantity, amount, rate, note);
    }

    private String getCell(ObservableList<String> row, int index) {
        if (row == null || row.size() <= index || row.get(index) == null) {
            return "";
        }

        return row.get(index);
    }

    private int getSelectedMonth() {
        return Integer.parseInt(cbbMonth.getValue());
    }

    private int getSelectedYear() {
        return Integer.parseInt(cbbYear.getValue());
    }

    private String formatMoney(double value) {
        return currencyFormat.format(value);
    }

    private String formatPercent(double value, double total) {
        if (total <= 0) {
            return "0%";
        }

        return String.format(Locale.US, "%.2f%%", (value * 100) / total);
    }

    private String formatSignedQuantity(int value) {
        if (value > 0) {
            return "+" + value;
        }

        return String.valueOf(value);
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private void showLoadError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void printSqlError(SQLException e) {
        System.out.println("SQL ERROR CODE: " + e.getErrorCode());
        System.out.println("SQL STATE: " + e.getSQLState());
        System.out.println("SQL MESSAGE: " + e.getMessage());
        e.printStackTrace();
    }

    private record RevenueReportRow(String brandId, String brandName, int repairCount, double amount) {
    }

    private record InventoryReportRow(
            String partId,
            String partName,
            int beginningStock,
            int movement,
            int endingStock,
            int imported,
            int sold,
            int usedForRepair,
            double endingValue
    ) {
    }
}
