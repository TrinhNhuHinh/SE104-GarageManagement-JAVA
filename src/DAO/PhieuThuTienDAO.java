package DAO;

import Config.DBConnection;
import MODEL.PhieuThuTien;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhieuThuTienDAO {
    
    //Insert
    public boolean insert(PhieuThuTien pt) {
        String sql = "INSERT INTO PHIEUTHUTIEN VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pt.getMaPhieuThuTien());
            ps.setString(2, pt.getMaTiepNhanXe());
            ps.setDate(3, pt.getNgayThuTien());
            ps.setString(4, pt.getBienSoXe());
            ps.setString(5, pt.getEmail());
            ps.setString(6, pt.getSoDienThoai());
            ps.setDouble(7, pt.getSoTienThu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    //getAll
    public List<PhieuThuTien> getAll() {
        List<PhieuThuTien> list = new ArrayList<>();
        String sql = "SELECT * FROM PHIEUTHUTIEN";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PhieuThuTien pt = new PhieuThuTien();
                pt.setMaPhieuThuTien(rs.getString("MaPhieuThuTien"));
                pt.setMaTiepNhanXe(rs.getString("Ma_TiepNhanXe"));
                pt.setNgayThuTien(rs.getDate("NgayThuTien"));
                pt.setBienSoXe(rs.getString("BienSoXe"));
                pt.setEmail(rs.getString("Email"));
                pt.setSoDienThoai(rs.getString("SoDienThoai"));
                pt.setSoTienThu(rs.getDouble("SoTienThu"));
                list.add(pt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    //getById
    public PhieuThuTien getById(String maPhieuThuTien) {
        String sql = "SELECT * FROM PHIEUTHUTIEN WHERE MaPhieuThuTien = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieuThuTien);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PhieuThuTien pt = new PhieuThuTien();
                pt.setMaPhieuThuTien(rs.getString("MaPhieuThuTien"));
                pt.setMaTiepNhanXe(rs.getString("Ma_TiepNhanXe"));
                pt.setNgayThuTien(rs.getDate("NgayThuTien"));
                pt.setBienSoXe(rs.getString("BienSoXe"));
                pt.setEmail(rs.getString("Email"));
                pt.setSoDienThoai(rs.getString("SoDienThoai"));
                pt.setSoTienThu(rs.getDouble("SoTienThu"));
                return pt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Phiếu thu tiền — gần như không update, chỉ cho update SoTienThu
    public boolean update(PhieuThuTien pt) {
        String sql = "UPDATE PHIEUTHUTIEN SET SoTienThu = ? WHERE MaPhieuThuTien = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, pt.getSoTienThu());
            ps.setString(2, pt.getMaPhieuThuTien());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(String maPhieuThuTien) {
        String sql = "DELETE FROM PHIEUTHUTIEN WHERE MaPhieuThuTien = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieuThuTien);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}