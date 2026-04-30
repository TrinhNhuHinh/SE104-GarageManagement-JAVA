package DAO;

import Config.DBConnection;
import MODEL.ChiTietNhapVatTu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietNhapVatTuDAO {
    
    public boolean insert(ChiTietNhapVatTu ct) {
        String sql = "INSERT INTO CHITIETNHAPVATTU VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getMaNhapVatTu());
            ps.setString(2, ct.getMaVatTuPhuTung());
            ps.setInt(3, ct.getSoLuong());
            ps.setDouble(4, ct.getDonGia());
            ps.setDouble(5, ct.getThanhTien());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<ChiTietNhapVatTu> getAll() {
        List<ChiTietNhapVatTu> list = new ArrayList<>();
        String sql = "SELECT * FROM CHITIETNHAPVATTU";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietNhapVatTu ct = new ChiTietNhapVatTu();
                ct.setMaNhapVatTu(rs.getString("Ma_NhapVatTu"));
                ct.setMaVatTuPhuTung(rs.getString("Ma_VatTuPhuTung"));
                ct.setSoLuong(rs.getInt("SoLuong"));
                ct.setDonGia(rs.getDouble("DonGia"));
                ct.setThanhTien(rs.getDouble("ThanhTien"));
                list.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public ChiTietNhapVatTu getById(String maNhapVatTu, String maVatTu) {
        String sql = "SELECT * FROM CHITIETNHAPVATTU "
                   + "WHERE Ma_NhapVatTu = ? AND Ma_VatTuPhuTung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhapVatTu);
            ps.setString(2, maVatTu);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ChiTietNhapVatTu ct = new ChiTietNhapVatTu();
                ct.setMaNhapVatTu(rs.getString("Ma_NhapVatTu"));
                ct.setMaVatTuPhuTung(rs.getString("Ma_VatTuPhuTung"));
                ct.setSoLuong(rs.getInt("SoLuong"));
                ct.setDonGia(rs.getDouble("DonGia"));
                ct.setThanhTien(rs.getDouble("ThanhTien"));
                return ct;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<ChiTietNhapVatTu> getByMaNhap(String maNhapVatTu) {
        List<ChiTietNhapVatTu> list = new ArrayList<>();
        String sql = "SELECT * FROM CHITIETNHAPVATTU WHERE Ma_NhapVatTu = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhapVatTu);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietNhapVatTu ct = new ChiTietNhapVatTu();
                ct.setMaNhapVatTu(rs.getString("Ma_NhapVatTu"));
                ct.setMaVatTuPhuTung(rs.getString("Ma_VatTuPhuTung"));
                ct.setSoLuong(rs.getInt("SoLuong"));
                ct.setDonGia(rs.getDouble("DonGia"));
                ct.setThanhTien(rs.getDouble("ThanhTien"));
                list.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public boolean update(ChiTietNhapVatTu ct) {
        String sql = "UPDATE CHITIETNHAPVATTU "
                   + "SET SoLuong = ?, DonGia = ?, ThanhTien = ? "
                   + "WHERE Ma_NhapVatTu = ? AND Ma_VatTuPhuTung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ct.getSoLuong());
            ps.setDouble(2, ct.getDonGia());
            ps.setDouble(3, ct.getThanhTien());
            ps.setString(4, ct.getMaNhapVatTu());
            ps.setString(5, ct.getMaVatTuPhuTung());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(String maNhapVatTu, String maVatTu) {
        String sql = "DELETE FROM CHITIETNHAPVATTU "
                   + "WHERE Ma_NhapVatTu = ? AND Ma_VatTuPhuTung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhapVatTu);
            ps.setString(2, maVatTu);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}