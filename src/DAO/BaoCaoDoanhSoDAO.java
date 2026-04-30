package DAO;

import Config.DBConnection;
import MODEL.BaoCaoDoanhSo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BaoCaoDoanhSoDAO {
    
    public boolean insert(BaoCaoDoanhSo bc) {
        String sql = "INSERT INTO BAOCAODOANHSO VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bc.getMaBaoCaoDoanhSo());
            ps.setDate(2, bc.getThang());
            ps.setDouble(3, bc.getTongDoanhThu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<BaoCaoDoanhSo> getAll() {
        List<BaoCaoDoanhSo> list = new ArrayList<>();
        String sql = "SELECT * FROM BAOCAODOANHSO";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BaoCaoDoanhSo bc = new BaoCaoDoanhSo();
                bc.setMaBaoCaoDoanhSo(rs.getString("MaBaoCaoDoanhSo"));
                bc.setThang(rs.getDate("Thang"));
                bc.setTongDoanhThu(rs.getDouble("TongDoanhThu"));
                list.add(bc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public BaoCaoDoanhSo getById(String maBaoCaoDoanhSo) {
        String sql = "SELECT * FROM BAOCAODOANHSO WHERE MaBaoCaoDoanhSo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBaoCaoDoanhSo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                BaoCaoDoanhSo bc = new BaoCaoDoanhSo();
                bc.setMaBaoCaoDoanhSo(rs.getString("MaBaoCaoDoanhSo"));
                bc.setThang(rs.getDate("Thang"));
                bc.setTongDoanhThu(rs.getDouble("TongDoanhThu"));
                return bc;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Báo cáo có thể cần update TongDoanhThu khi thêm chi tiết mới
    public boolean update(BaoCaoDoanhSo bc) {
        String sql = "UPDATE BAOCAODOANHSO SET TongDoanhThu = ? WHERE MaBaoCaoDoanhSo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, bc.getTongDoanhThu());
            ps.setString(2, bc.getMaBaoCaoDoanhSo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(String maBaoCaoDoanhSo) {
        String sql = "DELETE FROM BAOCAODOANHSO WHERE MaBaoCaoDoanhSo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBaoCaoDoanhSo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}