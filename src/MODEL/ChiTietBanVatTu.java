/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

/**
 *
 * @author hinh
 */
public class ChiTietBanVatTu {
    
    //Thuộc tính
    private String maBanVatTu; // khóa ngoại tham chiếu tới BanVatTu
    private String maVatTuPhuTung; // khóa ngoại tham chiếu tới VatTuPhuTUNg;
    private int soLuong;
    private double donGia;
    private double thanhTien;
    
    //Constructor 

    public ChiTietBanVatTu() {
    }

    public ChiTietBanVatTu(String maBanVatTu, String maVatTuPhuTung, int soLuong, double donGia, double thanhTien) {
        this.maBanVatTu = maBanVatTu;
        this.maVatTuPhuTung = maVatTuPhuTung;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
    }
    
    //Getter

    public String getMaBanVatTu() {
        return maBanVatTu;
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

    public void setMaBanVatTu(String maBanVatTu) {
        this.maBanVatTu = maBanVatTu;
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
        return "ChiTietBanVatTu{" + "maBanVatTu=" + maBanVatTu + ", maVatTuPhuTung=" + maVatTuPhuTung + ", soLuong=" + soLuong + ", donGia=" + donGia + ", thanhTien=" + thanhTien + '}';
    }
    
}
