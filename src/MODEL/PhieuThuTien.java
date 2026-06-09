package MODEL;

import java.sql.Date;

public class PhieuThuTien {

    private String maPhieuThuTien;
    private String maTiepNhanXe;
    private String maSuaChuaXe;
    private Date ngayThuTien;
    private String bienSoXe;
    private String email;
    private String soDienThoai;
    private double soTienThu;

    public PhieuThuTien() {
    }

    public PhieuThuTien(String maPhieuThuTien, String maTiepNhanXe, Date ngayThuTien,
                        String bienSoXe, String email, String soDienThoai, double soTienThu) {
        this.maPhieuThuTien = maPhieuThuTien;
        this.maTiepNhanXe = maTiepNhanXe;
        this.ngayThuTien = ngayThuTien;
        this.bienSoXe = bienSoXe;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.soTienThu = soTienThu;
    }

    public PhieuThuTien(String maPhieuThuTien, String maTiepNhanXe, String maSuaChuaXe,
                        Date ngayThuTien, String bienSoXe, String email,
                        String soDienThoai, double soTienThu) {
        this.maPhieuThuTien = maPhieuThuTien;
        this.maTiepNhanXe = maTiepNhanXe;
        this.maSuaChuaXe = maSuaChuaXe;
        this.ngayThuTien = ngayThuTien;
        this.bienSoXe = bienSoXe;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.soTienThu = soTienThu;
    }

    public String getMaPhieuThuTien() {
        return maPhieuThuTien;
    }

    public void setMaPhieuThuTien(String maPhieuThuTien) {
        this.maPhieuThuTien = maPhieuThuTien;
    }

    public String getMaTiepNhanXe() {
        return maTiepNhanXe;
    }

    public void setMaTiepNhanXe(String maTiepNhanXe) {
        this.maTiepNhanXe = maTiepNhanXe;
    }

    public String getMaSuaChuaXe() {
        return maSuaChuaXe;
    }

    public void setMaSuaChuaXe(String maSuaChuaXe) {
        this.maSuaChuaXe = maSuaChuaXe;
    }

    public Date getNgayThuTien() {
        return ngayThuTien;
    }

    public void setNgayThuTien(Date ngayThuTien) {
        this.ngayThuTien = ngayThuTien;
    }

    public String getBienSoXe() {
        return bienSoXe;
    }

    public void setBienSoXe(String bienSoXe) {
        this.bienSoXe = bienSoXe;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public double getSoTienThu() {
        return soTienThu;
    }

    public void setSoTienThu(double soTienThu) {
        this.soTienThu = soTienThu;
    }
}