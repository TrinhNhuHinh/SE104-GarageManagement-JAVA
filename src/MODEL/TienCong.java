/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

/**
 *
 * @author hinh
 */
public class TienCong {
    
    //Thuộc tính
    private String maTienCong;
    private double soTienCong;
    private String noiDungTienCong;
    
    //Constructor

    public TienCong() {
    }

    public TienCong(String maTienCong, double soTienCong, String noiDungTienCong) {
        this.maTienCong = maTienCong;
        this.soTienCong = soTienCong;
        this.noiDungTienCong = noiDungTienCong;
    }
    
    //Getter

    public String getMaTienCong() {
        return maTienCong;
    }

    public double getSoTienCong() {
        return soTienCong;
    }

    public String getNoiDungTienCong() {
        return noiDungTienCong;
    }
    
    //Setter

    public void setMaTienCong(String maTienCong) {
        this.maTienCong = maTienCong;
    }

    public void setSoTienCong(double soTienCong) {
        this.soTienCong = soTienCong;
    }

    public void setNoiDungTienCong(String noiDungTienCong) {
        this.noiDungTienCong = noiDungTienCong;
    }
    
    //toString

    @Override
    public String toString() {
        return "TienCong{" + "maTienCong=" + maTienCong + ", soTienCong=" + soTienCong + ", noiDungTienCong=" + noiDungTienCong + '}';
    }
    
}
