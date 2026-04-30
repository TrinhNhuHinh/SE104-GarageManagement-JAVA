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
public class SuaChuaXe {
    private String maSuaChuaXe;
    private String maTiepNhanXe; // khóa ngoại tham chiếu đến TiepNhanXe
    private Date ngaySuaChua;
    private double thanhTien;
    
    //Constructor

    public SuaChuaXe() {
    }

    public SuaChuaXe(String maSuaChuaXe, String maTiepNhanXe, Date ngaySuaChua, double thanhTien) {
        this.maSuaChuaXe = maSuaChuaXe;
        this.maTiepNhanXe = maTiepNhanXe;
        this.ngaySuaChua = ngaySuaChua;
        this.thanhTien = thanhTien;
    }
    
    //Getter

    public String getMaSuaChuaXe() {
        return maSuaChuaXe;
    }

    public String getMaTiepNhanXe() {
        return maTiepNhanXe;
    }

    public Date getNgaySuaChua() {
        return ngaySuaChua;
    }

    public double getThanhTien() {
        return thanhTien;
    }
    
    //Setter

    public void setMaSuaChuaXe(String maSuaChuaXe) {
        this.maSuaChuaXe = maSuaChuaXe;
    }

    public void setMaTiepNhanXe(String maTiepNhanXe) {
        this.maTiepNhanXe = maTiepNhanXe;
    }

    public void setNgaySuaChua(Date ngaySuaChua) {
        this.ngaySuaChua = ngaySuaChua;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }
    
    //toString

    @Override
    public String toString() {
        return "SuaChuaXe{" + "maSuaChuaXe=" + maSuaChuaXe + ", maTiepNhanXe=" + maTiepNhanXe + ", ngaySuaChua=" + ngaySuaChua + ", thanhTien=" + thanhTien + '}';
    }
    
}
