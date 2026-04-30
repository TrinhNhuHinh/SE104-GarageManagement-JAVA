/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Config.DBConnection;
import MODEL.TiepNhanXe;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

/**
 *
 * @author hinh
 */
public class TiepNhanXeDAO {
    //insert
    public boolean insert(TiepNhanXe tnx){
        
        //Lệnh sql
        String sql = "INSERT INTO TIEPNHANXE VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setString(1, tnx.getMaTiepNhanXe());
            ps.setString(2, tnx.getMaKhachHang());
            ps.setString(3, tnx.getBienSoXe());
            ps.setString(4, tnx.getMaHieuXe());
            ps.setDate(5, tnx.getNgayTiepNhan());
            ps.setDouble(6, tnx.getTienNo());
            
            //Thực thi
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    
    //getALL
    public List<TiepNhanXe>getALL(){
        List<TiepNhanXe> list = new ArrayList<>();
        
        //lệnh sql
        String sql = "SELECT * FROM TIEPNHANXE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                 TiepNhanXe tnx = new TiepNhanXe();
                 tnx.setMaTiepNhanXe(rs.getString("MaTiepNhanXe"));
                 tnx.setMaKhachHang(rs.getString("MaKhachHang"));
                 tnx.setBienSoXe(rs.getString("BienSoXe"));
                 tnx.setMaHieuXe(rs.getString("MaHieuXe"));
                 tnx.setNgayTiepNhan(rs.getDate("NgayTiepNhan"));
                 tnx.setTienNo(rs.getDouble("TienNo"));
                 list.add(tnx);
             }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    
    //getById
    public TiepNhanXe getById(String maTiepNhanXe){
        
        //Lệnh sql
        String sql = "SELECT * FROM TIEPNHANXE WHERE MATIEPNHANXE = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setString(1, maTiepNhanXe);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                TiepNhanXe tnx = new TiepNhanXe();
                tnx.setMaTiepNhanXe(rs.getString("MaTiepNhanXe"));
                tnx.setMaKhachHang(rs.getString("MaKhachHang"));
                tnx.setBienSoXe(rs.getString("BienSoXe"));
                tnx.setMaHieuXe(rs.getString("MaHieuXe"));
                tnx.setNgayTiepNhan(rs.getDate("NgayTiepNhan"));
                tnx.setTienNo(rs.getDouble("TienNo"));
                return tnx;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    //Update
    public boolean update(TiepNhanXe tnx){
        
        //Lệnh sql
        String sql = "UPDATE TIEPNHANXE SET TIENNO = ? WHERE MATIEPNHANXE = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setDouble(1, tnx.getTienNo());
            ps.setString(2, tnx.getMaTiepNhanXe());
            
            //Thực thi
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    
    //Delete
    public boolean delete(String maTiepNhanXe){
        
        //Lệnh sql
        String sql = "DELETE * FROM TIEPNHANXE WHHERE MATIEPNHANXE = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setString(1, maTiepNhanXe);
            
            //Thực thi 
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    
}
