/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

/**
 *
 * @author hinh
 */
public class ChiTietBaoCaoTon {
    //Thuộc tính
    private String maBaoCaoTon; //FK
    private String maVatTuPhuTung; //FK
    private int tonDau;
    private int tonCuoi;
    private int phatSinh;
    
    //Constructor

    public ChiTietBaoCaoTon() {
    }

    public ChiTietBaoCaoTon(String maBaoCaoTon, String maVatTuPhuTung, int tonDau, int tonCuoi, int phatSinh) {
        this.maBaoCaoTon = maBaoCaoTon;
        this.maVatTuPhuTung = maVatTuPhuTung;
        this.tonDau = tonDau;
        this.tonCuoi = tonCuoi;
        this.phatSinh = phatSinh;
    }
    
    //Getter

    public String getMaBaoCaoTon() {
        return maBaoCaoTon;
    }

    public String getMaVatTuPhuTung() {
        return maVatTuPhuTung;
    }

    public int getTonDau() {
        return tonDau;
    }

    public int getTonCuoi() {
        return tonCuoi;
    }

    public int getPhatSinh() {
        return phatSinh;
    }
    
    //Setter

    public void setMaBaoCaoTon(String maBaoCaoTon) {
        this.maBaoCaoTon = maBaoCaoTon;
    }

    public void setMaVatTuPhuTung(String maVatTuPhuTung) {
        this.maVatTuPhuTung = maVatTuPhuTung;
    }

    public void setTonDau(int tonDau) {
        this.tonDau = tonDau;
    }

    public void setTonCuoi(int tonCuoi) {
        this.tonCuoi = tonCuoi;
    }

    public void setPhatSinh(int phatSinh) {
        this.phatSinh = phatSinh;
    }
    
    
    //toString

    @Override
    public String toString() {
        return "ChiTietBaoCaoTon{" + "maBaoCaoTon=" + maBaoCaoTon + ", maVatTuPhuTung=" + maVatTuPhuTung + ", tonDau=" + tonDau + ", tonCuoi=" + tonCuoi + ", phatSinh=" + phatSinh + '}';
    }
    
    
}
