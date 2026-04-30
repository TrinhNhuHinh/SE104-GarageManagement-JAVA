/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

/**
 *
 * @author hinh
 */
public class HieuXe {
    
    //Thuộc tính 
    private String maHieuXe;
    private String tenHieuXe;
    
    //Constructor
    public HieuXe(){
        
    }

    public HieuXe(String maHieuXe, String tenHieuXe){
        this.maHieuXe = maHieuXe;
        this.tenHieuXe = tenHieuXe;
    }
    
    //Getter 
    public String getMaHieuXe() {
        return maHieuXe;
    }

    public String getTenHieuXe() {
        return tenHieuXe;
    }
    
    //Setter

    public void setMaHieuXe(String maHieuXe) {
        this.maHieuXe = maHieuXe;
    }

    public void setTenHieuXe(String tenHieuXe) {
        this.tenHieuXe = tenHieuXe;
    }
    
    // toString

    @Override
    public String toString() {
        return "HieuXe{" + "maHieuXe=" + maHieuXe + ", tenHieuXe=" + tenHieuXe + '}';
    }
    
    
}
