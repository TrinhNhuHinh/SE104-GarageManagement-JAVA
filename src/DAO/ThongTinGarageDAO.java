package DAO;

import Config.DBConnection;
import MODEL.ThongTinGarage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ThongTinGarageDAO {
    
    // Bảng này thường chỉ có 1 dòng, lấy ra dùng
    public ThongTinGarage get() {
        String sql = "SELECT * FROM THONGTINGARAGE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ThongTinGarage tt = new ThongTinGarage();
                tt.setId(rs.getString("Id"));
                tt.setSoLuongXeToiDa(rs.getInt("SoLuongXeToiDa"));
                tt.setTongSoHieuXe(rs.getInt("TongSoHieuXe"));
                tt.setSoTienThuSoVoiSoTienNo(rs.getDouble("SoTienThuSoVoiSoTienNo"));
                return tt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Insert chỉ chạy 1 lần khi setup app lần đầu
    public boolean insert(ThongTinGarage tt) {
        String sql = "INSERT INTO THONGTINGARAGE VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tt.getId());
            ps.setInt(2, tt.getSoLuongXeToiDa());
            ps.setInt(3, tt.getTongSoHieuXe());
            ps.setDouble(4, tt.getSoTienThuSoVoiSoTienNo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Update — quan trọng nhất, vì user sẽ chỉnh tham số
    public boolean update(ThongTinGarage tt) {
        String sql = "UPDATE THONGTINGARAGE "
                   + "SET SoLuongXeToiDa = ?, TongSoHieuXe = ?, SoTienThuSoVoiSoTienNo = ? "
                   + "WHERE Id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tt.getSoLuongXeToiDa());
            ps.setInt(2, tt.getTongSoHieuXe());
            ps.setDouble(3, tt.getSoTienThuSoVoiSoTienNo());
            ps.setString(4, tt.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Không cần getAll, getById, delete vì chỉ có 1 dòng duy nhất
}