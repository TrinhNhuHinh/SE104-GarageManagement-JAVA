package DAO;

import Config.DBConnection;
import MODEL.VatTuPhuTung;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VatTuPhuTungDAO {
    
    //insert
    public boolean insert(VatTuPhuTung vt) {
        String sql = "INSERT INTO VATTUPHUTUNG VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vt.getMaVatTuPhuTung());
            ps.setString(2, vt.getTenVatTuPhuTung());
            ps.setDouble(3, vt.getDonGiaVatTuPhuTung());     // money
            ps.setInt(4, vt.getSoLuongVatTuPhuTung());        // ⭐ setInt
            ps.setString(5, vt.getDonViTinh());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    //getAll
    public List<VatTuPhuTung> getAll() {
        List<VatTuPhuTung> list = new ArrayList<>();
        String sql = "SELECT * FROM VATTUPHUTUNG";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                VatTuPhuTung vt = new VatTuPhuTung();
                vt.setMaVatTuPhuTung(rs.getString("MaVatTuPhuTung"));
                vt.setTenVatTuPhuTung(rs.getString("TenVatTuPhuTung"));
                vt.setDonGiaVatTuPhuTung(rs.getDouble("DonGiaVatTuPhuTung"));
                vt.setSoLuongVatTuPhuTung(rs.getInt("SoLuongVatTuPhuTung"));   // ⭐ getInt
                vt.setDonViTinh(rs.getString("DonViTinh"));
                list.add(vt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    //getById
    public VatTuPhuTung getById(String maVatTuPhuTung) {
        String sql = "SELECT * FROM VATTUPHUTUNG WHERE MaVatTuPhuTung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maVatTuPhuTung);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                VatTuPhuTung vt = new VatTuPhuTung();
                vt.setMaVatTuPhuTung(rs.getString("MaVatTuPhuTung"));
                vt.setTenVatTuPhuTung(rs.getString("TenVatTuPhuTung"));
                vt.setDonGiaVatTuPhuTung(rs.getDouble("DonGiaVatTuPhuTung"));
                vt.setSoLuongVatTuPhuTung(rs.getInt("SoLuongVatTuPhuTung"));
                vt.setDonViTinh(rs.getString("DonViTinh"));
                return vt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //update
    public boolean update(VatTuPhuTung vt) {
        String sql = "UPDATE VATTUPHUTUNG SET TenVatTuPhuTung = ?, DonGiaVatTuPhuTung = ?, SoLuongVatTuPhuTung = ?, DonViTinh = ? WHERE MaVatTuPhuTung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vt.getTenVatTuPhuTung());
            ps.setDouble(2, vt.getDonGiaVatTuPhuTung());
            ps.setInt(3, vt.getSoLuongVatTuPhuTung());
            ps.setString(4, vt.getDonViTinh());
            ps.setString(5, vt.getMaVatTuPhuTung());          // PK ở cuối
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    //delete
    public boolean delete(String maVatTuPhuTung) {
        String sql = "DELETE FROM VATTUPHUTUNG WHERE MaVatTuPhuTung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maVatTuPhuTung);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}