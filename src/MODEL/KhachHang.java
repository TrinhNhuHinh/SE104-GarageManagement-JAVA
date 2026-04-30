/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

/**
 *
 * @author hinh
 */
public class KhachHang {
    //Thuộc tính
    private String maKhachHang;
    private String tenKhachHang;
    private String diaChiKhachHang;
    private String soDienThoaiKhachHang;
    
    //Constructor 

    public KhachHang() {
    }
    

    public KhachHang(String maKhachHang, String tenKhachHang, String diaChiKhachHang, String soDienThoaiKhachHang) {
        this.maKhachHang = maKhachHang;
        this.tenKhachHang = tenKhachHang;
        this.diaChiKhachHang = diaChiKhachHang;
        this.soDienThoaiKhachHang = soDienThoaiKhachHang;
    }
    
    //Getter

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public String getDiaChiKhachHang() {
        return diaChiKhachHang;
    }

    public String getSoDienThoaiKhachHang() {
        return soDienThoaiKhachHang;
    }
    
    //Setter

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public void setDiaChiKhachHang(String diaChiKhachHang) {
        this.diaChiKhachHang = diaChiKhachHang;
    }

    public void setSoDienThoaiKhachHang(String soDienThoaiKhachHang) {
        this.soDienThoaiKhachHang = soDienThoaiKhachHang;
    }
    
    //toString

    @Override
    public String toString() {
        return "KhachHang{" + "maKhachHang=" + maKhachHang + ", tenKhachHang=" + tenKhachHang + ", diaChiKhachHang=" + diaChiKhachHang + ", soDienThoaiKhachHang=" + soDienThoaiKhachHang + '}';
    }
    
}
