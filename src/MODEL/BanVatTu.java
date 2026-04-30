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
public class BanVatTu {
    
    //Thuộc tính
    private String maBanVatTu;
    private String maKhachHang; //khóa ngoại tham chiếu tới KhachHang
    private Date ngayBan;
    private double tongTien;
    
    //Constructor

    public BanVatTu() {
    }

    public BanVatTu(String maBanVatTu, String maKhachHang, Date ngayBan, double tongTien) {
        this.maBanVatTu = maBanVatTu;
        this.maKhachHang = maKhachHang;
        this.ngayBan = ngayBan;
        this.tongTien = tongTien;
    }
    
    //Getter

    public String getMaBanVatTu() {
        return maBanVatTu;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public Date getNgayBan() {
        return ngayBan;
    }

    public double getTongTien() {
        return tongTien;
    }
    
    //Setter

    public void setMaBanVatTu(String maBanVatTu) {
        this.maBanVatTu = maBanVatTu;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public void setNgayBan(Date ngayBan) {
        this.ngayBan = ngayBan;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
    
    //toString

    @Override
    public String toString() {
        return "BanVatTu{" + "maBanVatTu=" + maBanVatTu + ", maKhachHang=" + maKhachHang + ", ngayBan=" + ngayBan + ", tongTien=" + tongTien + '}';
    }
    
}
