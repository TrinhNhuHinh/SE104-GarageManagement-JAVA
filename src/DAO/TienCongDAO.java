package DAO;

import Config.DBConnection;
import MODEL.TienCong;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TienCongDAO {
    
    // INSERT
    public boolean insert(TienCong tc) {
        String sql = "INSERT INTO TIENCONG VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tc.getMaTienCong());
            ps.setDouble(2, tc.getSoTienCong());
            ps.setString(3, tc.getNoiDungTienCong());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // GET ALL
    public List<TienCong> getAll() {
        List<TienCong> list = new ArrayList<>();
        String sql = "SELECT * FROM TIENCONG";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TienCong tc = new TienCong();
                tc.setMaTienCong(rs.getString("MaTienCong"));
                tc.setSoTienCong(rs.getDouble("SoTienCong"));
                tc.setNoiDungTienCong(rs.getString("NoiDungTienCong"));
                list.add(tc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // GET BY ID
    public TienCong getById(String maTienCong) {
        String sql = "SELECT * FROM TIENCONG WHERE MaTienCong = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTienCong);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TienCong tc = new TienCong();
                tc.setMaTienCong(rs.getString("MaTienCong"));
                tc.setSoTienCong(rs.getDouble("SoTienCong"));
                tc.setNoiDungTienCong(rs.getString("NoiDungTienCong"));
                return tc;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // UPDATE
    public boolean update(TienCong tc) {
        String sql = "UPDATE TIENCONG SET SoTienCong = ?, NoiDungTienCong = ? WHERE MaTienCong = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, tc.getSoTienCong());
            ps.setString(2, tc.getNoiDungTienCong());
            ps.setString(3, tc.getMaTienCong());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // DELETE
    public boolean delete(String maTienCong) {
        String sql = "DELETE FROM TIENCONG WHERE MaTienCong = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTienCong);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}