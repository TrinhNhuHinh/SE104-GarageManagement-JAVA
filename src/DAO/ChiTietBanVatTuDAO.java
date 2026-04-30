package DAO;

import Config.DBConnection;
import MODEL.ChiTietBanVatTu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietBanVatTuDAO {
    
    public boolean insert(ChiTietBanVatTu ct) {
        String sql = "INSERT INTO CHITIETBANVATTU VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getMaBanVatTu());
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
    
    public List<ChiTietBanVatTu> getAll() {
        List<ChiTietBanVatTu> list = new ArrayList<>();
        String sql = "SELECT * FROM CHITIETBANVATTU";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietBanVatTu ct = new ChiTietBanVatTu();
                ct.setMaBanVatTu(rs.getString("Ma_BanVatTu"));
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
    
    public ChiTietBanVatTu getById(String maBanVatTu, String maVatTu) {
        String sql = "SELECT * FROM CHITIETBANVATTU "
                   + "WHERE Ma_BanVatTu = ? AND Ma_VatTuPhuTung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBanVatTu);
            ps.setString(2, maVatTu);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ChiTietBanVatTu ct = new ChiTietBanVatTu();
                ct.setMaBanVatTu(rs.getString("Ma_BanVatTu"));
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
    
    public List<ChiTietBanVatTu> getByMaBan(String maBanVatTu) {
        List<ChiTietBanVatTu> list = new ArrayList<>();
        String sql = "SELECT * FROM CHITIETBANVATTU WHERE Ma_BanVatTu = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBanVatTu);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietBanVatTu ct = new ChiTietBanVatTu();
                ct.setMaBanVatTu(rs.getString("Ma_BanVatTu"));
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
    
    public boolean update(ChiTietBanVatTu ct) {
        String sql = "UPDATE CHITIETBANVATTU "
                   + "SET SoLuong = ?, DonGia = ?, ThanhTien = ? "
                   + "WHERE Ma_BanVatTu = ? AND Ma_VatTuPhuTung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ct.getSoLuong());
            ps.setDouble(2, ct.getDonGia());
            ps.setDouble(3, ct.getThanhTien());
            ps.setString(4, ct.getMaBanVatTu());
            ps.setString(5, ct.getMaVatTuPhuTung());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(String maBanVatTu, String maVatTu) {
        String sql = "DELETE FROM CHITIETBANVATTU "
                   + "WHERE Ma_BanVatTu = ? AND Ma_VatTuPhuTung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBanVatTu);
            ps.setString(2, maVatTu);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}