/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

import java.util.Date;

/**
 *
 * @author hinh
 */
public class ChiTietNhapVatTu {
    //thuộc tính
    private String maNhapVatTu; //khóa ngoại
    private String maVatTuPhuTung; //khóa ngoại
    private int soLuong;
    private double donGia;
    private double thanhTien;
    
    //Constructor

    public ChiTietNhapVatTu() {
    }

    public ChiTietNhapVatTu(String maNhapVatTu, String maVatTuPhuTung, int soLuong, double donGia, double thanhTien) {
        this.maNhapVatTu = maNhapVatTu;
        this.maVatTuPhuTung = maVatTuPhuTung;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
    }
    
    //getter

    public String getMaNhapVatTu() {
        return maNhapVatTu;
    }

    public String getMaVatTuPhuTung() {
        return maVatTuPhuTung;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public double getThanhTien() {
        return thanhTien;
    }
    
    //Setter

    public void setMaNhapVatTu(String maNhapVatTu) {
        this.maNhapVatTu = maNhapVatTu;
    }

    public void setMaVatTuPhuTung(String maVatTuPhuTung) {
        this.maVatTuPhuTung = maVatTuPhuTung;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }
    
    //toString

    @Override
    public String toString() {
        return "ChiTietNhapVatTu{" + "maNhapVatTu=" + maNhapVatTu + ", maVatTuPhuTung=" + maVatTuPhuTung + ", soLuong=" + soLuong + ", donGia=" + donGia + ", thanhTien=" + thanhTien + '}';
    }
}
