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
public class BaoCaoDoanhSo {
    
    //Thuộc tính
    private String maBaoCaoDoanhSo;
    private Date thang;
    private double tongDoanhThu;
    
    //Constructor

    public BaoCaoDoanhSo() {
    }

    public BaoCaoDoanhSo(String maBaoCaoDoanhSo, Date thang, double tongDoanhThu) {
        this.maBaoCaoDoanhSo = maBaoCaoDoanhSo;
        this.thang = thang;
        this.tongDoanhThu = tongDoanhThu;
    }
    
    //Getter

    public String getMaBaoCaoDoanhSo() {
        return maBaoCaoDoanhSo;
    }

    public Date getThang() {
        return thang;
    }

    public double getTongDoanhThu() {
        return tongDoanhThu;
    }
    
    //Setter

    public void setMaBaoCaoDoanhSo(String maBaoCaoDoanhSo) {
        this.maBaoCaoDoanhSo = maBaoCaoDoanhSo;
    }

    public void setThang(Date thang) {
        this.thang = thang;
    }

    public void setTongDoanhThu(double tongDoanhThu) {
        this.tongDoanhThu = tongDoanhThu;
    }
    
    //toString

    @Override
    public String toString() {
        return "BaoCaoDoanhSo{" + "maBaoCaoDoanhSo=" + maBaoCaoDoanhSo + ", thang=" + thang + ", tongDoanhThu=" + tongDoanhThu + '}';
    }
    
}
