/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

/**
 *
 * @author hinh
 */
public class VatTuPhuTung {
    
    //Thuộc tính
    private String maVatTuPhuTung;
    private String tenVatTuPhuTung;
    private double donGiaVatTuPhuTung;
    private int soLuongVatTuPhuTung;
    private String donViTinh;
    
    //Constructor

    public VatTuPhuTung() {
    }

    public VatTuPhuTung(String maVatTuPhuTung, String tenVatTuPhuTung, double donGiaVatTuPhuTung, int soLuongVatTuPhuTung, String donViTinh) {
        this.maVatTuPhuTung = maVatTuPhuTung;
        this.tenVatTuPhuTung = tenVatTuPhuTung;
        this.donGiaVatTuPhuTung = donGiaVatTuPhuTung;
        this.soLuongVatTuPhuTung = soLuongVatTuPhuTung;
        this.donViTinh = donViTinh;
    }
    
    //Getter

    public String getMaVatTuPhuTung() {
        return maVatTuPhuTung;
    }

    public String getTenVatTuPhuTung() {
        return tenVatTuPhuTung;
    }

    public double getDonGiaVatTuPhuTung() {
        return donGiaVatTuPhuTung;
    }

    public int getSoLuongVatTuPhuTung() {
        return soLuongVatTuPhuTung;
    }

    public String getDonViTinh() {
        return donViTinh;
    }
    
    //Setter

    public void setMaVatTuPhuTung(String maVatTuPhuTung) {
        this.maVatTuPhuTung = maVatTuPhuTung;
    }

    public void setTenVatTuPhuTung(String tenVatTuPhuTung) {
        this.tenVatTuPhuTung = tenVatTuPhuTung;
    }

    public void setDonGiaVatTuPhuTung(double donGiaVatTuPhuTung) {
        this.donGiaVatTuPhuTung = donGiaVatTuPhuTung;
    }

    public void setSoLuongVatTuPhuTung(int soLuongVatTuPhuTung) {
        this.soLuongVatTuPhuTung = soLuongVatTuPhuTung;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }
    
    //toString

    @Override
    public String toString() {
        return "VatTuPhuTung{" + "maVatTuPhuTung=" + maVatTuPhuTung + ", tenVatTuPhuTung=" + tenVatTuPhuTung + ", donGiaVatTuPhuTung=" + donGiaVatTuPhuTung + ", soLuongVatTuPhuTung=" + soLuongVatTuPhuTung + ", donViTinh=" + donViTinh + '}';
    }
    
}
