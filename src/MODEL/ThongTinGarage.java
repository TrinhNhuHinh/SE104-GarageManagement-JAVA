package MODEL;

public class ThongTinGarage {

    private String id;
    private int soLuongXeToiDa;
    private int tongSoHieuXe;
    private double soTienThuSoVoiSoTienNo;
    private int soLuongVatTuToiDa;
    private int soLuongTienCongToiDa;
    private double vatPercent;
    private double priceIncreasePercent;

    public ThongTinGarage() {
    }

    public ThongTinGarage(String id, int soLuongXeToiDa, int tongSoHieuXe,
                          double soTienThuSoVoiSoTienNo,
                          int soLuongVatTuToiDa, int soLuongTienCongToiDa) {
        this(id, soLuongXeToiDa, tongSoHieuXe, soTienThuSoVoiSoTienNo,
                soLuongVatTuToiDa, soLuongTienCongToiDa, 0, 0);
    }

    public ThongTinGarage(String id, int soLuongXeToiDa, int tongSoHieuXe,
                          double soTienThuSoVoiSoTienNo,
                          int soLuongVatTuToiDa, int soLuongTienCongToiDa,
                          double vatPercent, double priceIncreasePercent) {
        this.id = id;
        this.soLuongXeToiDa = soLuongXeToiDa;
        this.tongSoHieuXe = tongSoHieuXe;
        this.soTienThuSoVoiSoTienNo = soTienThuSoVoiSoTienNo;
        this.soLuongVatTuToiDa = soLuongVatTuToiDa;
        this.soLuongTienCongToiDa = soLuongTienCongToiDa;
        this.vatPercent = vatPercent;
        this.priceIncreasePercent = priceIncreasePercent;
    }

    public String getId() {
        return id;
    }

    public int getSoLuongXeToiDa() {
        return soLuongXeToiDa;
    }

    public int getTongSoHieuXe() {
        return tongSoHieuXe;
    }

    public double getSoTienThuSoVoiSoTienNo() {
        return soTienThuSoVoiSoTienNo;
    }

    public int getSoLuongVatTuToiDa() {
        return soLuongVatTuToiDa;
    }

    public int getSoLuongTienCongToiDa() {
        return soLuongTienCongToiDa;
    }

    public double getVatPercent() {
        return vatPercent;
    }

    public double getPriceIncreasePercent() {
        return priceIncreasePercent;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSoLuongXeToiDa(int soLuongXeToiDa) {
        this.soLuongXeToiDa = soLuongXeToiDa;
    }

    public void setTongSoHieuXe(int tongSoHieuXe) {
        this.tongSoHieuXe = tongSoHieuXe;
    }

    public void setSoTienThuSoVoiSoTienNo(double soTienThuSoVoiSoTienNo) {
        this.soTienThuSoVoiSoTienNo = soTienThuSoVoiSoTienNo;
    }

    public void setSoLuongVatTuToiDa(int soLuongVatTuToiDa) {
        this.soLuongVatTuToiDa = soLuongVatTuToiDa;
    }

    public void setSoLuongTienCongToiDa(int soLuongTienCongToiDa) {
        this.soLuongTienCongToiDa = soLuongTienCongToiDa;
    }

    public void setVatPercent(double vatPercent) {
        this.vatPercent = vatPercent;
    }

    public void setPriceIncreasePercent(double priceIncreasePercent) {
        this.priceIncreasePercent = priceIncreasePercent;
    }
}
