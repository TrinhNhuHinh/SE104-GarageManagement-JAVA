/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

import java.sql.Date;

/**
 *
 * @author hinh
 */
public class BaoCaoTon {
    //Thuộc tính
    private String maBaoCaoTon;
    private Date thang;
    
    //Constructor

    public BaoCaoTon() {
    }

    public BaoCaoTon(String maBaoCaoTon, Date thang) {
        this.maBaoCaoTon = maBaoCaoTon;
        this.thang = thang;
    }
    
    //Getter

    public String getMaBaoCaoTon() {
        return maBaoCaoTon;
    }

    public Date getThang() {
        return thang;
    }
    
    //Setter

    public void setMaBaoCaoTon(String maBaoCaoTon) {
        this.maBaoCaoTon = maBaoCaoTon;
    }

    public void setThang(Date thang) {
        this.thang = thang;
    }
    
    //toString

    @Override
    public String toString() {
        return "BaoCaoTon{" + "maBaoCaoTon=" + maBaoCaoTon + ", thang=" + thang + '}';
    }
    
}
