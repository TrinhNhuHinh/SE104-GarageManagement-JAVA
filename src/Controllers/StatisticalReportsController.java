package Controllers;

import DAO.BanVatTuDAO;
import DAO.PhieuThuTienDAO;
import DAO.SuaChuaXeDAO;
import DAO.TiepNhanXeDAO;
import DAO.VatTuPhuTungDAO;
import MODEL.BanVatTu;
import MODEL.PhieuThuTien;
import MODEL.SuaChuaXe;
import MODEL.TiepNhanXe;
import MODEL.VatTuPhuTung;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

public class StatisticalReportsController implements Initializable {

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

    private final PhieuThuTienDAO phieuThuTienDAO = new PhieuThuTienDAO();
    private final BanVatTuDAO banVatTuDAO = new BanVatTuDAO();
    private final SuaChuaXeDAO suaChuaXeDAO = new SuaChuaXeDAO();
    private final TiepNhanXeDAO tiepNhanXeDAO = new TiepNhanXeDAO();
    private final VatTuPhuTungDAO vatTuPhuTungDAO = new VatTuPhuTungDAO();

    private final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupComboBoxes();
        setupTableColumns();
        setupChart();
        setupEvents();
        loadSummaryCards();
        loadReport();
    }

    private void setupComboBoxes() {
        cbbReportType.getItems().setAll(
                "Revenue Report",
                "Repair Report",
                "Inventory Report",
                "Vehicle Brand Report"
        );

        for (int i = 1; i <= 12; i++) {
            cbbMonth.getItems().add(String.valueOf(i));
        }

        int currentYear = LocalDate.now().getYear();

        for (int y = currentYear - 3; y <= currentYear + 1; y++) {
            cbbYear.getItems().add(String.valueOf(y));
        }

        cbbReportType.setValue("Revenue Report");
        cbbMonth.setValue(String.valueOf(LocalDate.now().getMonthValue()));
        cbbYear.setValue(String.valueOf(currentYear));
    }

    private void setupTableColumns() {
        colReportNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
        colReportName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
        colReportQuantity.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(2)));
        colReportAmount.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(3)));
        colReportRate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(4)));
        colReportNote.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(5)));
    }

    private void setupChart() {
        barReportChart.setTitle("");
        barReportChart.setAnimated(false);
        barReportChart.setLegendVisible(false);

        chartXAxis.setLabel("Item");
        chartYAxis.setLabel("Value");
    }

    private void setupEvents() {
        btnViewReport.setOnAction(e -> {
            loadSummaryCards();
            loadReport();
        });

        btnExportReport.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export");
            alert.setHeaderText(null);
            alert.setContentText("Chức năng export có thể làm sau. Hiện tại bảng báo cáo và biểu đồ đã load dữ liệu.");
            alert.showAndWait();
        });
    }

    private void loadSummaryCards() {
        int month = Integer.parseInt(cbbMonth.getValue());
        int year = Integer.parseInt(cbbYear.getValue());

        List<PhieuThuTien> receipts = phieuThuTienDAO.getAll();
        List<BanVatTu> sales = banVatTuDAO.getAll();
        List<SuaChuaXe> repairs = suaChuaXeDAO.getAll();
        List<TiepNhanXe> intakes = tiepNhanXeDAO.getALL();
        List<VatTuPhuTung> parts = vatTuPhuTungDAO.getAll();

        double monthlyRevenue = 0;
        int monthlyRepairs = 0;
        int monthlyIntakes = 0;
        int lowStock = 0;

        for (PhieuThuTien pt : receipts) {
            if (isSameMonth(pt.getNgayThuTien(), month, year)) {
                monthlyRevenue += pt.getSoTienThu();
            }
        }

        for (BanVatTu bvt : sales) {
            if (isSameMonth(bvt.getNgayBan(), month, year)) {
                monthlyRevenue += bvt.getTongTien();
            }
        }

        for (SuaChuaXe sc : repairs) {
            if (isSameMonth(sc.getNgaySuaChua(), month, year)) {
                monthlyRepairs++;
            }
        }

        for (TiepNhanXe tnx : intakes) {
            if (isSameMonth(tnx.getNgayTiepNhan(), month, year)) {
                monthlyIntakes++;
            }
        }

        for (VatTuPhuTung vt : parts) {
            if (vt.getSoLuongVatTuPhuTung() <= 5) {
                lowStock++;
            }
        }

        lblMonthlyRevenue.setText(formatMoney(monthlyRevenue));
        lblReportRepairOrders.setText(String.valueOf(monthlyRepairs));
        lblReportCarsReceived.setText(String.valueOf(monthlyIntakes));
        lblLowStockParts.setText(String.valueOf(lowStock));
    }

    private void loadReport() {
        String type = cbbReportType.getValue();

        if (type == null) {
            type = "Revenue Report";
        }

        switch (type) {
            case "Repair Report":
                loadRepairReport();
                break;
            case "Inventory Report":
                loadInventoryReport();
                break;
            case "Vehicle Brand Report":
                loadVehicleBrandReport();
                break;
            default:
                loadRevenueReport();
                break;
        }
    }

    private void loadRevenueReport() {
        int month = Integer.parseInt(cbbMonth.getValue());
        int year = Integer.parseInt(cbbYear.getValue());

        ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        List<PhieuThuTien> receipts = phieuThuTienDAO.getAll();
        List<BanVatTu> sales = banVatTuDAO.getAll();

        int index = 1;
        double total = 0;

        for (PhieuThuTien pt : receipts) {
            if (isSameMonth(pt.getNgayThuTien(), month, year)) {
                total += pt.getSoTienThu();

                rows.add(row(
                        String.valueOf(index++),
                        "Repair receipt " + pt.getMaPhieuThuTien(),
                        "1",
                        formatMoney(pt.getSoTienThu()),
                        "-",
                        "Intake: " + pt.getMaTiepNhanXe()
                ));

                series.getData().add(new XYChart.Data<>(
                        pt.getMaPhieuThuTien(),
                        pt.getSoTienThu()
                ));
            }
        }

        for (BanVatTu bvt : sales) {
            if (isSameMonth(bvt.getNgayBan(), month, year)) {
                total += bvt.getTongTien();

                rows.add(row(
                        String.valueOf(index++),
                        "Part sale " + bvt.getMaBanVatTu(),
                        "1",
                        formatMoney(bvt.getTongTien()),
                        "-",
                        "Customer: " + bvt.getMaKhachHang()
                ));

                series.getData().add(new XYChart.Data<>(
                        bvt.getMaBanVatTu(),
                        bvt.getTongTien()
                ));
            }
        }

        rows.add(row("", "Total Revenue", String.valueOf(index - 1), formatMoney(total), "100%", "Repair + part sales"));

        tblReports.setItems(rows);
        updateBarChart("Revenue Report", "Invoice", "Amount", series);
    }

    private void loadRepairReport() {
        int month = Integer.parseInt(cbbMonth.getValue());
        int year = Integer.parseInt(cbbYear.getValue());

        ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        List<SuaChuaXe> repairs = suaChuaXeDAO.getAll();

        int index = 1;
        double total = 0;

        for (SuaChuaXe sc : repairs) {
            if (isSameMonth(sc.getNgaySuaChua(), month, year)) {
                total += sc.getThanhTien();

                rows.add(row(
                        String.valueOf(index++),
                        "Repair " + sc.getMaSuaChuaXe(),
                        "1",
                        formatMoney(sc.getThanhTien()),
                        "-",
                        "Intake: " + sc.getMaTiepNhanXe()
                ));

                series.getData().add(new XYChart.Data<>(
                        sc.getMaSuaChuaXe(),
                        sc.getThanhTien()
                ));
            }
        }

        rows.add(row("", "Total Repair Amount", String.valueOf(index - 1), formatMoney(total), "100%", "Monthly repairs"));

        tblReports.setItems(rows);
        updateBarChart("Repair Report", "Repair order", "Amount", series);
    }

    private void loadInventoryReport() {
        ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        List<VatTuPhuTung> parts = vatTuPhuTungDAO.getAll();

        int index = 1;
        double totalValue = 0;

        for (VatTuPhuTung vt : parts) {
            double value = vt.getSoLuongVatTuPhuTung() * vt.getDonGiaVatTuPhuTung();
            totalValue += value;

            String note = vt.getSoLuongVatTuPhuTung() <= 5 ? "Low stock" : "OK";

            rows.add(row(
                    String.valueOf(index++),
                    vt.getTenVatTuPhuTung(),
                    String.valueOf(vt.getSoLuongVatTuPhuTung()),
                    formatMoney(value),
                    "-",
                    note
            ));

            series.getData().add(new XYChart.Data<>(
                    vt.getMaVatTuPhuTung(),
                    value
            ));
        }

        rows.add(row("", "Total Inventory Value", String.valueOf(parts.size()), formatMoney(totalValue), "100%", "Inventory"));

        tblReports.setItems(rows);
        updateBarChart("Inventory Report", "Part", "Stock value", series);
    }

    private void loadVehicleBrandReport() {
        ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        List<TiepNhanXe> intakes = tiepNhanXeDAO.getALL();

        Map<String, Integer> countByBrand = new HashMap<>();

        for (TiepNhanXe tnx : intakes) {
            String brand = tnx.getMaHieuXe();
            countByBrand.put(brand, countByBrand.getOrDefault(brand, 0) + 1);
        }

        int index = 1;
        int total = intakes.size();

        for (String brand : countByBrand.keySet()) {
            int quantity = countByBrand.get(brand);
            double rate = total == 0 ? 0 : (quantity * 100.0 / total);

            rows.add(row(
                    String.valueOf(index++),
                    brand,
                    String.valueOf(quantity),
                    "-",
                    String.format("%.2f%%", rate),
                    "Vehicle brand frequency"
            ));

            series.getData().add(new XYChart.Data<>(brand, quantity));
        }

        tblReports.setItems(rows);
        updateBarChart("Vehicle Brand Report", "Brand", "Cars received", series);
    }

    private void updateBarChart(String title, String xLabel, String yLabel, XYChart.Series<String, Number> series) {
        barReportChart.getData().clear();

        chartXAxis.setLabel(xLabel);
        chartYAxis.setLabel(yLabel);

        barReportChart.setTitle(title);

        if (series.getData().isEmpty()) {
            series.getData().add(new XYChart.Data<>("No data", 0));
        }

        barReportChart.getData().add(series);
    }

    private ObservableList<String> row(String no, String name, String quantity, String amount, String rate, String note) {
        return FXCollections.observableArrayList(no, name, quantity, amount, rate, note);
    }

    private boolean isSameMonth(java.sql.Date date, int month, int year) {
        if (date == null) {
            return false;
        }

        LocalDate localDate = date.toLocalDate();
        return localDate.getMonthValue() == month && localDate.getYear() == year;
    }

    private String formatMoney(double value) {
        return currencyFormat.format(value);
    }
}