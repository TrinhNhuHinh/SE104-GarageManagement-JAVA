package MODEL;

public class ThongTinGarage {

    private String id;
    private int soLuongXeToiDa;
    private int tongSoHieuXe;
    private double soTienThuSoVoiSoTienNo;
    private int soLuongVatTuToiDa;
    private int soLuongTienCongToiDa;

    public ThongTinGarage() {
    }

    public ThongTinGarage(String id, int soLuongXeToiDa, int tongSoHieuXe,
                          double soTienThuSoVoiSoTienNo,
                          int soLuongVatTuToiDa, int soLuongTienCongToiDa) {
        this.id = id;
        this.soLuongXeToiDa = soLuongXeToiDa;
        this.tongSoHieuXe = tongSoHieuXe;
        this.soTienThuSoVoiSoTienNo = soTienThuSoVoiSoTienNo;
        this.soLuongVatTuToiDa = soLuongVatTuToiDa;
        this.soLuongTienCongToiDa = soLuongTienCongToiDa;
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
}