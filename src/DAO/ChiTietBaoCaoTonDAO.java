package DAO;

import Config.DBConnection;
import MODEL.ChiTietBaoCaoTon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietBaoCaoTonDAO {
    
    public boolean insert(ChiTietBaoCaoTon ct) {
        String sql = "INSERT INTO CHITIETBAOCAOTON VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getMaBaoCaoTon());
            ps.setString(2, ct.getMaVatTuPhuTung());
            ps.setInt(3, ct.getTonDau());
            ps.setInt(4, ct.getTonCuoi());
            ps.setInt(5, ct.getPhatSinh());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<ChiTietBaoCaoTon> getAll() {
        List<ChiTietBaoCaoTon> list = new ArrayList<>();
        String sql = "SELECT * FROM CHITIETBAOCAOTON";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietBaoCaoTon ct = new ChiTietBaoCaoTon();
                ct.setMaBaoCaoTon(rs.getString("Ma_BaoCaoTon"));
                ct.setMaVatTuPhuTung(rs.getString("Ma_VatTuPhuTung"));
                ct.setTonDau(rs.getInt("TonDau"));
                ct.setTonCuoi(rs.getInt("TonCuoi"));
                ct.setPhatSinh(rs.getInt("PhatSinh"));
                list.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public ChiTietBaoCaoTon getById(String maBaoCaoTon, String maVatTu) {
        String sql = "SELECT * FROM CHITIETBAOCAOTON "
                   + "WHERE Ma_BaoCaoTon = ? AND Ma_VatTuPhuTung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBaoCaoTon);
            ps.setString(2, maVatTu);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ChiTietBaoCaoTon ct = new ChiTietBaoCaoTon();
                ct.setMaBaoCaoTon(rs.getString("Ma_BaoCaoTon"));
                ct.setMaVatTuPhuTung(rs.getString("Ma_VatTuPhuTung"));
                ct.setTonDau(rs.getInt("TonDau"));
                ct.setTonCuoi(rs.getInt("TonCuoi"));
                ct.setPhatSinh(rs.getInt("PhatSinh"));
                return ct;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Bonus: lấy tất cả chi tiết của 1 báo cáo tồn
    public List<ChiTietBaoCaoTon> getByMaBaoCao(String maBaoCaoTon) {
        List<ChiTietBaoCaoTon> list = new ArrayList<>();
        String sql = "SELECT * FROM CHITIETBAOCAOTON WHERE Ma_BaoCaoTon = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBaoCaoTon);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietBaoCaoTon ct = new ChiTietBaoCaoTon();
                ct.setMaBaoCaoTon(rs.getString("Ma_BaoCaoTon"));
                ct.setMaVatTuPhuTung(rs.getString("Ma_VatTuPhuTung"));
                ct.setTonDau(rs.getInt("TonDau"));
                ct.setTonCuoi(rs.getInt("TonCuoi"));
                ct.setPhatSinh(rs.getInt("PhatSinh"));
                list.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public boolean update(ChiTietBaoCaoTon ct) {
        String sql = "UPDATE CHITIETBAOCAOTON "
                   + "SET TonDau = ?, TonCuoi = ?, PhatSinh = ? "
                   + "WHERE Ma_BaoCaoTon = ? AND Ma_VatTuPhuTung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ct.getTonDau());
            ps.setInt(2, ct.getTonCuoi());
            ps.setInt(3, ct.getPhatSinh());
            ps.setString(4, ct.getMaBaoCaoTon());
            ps.setString(5, ct.getMaVatTuPhuTung());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean delete(String maBaoCaoTon, String maVatTu) {
        String sql = "DELETE FROM CHITIETBAOCAOTON "
                   + "WHERE Ma_BaoCaoTon = ? AND Ma_VatTuPhuTung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBaoCaoTon);
            ps.setString(2, maVatTu);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}