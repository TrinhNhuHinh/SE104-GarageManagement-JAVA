/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

/**
 *
 * @author hinh
 */
public class ChiTietSuaChuaXe {
    
    //Thuộc tính
    private String maChiTietSuaChuaXe;
    private String maSuaChuaXe; // khóa ngoại tham chiếu tới SUACHUAXE
    private String noiDung;
    private String maVatTuPhuTung; // Khóa ngoại tham chiếu tới VatTuPhuTung
    private int soLuong; 
    private double donGia;
    private String maTienCong;
    private double thanhTien;
    private double soTienCong;
    
    //Constructor

    public ChiTietSuaChuaXe() {
    }

    public ChiTietSuaChuaXe(String maChiTietSuaChuaXe, String maSuaChuaXe, String noiDung, String maVatTuPhuTung, int soLuong, double donGia, String maTienCong, double thanhTien, double soTienCong) {
        this.maChiTietSuaChuaXe = maChiTietSuaChuaXe;
        this.maSuaChuaXe = maSuaChuaXe;
        this.noiDung = noiDung;
        this.maVatTuPhuTung = maVatTuPhuTung;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.maTienCong = maTienCong;
        this.thanhTien = thanhTien;
        this.soTienCong = soTienCong;
    }
    
    //Getter

    public String getMaChiTietSuaChuaXe() {
        return maChiTietSuaChuaXe;
    }

    public String getMaSuaChuaXe() {
        return maSuaChuaXe;
    }

    public String getNoiDung() {
        return noiDung;
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

    public String getMaTienCong() {
        return maTienCong;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public double getSoTienCong() {
        return soTienCong;
    }
    
    //Setter

    public void setMaChiTietSuaChuaXe(String maChiTietSuaChuaXe) {
        this.maChiTietSuaChuaXe = maChiTietSuaChuaXe;
    }

    public void setMaSuaChuaXe(String maSuaChuaXe) {
        this.maSuaChuaXe = maSuaChuaXe;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
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

    public void setMaTienCong(String maTienCong) {
        this.maTienCong = maTienCong;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public void setSoTienCong(double soTienCong) {
        this.soTienCong = soTienCong;
    }
    
    //toString

    @Override
    public String toString() {
        return "ChiTietSuaChuaXe{" + "maChiTietSuaChuaXe=" + maChiTietSuaChuaXe + ", maSuaChuaXe=" + maSuaChuaXe + ", noiDung=" + noiDung + ", maVatTuPhuTung=" + maVatTuPhuTung + ", soLuong=" + soLuong + ", donGia=" + donGia + ", maTienCong=" + maTienCong + ", thanhTien=" + thanhTien + ", soTienCong=" + soTienCong + '}';
    }
    
}
