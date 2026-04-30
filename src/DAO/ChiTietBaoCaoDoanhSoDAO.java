package DAO;

import Config.DBConnection;
import MODEL.ChiTietBaoCaoDoanhSo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietBaoCaoDoanhSoDAO {
    
    public boolean insert(ChiTietBaoCaoDoanhSo ct) {
        String sql = "INSERT INTO CHITIETBAOCAODOANHSO VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getMaBaoCaoDoanhSo());     // PK1
            ps.setString(2, ct.getMaHieuXe());             // PK2
            ps.setInt(3, ct.getSoLuotSua());
            ps.setDouble(4, ct.getThanhTien());
            ps.setDouble(5, ct.getTiLe());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<ChiTietBaoCaoDoanhSo> getAll() {
        List<ChiTietBaoCaoDoanhSo> list = new ArrayList<>();
        String sql = "SELECT * FROM CHITIETBAOCAODOANHSO";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietBaoCaoDoanhSo ct = new ChiTietBaoCaoDoanhSo();
                ct.setMaBaoCaoDoanhSo(rs.getString("Ma_BaoCaoDoanhSo"));
                ct.setMaHieuXe(rs.getString("Ma_HieuXe"));
                ct.setSoLuotSua(rs.getInt("SoLuotSua"));
                ct.setThanhTien(rs.getDouble("ThanhTien"));
                ct.setTiLe(rs.getDouble("TiLe"));
                list.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // ⭐ getById nhận 2 THAM SỐ (vì 2 PK)
    public ChiTietBaoCaoDoanhSo getById(String maBaoCao, String maHieuXe) {
        String sql = "SELECT * FROM CHITIETBAOCAODOANHSO "
                   + "WHERE Ma_BaoCaoDoanhSo = ? AND Ma_HieuXe = ?";  // ⭐ AND nối 2 điều kiện
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBaoCao);                  // ?(1)
            ps.setString(2, maHieuXe);                  // ?(2)
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ChiTietBaoCaoDoanhSo ct = new ChiTietBaoCaoDoanhSo();
                ct.setMaBaoCaoDoanhSo(rs.getString("Ma_BaoCaoDoanhSo"));
                ct.setMaHieuXe(rs.getString("Ma_HieuXe"));
                ct.setSoLuotSua(rs.getInt("SoLuotSua"));
                ct.setThanhTien(rs.getDouble("ThanhTien"));
                ct.setTiLe(rs.getDouble("TiLe"));
                return ct;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // ⭐ getByMaBaoCao - hàm bonus rất hữu ích
    // Lấy TẤT CẢ chi tiết của 1 báo cáo (vd: BC001 có chi tiết của Honda, Yamaha, Suzuki)
    public List<ChiTietBaoCaoDoanhSo> getByMaBaoCao(String maBaoCao) {
        List<ChiTietBaoCaoDoanhSo> list = new ArrayList<>();
        String sql = "SELECT * FROM CHITIETBAOCAODOANHSO WHERE Ma_BaoCaoDoanhSo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBaoCao);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietBaoCaoDoanhSo ct = new ChiTietBaoCaoDoanhSo();
                ct.setMaBaoCaoDoanhSo(rs.getString("Ma_BaoCaoDoanhSo"));
                ct.setMaHieuXe(rs.getString("Ma_HieuXe"));
                ct.setSoLuotSua(rs.getInt("SoLuotSua"));
                ct.setThanhTien(rs.getDouble("ThanhTien"));
                ct.setTiLe(rs.getDouble("TiLe"));
                list.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // ⭐ UPDATE: WHERE có 2 dấu ? (vì 2 PK)
    public boolean update(ChiTietBaoCaoDoanhSo ct) {
        String sql = "UPDATE CHITIETBAOCAODOANHSO "
                   + "SET SoLuotSua = ?, ThanhTien = ?, TiLe = ? "
                   + "WHERE Ma_BaoCaoDoanhSo = ? AND Ma_HieuXe = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            // SET (3 cột non-PK)
            ps.setInt(1, ct.getSoLuotSua());
            ps.setDouble(2, ct.getThanhTien());
            ps.setDouble(3, ct.getTiLe());
            // WHERE (2 PK ở cuối)
            ps.setString(4, ct.getMaBaoCaoDoanhSo());     // ⭐ PK1
            ps.setString(5, ct.getMaHieuXe());             // ⭐ PK2
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // ⭐ DELETE nhận 2 THAM SỐ (vì 2 PK)
    public boolean delete(String maBaoCao, String maHieuXe) {
        String sql = "DELETE FROM CHITIETBAOCAODOANHSO "
                   + "WHERE Ma_BaoCaoDoanhSo = ? AND Ma_HieuXe = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBaoCao);
            ps.setString(2, maHieuXe);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}