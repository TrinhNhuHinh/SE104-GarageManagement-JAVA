package DAO;

import Config.DBConnection;
import MODEL.BaoCaoTon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BaoCaoTonDAO {
    
    public boolean insert(BaoCaoTon bct) {
        String sql = "INSERT INTO BAOCAOTON VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bct.getMaBaoCaoTon());
            ps.setDate(2, bct.getThang());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<BaoCaoTon> getAll() {
        List<BaoCaoTon> list = new ArrayList<>();
        String sql = "SELECT * FROM BAOCAOTON";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BaoCaoTon bct = new BaoCaoTon();
                bct.setMaBaoCaoTon(rs.getString("MaBaoCaoTon"));
                bct.setThang(rs.getDate("Thang"));
                list.add(bct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public BaoCaoTon getById(String maBaoCaoTon) {
        String sql = "SELECT * FROM BAOCAOTON WHERE MaBaoCaoTon = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBaoCaoTon);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                BaoCaoTon bct = new BaoCaoTon();
                bct.setMaBaoCaoTon(rs.getString("MaBaoCaoTon"));
                bct.setThang(rs.getDate("Thang"));
                return bct;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Báo cáo tồn — thường không update (chỉ xem)
    public boolean update(BaoCaoTon bct) {
        String sql = "UPDATE BAOCAOTON SET Thang = ? WHERE MaBaoCaoTon = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, bct.getThang());
            ps.setString(2, bct.getMaBaoCaoTon());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(String maBaoCaoTon) {
        String sql = "DELETE FROM BAOCAOTON WHERE MaBaoCaoTon = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBaoCaoTon);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}