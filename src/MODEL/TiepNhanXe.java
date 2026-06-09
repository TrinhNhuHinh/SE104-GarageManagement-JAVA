package MODEL;

import java.sql.Date;

public class TiepNhanXe {

    public static final String STATUS_RECEIVED = "DA_TIEP_NHAN";
    public static final String STATUS_REPAIRING = "DANG_SUA";
    public static final String STATUS_RETURNED = "DA_TRA";

    private String maTiepNhanXe;
    private String maKhachHang;
    private String bienSoXe;
    private String maHieuXe;
    private Date ngayTiepNhan;
    private double tienNo;
    private String trangThai;

    public TiepNhanXe() {
    }

    public TiepNhanXe(String maTiepNhanXe, String maKhachHang, String bienSoXe,
                     String maHieuXe, Date ngayTiepNhan, double tienNo) {
        this(maTiepNhanXe, maKhachHang, bienSoXe, maHieuXe, ngayTiepNhan, tienNo, STATUS_RECEIVED);
    }

    public TiepNhanXe(String maTiepNhanXe, String maKhachHang, String bienSoXe,
                     String maHieuXe, Date ngayTiepNhan, double tienNo, String trangThai) {
        this.maTiepNhanXe = maTiepNhanXe;
        this.maKhachHang = maKhachHang;
        this.bienSoXe = bienSoXe;
        this.maHieuXe = maHieuXe;
        this.ngayTiepNhan = ngayTiepNhan;
        this.tienNo = tienNo;
        this.trangThai = trangThai;
    }

    public String getMaTiepNhanXe() {
        return maTiepNhanXe;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public String getBienSoXe() {
        return bienSoXe;
    }

    public String getMaHieuXe() {
        return maHieuXe;
    }

    public Date getNgayTiepNhan() {
        return ngayTiepNhan;
    }

    public double getTienNo() {
        return tienNo;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public String getTrangThaiHienThi() {
        return switch (normalizeStatus(trangThai)) {
            case STATUS_REPAIRING -> "Dang sua";
            case STATUS_RETURNED -> "Da tra";
            default -> "Da tiep nhan";
        };
    }

    public void setMaTiepNhanXe(String maTiepNhanXe) {
        this.maTiepNhanXe = maTiepNhanXe;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public void setBienSoXe(String bienSoXe) {
        this.bienSoXe = bienSoXe;
    }

    public void setMaHieuXe(String maHieuXe) {
        this.maHieuXe = maHieuXe;
    }

    public void setNgayTiepNhan(Date ngayTiepNhan) {
        this.ngayTiepNhan = ngayTiepNhan;
    }

    public void setTienNo(double tienNo) {
        this.tienNo = tienNo;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public static String normalizeStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return STATUS_RECEIVED;
        }

        return status.trim();
    }

    @Override
    public String toString() {
        return "TiepNhanXe{"
                + "maTiepNhanXe=" + maTiepNhanXe
                + ", maKhachHang=" + maKhachHang
                + ", bienSoXe=" + bienSoXe
                + ", maHieuXe=" + maHieuXe
                + ", ngayTiepNhan=" + ngayTiepNhan
                + ", trangThai=" + trangThai
                + '}';
    }
}
