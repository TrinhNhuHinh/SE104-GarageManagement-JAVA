/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

/**
 *
 * @author hinh
 */
public class NguoiDung {
    private String maNguoiDung;
    private String tenTaiKhoan;
    private String matKhau;
    private String maChucVu;
    
    //Constructor

    public NguoiDung() {
    }

    public NguoiDung(String maNguoiDung, String tenTaiKhoan, String matKhau, String maChucVu) {
        this.maNguoiDung = maNguoiDung;
        this.tenTaiKhoan = tenTaiKhoan;
        this.matKhau = matKhau;
        this.maChucVu = maChucVu;
    }
    
    //Getter

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public String getTenTaiKhoan() {
        return tenTaiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public String getMaChucVu() {
        return maChucVu;
    }
    
    //Setter

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public void setTenTaiKhoan(String tenTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public void setMaChucVu(String maChucVu) {
        this.maChucVu = maChucVu;
    }
    
    //toString

    @Override
    public String toString() {
        return "NguoiDung{" + "maNguoiDung=" + maNguoiDung + ", tenTaiKhoan=" + tenTaiKhoan + ", matKhau=" + matKhau + ", maChucVu=" + maChucVu + '}';
    }
    
}
