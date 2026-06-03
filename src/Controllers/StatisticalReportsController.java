package Controllers;

import DAO.PhieuThuTienDAO;
import DAO.VatTuPhuTungDAO;
import MODEL.PhieuThuTien;
import MODEL.VatTuPhuTung;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
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

public class StatisticalReportsController implements Initializable {

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

    private final PhieuThuTienDAO phieuThuTienDAO = new PhieuThuTienDAO();
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
        btnViewReport.setOnAction(e -> {
            loadSummaryCards();
            loadReport();
        });

        btnExportReport.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Xuất file");
            alert.setHeaderText(null);
            alert.setContentText("Chức năng xuất file có thể phát triển sau. Hiện tại bảng và biểu đồ đã tải dữ liệu báo cáo.");
            alert.showAndWait();
        });

        cbbReportType.setOnAction(e -> loadReport());
    }

    private void loadSummaryCards() {
        int month = Integer.parseInt(cbbMonth.getValue());
        int year = Integer.parseInt(cbbYear.getValue());

        List<PhieuThuTien> receipts = phieuThuTienDAO.getAll();
        List<VatTuPhuTung> parts = vatTuPhuTungDAO.getAll();

        double revenue = 0;
        int receiptCount = 0;
        int warningParts = 0;
        double inventoryValue = 0;

        for (PhieuThuTien receipt : receipts) {
            if (isSameMonth(receipt.getNgayThuTien(), month, year)) {
                revenue += receipt.getSoTienThu();
                receiptCount++;
            }
        }

        for (VatTuPhuTung part : parts) {
            inventoryValue += part.getSoLuongVatTuPhuTung() * part.getDonGiaVatTuPhuTung();
            if (part.getSoLuongVatTuPhuTung() <= 5) {
                warningParts++;
            }
        }

        lblMonthlyRevenue.setText(formatMoney(revenue));
        lblReportRepairOrders.setText(String.valueOf(receiptCount));
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
        int month = Integer.parseInt(cbbMonth.getValue());
        int year = Integer.parseInt(cbbYear.getValue());

        ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<PhieuThuTien> receipts = phieuThuTienDAO.getAll();

        int index = 1;
        double total = 0;

        for (PhieuThuTien receipt : receipts) {
            if (isSameMonth(receipt.getNgayThuTien(), month, year)) {
                total += receipt.getSoTienThu();

                rows.add(row(
                        String.valueOf(index++),
                        "Phiếu thu " + receipt.getMaPhieuThuTien(),
                        "1",
                        formatMoney(receipt.getSoTienThu()),
                        "-",
                        "Mã tiếp nhận: " + receipt.getMaTiepNhanXe()
                ));

                series.getData().add(new XYChart.Data<>(
                        receipt.getMaPhieuThuTien(),
                        receipt.getSoTienThu()
                ));
            }
        }

        rows.add(row("", "Tổng doanh thu", String.valueOf(index - 1), formatMoney(total), "100%", "Phiếu thu trong kỳ"));
        tblReports.setItems(rows);
        updateBarChart("Báo cáo doanh thu tháng " + month + "/" + year, "Phiếu thu", "Số tiền", series);
    }

    private void loadInventoryReport() {
        ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<VatTuPhuTung> parts = vatTuPhuTungDAO.getAll();

        int index = 1;
        double totalValue = 0;

        for (VatTuPhuTung part : parts) {
            double value = part.getSoLuongVatTuPhuTung() * part.getDonGiaVatTuPhuTung();
            totalValue += value;

            rows.add(row(
                    String.valueOf(index++),
                    part.getTenVatTuPhuTung(),
                    String.valueOf(part.getSoLuongVatTuPhuTung()),
                    formatMoney(value),
                    "-",
                    getInventoryStatus(part)
            ));

            series.getData().add(new XYChart.Data<>(part.getMaVatTuPhuTung(), value));
        }

        rows.add(row("", "Tổng giá trị tồn", String.valueOf(parts.size()), formatMoney(totalValue), "100%", "Tồn kho hiện tại"));
        tblReports.setItems(rows);
        updateBarChart("Báo cáo vật tư tồn", "Vật tư", "Giá trị tồn", series);
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

    private String getInventoryStatus(VatTuPhuTung part) {
        if (part.getSoLuongVatTuPhuTung() <= 0) {
            return "Hết hàng";
        }

        if (part.getSoLuongVatTuPhuTung() <= 5) {
            return "Sắp hết";
        }

        return "Còn hàng";
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
