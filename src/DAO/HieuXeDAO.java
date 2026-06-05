/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author hinh
 */
import Config.DBConnection;
import MODEL.HieuXe;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HieuXeDAO {
    //INSERT
    public boolean insert(HieuXe hx){
        //lệnh sql
        String sql = "INSERT INTO HIEUXE VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
        
            ps.setString(1, hx.getMaHieuXe());
            ps.setString(2, hx.getTenHieuXe());
            
            //Thực thi
            return ps.executeUpdate() > 0;
        }
        
    catch(SQLException e){
        e.printStackTrace();
    }
    return false;
}
    
    //GetAll
    public List<HieuXe>getAll(){
        List<HieuXe> list = new ArrayList<>();
        String sql = "SELECT * FROM HIEUXE";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                HieuXe hx = new HieuXe();
                hx.setMaHieuXe(rs.getString("MaHieuXe"));
                hx.setTenHieuXe(rs.getString("TenHieuXe"));
                list.add(hx);
            }
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    
    //GetById
    public HieuXe getById(String maHieuXe){
        String sql = "SELECT * FROM HIEUXE WHERE MAHIEUXE = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
             ps.setString(1, maHieuXe);
             ResultSet rs = ps.executeQuery();
             if(rs.next()){
                HieuXe hx = new HieuXe();
                hx.setMaHieuXe(rs.getString("MaHieuXe"));
                hx.setTenHieuXe(rs.getString("TenHieuXe"));
                return hx;
            }
        }
        
    catch(SQLException e){
        e.printStackTrace();
    }
        return null;
    }

    public HieuXe getByName(String tenHieuXe) {
        String sql = "SELECT * FROM HIEUXE WHERE TenHieuXe = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenHieuXe);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                HieuXe hx = new HieuXe();
                hx.setMaHieuXe(rs.getString("MaHieuXe"));
                hx.setTenHieuXe(rs.getString("TenHieuXe"));
                return hx;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    //Update
    public boolean update(HieuXe hx){
        String sql = "UPDATE HIEUXE SET TENHIEUXE = ? WHERE MAHIEUXE = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, hx.getTenHieuXe());
            ps.setString(2, hx.getMaHieuXe());
            
            //Thực thi
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    //Delete
    public boolean delete(String maHieuXe){
        String sql = "DELETE FROM HIEUXE WHERE MAHIEUXE = ?";
                try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, maHieuXe);
            
            //Thực thi
            return ps.executeUpdate() > 0;
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
