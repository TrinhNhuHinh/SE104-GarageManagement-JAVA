/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Config.DBConnection;
import MODEL.KhachHang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hinh
 */
public class KhachHangDAO {
    //Insert
    public boolean insert(KhachHang kh){
        //lệnh sql
        String sql = "INSERT INTO KHACHHANG VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setString(1, kh.getMaKhachHang());
            ps.setString(2, kh.getTenKhachHang());
            ps.setString(3, kh.getDiaChiKhachHang());
            ps.setString(4, kh.getSoDienThoaiKhachHang());
            
            //Thực thi
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    
    //getAll
    public List<KhachHang>getAll(){
        List<KhachHang> list = new ArrayList<>();
        //lệnh sql
        String sql = "SELECT * FROM KHACHHANG";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
        
        ResultSet rs = ps.executeQuery();
            while(rs.next()){
                KhachHang kh = new KhachHang();
                kh.setMaKhachHang(rs.getString("MaKhachHang"));
                kh.setTenKhachHang(rs.getString("TenKhachHang"));
                kh.setDiaChiKhachHang(rs.getString("DiaChiKhachHang"));
                kh.setSoDienThoaiKhachHang(rs.getString("SoDienThoaiKhachHang"));
                list.add(kh);
            }
    }catch(SQLException e){
        e.printStackTrace();
    }
        return list;
    }
    
    //getById
    public KhachHang getById(String maKhachHang){
        
        //Lệnh sql
        String sql = "SELECT  * FROM KHACHHANG WHERE MAKHACHHANG = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setString(1, maKhachHang);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                KhachHang kh = new KhachHang();
                kh.setMaKhachHang(rs.getString("MaKhachHang"));
                kh.setTenKhachHang(rs.getString("TenKhachHang"));
                kh.setDiaChiKhachHang(rs.getString("DiaChiKhachHang"));
                kh.setSoDienThoaiKhachHang(rs.getString("SoDienThoaiKhachHang"));
                return kh;
            }
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    //Update
    public boolean update(KhachHang kh){
        
        //Lệnh sql
        String sql = "UPDATE KHACHHANG SET TENKHACHHANG = ?, DIACHIKHACHHANG = ?, SODIENTHOAIKHACHHANG = ? WHERE MAKHACHHANG = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setString(1, kh.getTenKhachHang());
            ps.setString(2, kh.getDiaChiKhachHang());
            ps.setString(3, kh.getSoDienThoaiKhachHang());
            ps.setString(4, kh.getMaKhachHang());
            
            //Thực thi
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    
    //Delete 
    public boolean delete(String maKhachHang){
        
        //Lệnh sql
        String sql = "DELETE FROM KHACHHANG WHERE MAKHACHHANG = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setString(1, maKhachHang);
            //Thực thi
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    
}
