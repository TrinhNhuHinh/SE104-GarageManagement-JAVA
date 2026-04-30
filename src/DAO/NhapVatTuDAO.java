package DAO;

import Config.DBConnection;
import MODEL.NhapVatTu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NhapVatTuDAO {
    
    public boolean insert(NhapVatTu nvt) {
        String sql = "INSERT INTO NHAPVATTU VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nvt.getMaNhapVatTu());
            ps.setString(2, nvt.getMaNhaCungCap());
            ps.setDouble(3, nvt.getTongTien());
            ps.setDate(4, nvt.getNgayNhap());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<NhapVatTu> getAll() {
        List<NhapVatTu> list = new ArrayList<>();
        String sql = "SELECT * FROM NHAPVATTU";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NhapVatTu nvt = new NhapVatTu();
                nvt.setMaNhapVatTu(rs.getString("MaNhapVatTu"));
                nvt.setMaNhaCungCap(rs.getString("Ma_NhaCungCap"));
                nvt.setTongTien(rs.getDouble("TongTien"));
                nvt.setNgayNhap(rs.getDate("NgayNhap"));
                list.add(nvt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public NhapVatTu getById(String maNhapVatTu) {
        String sql = "SELECT * FROM NHAPVATTU WHERE MaNhapVatTu = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhapVatTu);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                NhapVatTu nvt = new NhapVatTu();
                nvt.setMaNhapVatTu(rs.getString("MaNhapVatTu"));
                nvt.setMaNhaCungCap(rs.getString("Ma_NhaCungCap"));
                nvt.setTongTien(rs.getDouble("TongTien"));
                nvt.setNgayNhap(rs.getDate("NgayNhap"));
                return nvt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Phiếu nhập — chỉ cho update TongTien (nếu cần điều chỉnh sau)
    public boolean update(NhapVatTu nvt) {
        String sql = "UPDATE NHAPVATTU SET TongTien = ? WHERE MaNhapVatTu = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, nvt.getTongTien());
            ps.setString(2, nvt.getMaNhapVatTu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(String maNhapVatTu) {
        String sql = "DELETE FROM NHAPVATTU WHERE MaNhapVatTu = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhapVatTu);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}