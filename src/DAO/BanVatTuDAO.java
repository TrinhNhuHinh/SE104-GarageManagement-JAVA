package DAO;

import Config.DBConnection;
import MODEL.BanVatTu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BanVatTuDAO {
    
    //insert
    public boolean insert(BanVatTu bvt) {
        String sql = "INSERT INTO BANVATTU VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bvt.getMaBanVatTu());
            ps.setString(2, bvt.getMaKhachHang());
            ps.setDate(3, bvt.getNgayBan());
            ps.setDouble(4, bvt.getTongTien());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    //getALL
    public List<BanVatTu> getAll() {
        List<BanVatTu> list = new ArrayList<>();
        String sql = "SELECT * FROM BANVATTU";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BanVatTu bvt = new BanVatTu();
                bvt.setMaBanVatTu(rs.getString("MaBanVatTu"));
                bvt.setMaKhachHang(rs.getString("Ma_KhachHang"));
                bvt.setNgayBan(rs.getDate("NgayBan"));
                bvt.setTongTien(rs.getDouble("TongTien"));
                list.add(bvt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    //getByID
    public BanVatTu getById(String maBanVatTu) {
        String sql = "SELECT * FROM BANVATTU WHERE MaBanVatTu = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBanVatTu);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                BanVatTu bvt = new BanVatTu();
                bvt.setMaBanVatTu(rs.getString("MaBanVatTu"));
                bvt.setMaKhachHang(rs.getString("Ma_KhachHang"));
                bvt.setNgayBan(rs.getDate("NgayBan"));
                bvt.setTongTien(rs.getDouble("TongTien"));
                return bvt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //update
    public boolean update(BanVatTu bvt) {
        String sql = "UPDATE BANVATTU SET TongTien = ? WHERE MaBanVatTu = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, bvt.getTongTien());
            ps.setString(2, bvt.getMaBanVatTu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    //delete
    public boolean delete(String maBanVatTu) {
        String sql = "DELETE FROM BANVATTU WHERE MaBanVatTu = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBanVatTu);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}