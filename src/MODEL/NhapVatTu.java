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
public class NhapVatTu {
    //Thuộc tính
    private String maNhapVatTu;
    private String maNhaCungCap; // khóa ngoại tham chiếu đến NhaCungCap
    private double tongTien;
    private Date ngayNhap;
    
    //Constructor

    public NhapVatTu() {
    }

    public NhapVatTu(String maNhapVatTu, String maNhaCungCap, double tongTien, Date ngayNhap) {
        this.maNhapVatTu = maNhapVatTu;
        this.maNhaCungCap = maNhaCungCap;
        this.tongTien = tongTien;
        this.ngayNhap = ngayNhap;
    }
    
    //Getter

    public String getMaNhapVatTu() {
        return maNhapVatTu;
    }

    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public double getTongTien() {
        return tongTien;
    }

    public Date getNgayNhap() {
        return ngayNhap;
    }
    
    //Setter

    public void setMaNhapVatTu(String maNhapVatTu) {
        this.maNhapVatTu = maNhapVatTu;
    }

    public void setMaNhaCungCap(String maNhaCungCap) {
        this.maNhaCungCap = maNhaCungCap;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public void setNgayNhap(Date ngayNhap) {
        this.ngayNhap = ngayNhap;
    }
    
    //toString

    @Override
    public String toString() {
        return "NhapVatTu{" + "maNhapVatTu=" + maNhapVatTu + ", maNhaCungCap=" + maNhaCungCap + ", tongTien=" + tongTien + ", ngayNhap=" + ngayNhap + '}';
    }
    
}
