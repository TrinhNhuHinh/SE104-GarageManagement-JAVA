/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

/**
 *
 * @author hinh
 */
public class ThongTinGarage {
    //Thuộc tính
    private String id;
    private int soLuongXeToiDa;
    private int tongSoHieuXe;
    private double soTienThuSoVoiSoTienNo;
    
    //Constructor

    public ThongTinGarage() {
    }

    public ThongTinGarage(String id, int soLuongXeToiDa, int tongSoHieuXe, double soTienThuSoVoiSoTienNo) {
        this.id = id;
        this.soLuongXeToiDa = soLuongXeToiDa;
        this.tongSoHieuXe = tongSoHieuXe;
        this.soTienThuSoVoiSoTienNo = soTienThuSoVoiSoTienNo;
    }
    
    //getter

    public String getId() {
        return id;
    }

    public int getSoLuongXeToiDa() {
        return soLuongXeToiDa;
    }

    public int getTongSoHieuXe() {
        return tongSoHieuXe;
    }

    public double getSoTienThuSoVoiSoTienNo() {
        return soTienThuSoVoiSoTienNo;
    }
    
    //Setter

    public void setId(String id) {
        this.id = id;
    }

    public void setSoLuongXeToiDa(int soLuongXeToiDa) {
        this.soLuongXeToiDa = soLuongXeToiDa;
    }

    public void setTongSoHieuXe(int tongSoHieuXe) {
        this.tongSoHieuXe = tongSoHieuXe;
    }

    public void setSoTienThuSoVoiSoTienNo(double soTienThuSoVoiSoTienNo) {
        this.soTienThuSoVoiSoTienNo = soTienThuSoVoiSoTienNo;
    }
    
    //toString

    @Override
    public String toString() {
        return "ThongTinGarage{" + "id=" + id + ", soLuongXeToiDa=" + soLuongXeToiDa + ", tongSoHieuXe=" + tongSoHieuXe + ", soTienThuSoVoiSoTienNo=" + soTienThuSoVoiSoTienNo + '}';
    }
    
}
