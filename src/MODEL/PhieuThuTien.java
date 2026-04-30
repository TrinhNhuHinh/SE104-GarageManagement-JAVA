/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

import java.sql.Date;

/**
 *
 * @author hinh
 */
public class PhieuThuTien {
    private String maPhieuThuTien;
    private String maTiepNhanXe; //khóa ngoại tham chiếu TiepNhanXe
    private Date ngayThuTien;
    private String bienSoXe;
    private String email;
    private String soDienThoai;
    private double soTienThu;
    
    //Constructor 
    public PhieuThuTien() {
    }
    
    public PhieuThuTien(String maPhieuThuTien, String maTiepNhanXe, Date ngayThuTien, String bienSoXe, String email, String soDienThoai, double soTienThu) {
        this.maPhieuThuTien = maPhieuThuTien;
        this.maTiepNhanXe = maTiepNhanXe;
        this.ngayThuTien = ngayThuTien;
        this.bienSoXe = bienSoXe;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.soTienThu = soTienThu;
    }
    
    //Getter

    public String getMaPhieuThuTien() {
        return maPhieuThuTien;
    }

    public String getMaTiepNhanXe() {
        return maTiepNhanXe;
    }

    public Date getNgayThuTien() {
        return ngayThuTien;
    }

    public String getBienSoXe() {
        return bienSoXe;
    }

    public String getEmail() {
        return email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public double getSoTienThu() {
        return soTienThu;
    }
    
    //Setter

    public void setMaPhieuThuTien(String maPhieuThuTien) {
        this.maPhieuThuTien = maPhieuThuTien;
    }

    public void setMaTiepNhanXe(String maTiepNhanXe) {
        this.maTiepNhanXe = maTiepNhanXe;
    }

    public void setNgayThuTien(Date ngayThuTien) {
        this.ngayThuTien = ngayThuTien;
    }

    public void setBienSoXe(String bienSoXe) {
        this.bienSoXe = bienSoXe;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public void setSoTienThu(double soTienThu) {
        this.soTienThu = soTienThu;
    }
    
    //toString

    @Override
    public String toString() {
        return "PhieuThuTien{" + "maPhieuThuTien=" + maPhieuThuTien + ", maTiepNhanXe=" + maTiepNhanXe + ", ngayThuTien=" + ngayThuTien + ", bienSoXe=" + bienSoXe + ", email=" + email + ", soDienThoai=" + soDienThoai + ", soTienThu=" + soTienThu + '}';
    }
    

}
