/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

/**
 *
 * @author hinh
 */
public class ChiTietBaoCaoDoanhSo {
    
    //Thuộc tính
    private String maBaoCaoDoanhSo; //khóa ngoại(khóa chính) tham chiếu tới BAOCAODOANHSO
    private String maHieuXe; //khóa ngoại(khóa chính) tham chiếu tới HIEUXE
    private int soLuotSua;
    private double thanhTien;
    private double tiLe;
    
    //Constructor

    public ChiTietBaoCaoDoanhSo() {
    }

    public ChiTietBaoCaoDoanhSo(String maBaoCaoDoanhSo, String maHieuXe, int soLuotSua, double thanhTien, double tiLe) {
        this.maBaoCaoDoanhSo = maBaoCaoDoanhSo;
        this.maHieuXe = maHieuXe;
        this.soLuotSua = soLuotSua;
        this.thanhTien = thanhTien;
        this.tiLe = tiLe;
    }
    
    //Getter

    public String getMaBaoCaoDoanhSo() {
        return maBaoCaoDoanhSo;
    }

    public String getMaHieuXe() {
        return maHieuXe;
    }

    public int getSoLuotSua() {
        return soLuotSua;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public double getTiLe() {
        return tiLe;
    }
    
    //Setter

    public void setMaBaoCaoDoanhSo(String maBaoCaoDoanhSo) {
        this.maBaoCaoDoanhSo = maBaoCaoDoanhSo;
    }

    public void setMaHieuXe(String maHieuXe) {
        this.maHieuXe = maHieuXe;
    }

    public void setSoLuotSua(int soLuotSua) {
        this.soLuotSua = soLuotSua;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public void setTiLe(double tiLe) {
        this.tiLe = tiLe;
    }
    
    //toString

    @Override
    public String toString() {
        return "ChiTietBaoCaoDoanhSo{" + "maBaoCaoDoanhSo=" + maBaoCaoDoanhSo + ", maHieuXe=" + maHieuXe + ", soLuotSua=" + soLuotSua + ", thanhTien=" + thanhTien + ", tiLe=" + tiLe + '}';
    }
    
    
}
