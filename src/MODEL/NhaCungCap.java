/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

/**
 *
 * @author hinh
 */
public class NhaCungCap {
    
    //Thuộc tính
    private String maNhaCungCap;
    private String tenNhaCungCap;
    private String soDienThoaiNhaCungCap;
    private String emailNhaCungCap;
    
    //constructor

    public NhaCungCap() {
    }

    public NhaCungCap(String maNhaCungCap, String tenNhaCungCap, String soDienThoaiNhaCungCap, String emailNhaCungCap) {
        this.maNhaCungCap = maNhaCungCap;
        this.tenNhaCungCap = tenNhaCungCap;
        this.soDienThoaiNhaCungCap = soDienThoaiNhaCungCap;
        this.emailNhaCungCap = emailNhaCungCap;
    }
    
    //getter

    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public String getTenNhaCungCap() {
        return tenNhaCungCap;
    }

    public String getSoDienThoaiNhaCungCap() {
        return soDienThoaiNhaCungCap;
    }

    public String getEmailNhaCungCap() {
        return emailNhaCungCap;
    }
    
    //Setter

    public void setMaNhaCungCap(String maNhaCungCap) {
        this.maNhaCungCap = maNhaCungCap;
    }

    public void setTenNhaCungCap(String tenNhaCungCap) {
        this.tenNhaCungCap = tenNhaCungCap;
    }

    public void setSoDienThoaiNhaCungCap(String soDienThoaiNhaCungCap) {
        this.soDienThoaiNhaCungCap = soDienThoaiNhaCungCap;
    }

    public void setEmailNhaCungCap(String emailNhaCungCap) {
        this.emailNhaCungCap = emailNhaCungCap;
    }
    
    //toString

    @Override
    public String toString() {
        return "NhaCungCap{" + "maNhaCungCap=" + maNhaCungCap + ", tenNhaCungCap=" + tenNhaCungCap + ", soDienThoaiNhaCungCap=" + soDienThoaiNhaCungCap + ", emailNhaCungCap=" + emailNhaCungCap + '}';
    }
    
}
