package MODEL;

public class QuyenHan {

    private String maQuyenHan;
    private String tenQuyenHan;
    private String noiDungQuyenHan;

    public QuyenHan() {
    }

    public QuyenHan(String maQuyenHan, String tenQuyenHan, String noiDungQuyenHan) {
        this.maQuyenHan = maQuyenHan;
        this.tenQuyenHan = tenQuyenHan;
        this.noiDungQuyenHan = noiDungQuyenHan;
    }

    public String getMaQuyenHan() {
        return maQuyenHan;
    }

    public void setMaQuyenHan(String maQuyenHan) {
        this.maQuyenHan = maQuyenHan;
    }

    public String getTenQuyenHan() {
        return tenQuyenHan;
    }

    public void setTenQuyenHan(String tenQuyenHan) {
        this.tenQuyenHan = tenQuyenHan;
    }

    public String getNoiDungQuyenHan() {
        return noiDungQuyenHan;
    }

    public void setNoiDungQuyenHan(String noiDungQuyenHan) {
        this.noiDungQuyenHan = noiDungQuyenHan;
    }

    @Override
    public String toString() {
        return tenQuyenHan == null ? "" : tenQuyenHan;
    }
}
