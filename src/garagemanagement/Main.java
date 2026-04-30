/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package garagemanagement;

import DAO.HieuXeDAO;
import DAO.KhachHangDAO;
import DAO.TiepNhanXeDAO;
import MODEL.HieuXe;
import MODEL.KhachHang;
import MODEL.TiepNhanXe;
import java.util.List;
import java.sql.Date;

public class Main {
    public static void main(String[] args) {
        HieuXeDAO hxdao = new HieuXeDAO();
        KhachHangDAO khdao = new KhachHangDAO();
        TiepNhanXeDAO tnxdao = new TiepNhanXeDAO();
        Date date = Date.valueOf("2005-12-12");
        
        /*khdao.insert(new KhachHang("kh001", "Hinh", "Cam", "cc"));*/
        tnxdao.insert(new TiepNhanXe("tnx001", "cc", "cc", "cc", date, 2.6));
        
        /*
        // Test insert (insert vài dòng để có data)
        hxdao.insert(new HieuXe("HX008", "Toyot4a"));
        hxdao.insert(new HieuXe("HX009", "Honda"));
        hxdao.insert(new HieuXe("HX007", "Yamaha"));
        
        List<KhachHang> ds1 = khdao.getAll();
        for(KhachHang kh : ds1){
            System.out.println(kh);
        }
        
        KhachHang kh1 = khdao.getById("kh001");
                if(kh1 != null){
            System.out.println("ngon" + kh1);
        }
                
        
        // Test getAll
        System.out.println("=== Danh sach hieu xe ===");
        List<HieuXe> ds = hxdao.getAll();
        for (HieuXe hx : ds) {
            System.out.println(hx);   // gọi toString() đã viết trong Model
        }
        System.out.println("Tong cong: " + ds.size() + " hieu xe");
        
        // getById
        HieuXe hx1 = hxdao.getById("HX001");
        if(hx1 != null){
            System.out.println("ngon" + hx1);
        }
        
        //delete th kh
        boolean result = khdao.delete("kh001");*/
            
    }
}
